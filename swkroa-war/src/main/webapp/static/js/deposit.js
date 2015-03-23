/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Deposit pages.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.service('depositService', ['$http', function($http) {
  this.getDeposits = function() {
    var promise = $http.get('/api/deposits');

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };

  this.getDeposit = function(depositUID) {
    var promise = $http.get('/api/deposits/' + depositUID);

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };

  this.saveDeposit = function(deposit) {
    var promise = $http.post('/api/deposits', deposit);

    promise.success = function(fn) {
      promise.then(function(response) {
        if (responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    promise.error = function(fn) {
      promise.then(function(response) {
        if (!responseSuccessful(response)) {
          fn(response.data, response.status);
        }
      });
    };

    return promise;
  };
}]);

swkroaApp.controller('depositController', ['$scope', 'depositService', 'transactionService',
    function($scope, depositService, transactionService) {

  $('#createdMessage').hide();
  $('#updatedMessage').hide();

  $scope.view     = "listing";
  $scope.original = null;

  depositService.getDeposits().success(function(data) {
    $scope.deposits = data;
  });

  $scope.addDeposit = function() {
    depositService.getDeposit(0).success(function(data) {
      $scope.deposit  = data;
      $scope.original = angular.copy(data);

      $scope.view = "add";
    });

    transactionService.getUnpaidTransactions().success(function(data) {
      $scope.unpaid = data;
    });
  };

  $scope.editDeposit = function(deposit) {
    $('#createdMessage').hide();
    $('#updatedMessage').hide();

    depositService.getDeposit(deposit.depositUID).success(function(data) {
      $scope.deposit  = data;
      $scope.original = angular.copy(data);

      $scope.view = "edit";
    });

    transactionService.getUnpaidTransactions().success(function(data) {
      $scope.unpaid = data;

      syncTransactions($scope)
    });
  };

  $scope.selectDeposit = function(deposit) {
    $scope.deposit = deposit;
  };

  $scope.deleteDeposit = function() {
  };

  $scope.cancelChanges = function() {
    $scope.view = "listing";
  };
}]);

var syncTransactions = function(scope) {
  var found = false;

  for (var idx1 = 0; idx1 < scope.deposit.transactions.length; idx1++) {
    found = false;

    for (var idx2 = 0; idx2 < scope.unpaid.length; idx2++) {
      if (scope.unpaid[idx2].transactionUID == scope.deposit.transactions[idx1].transactionUID) {
        scope.unpaid[idx2].transactionInDeposit = true
        found = true;
        break;
      }
    }

    if (!found) {
      scope.unpaid.push(scope.deposit.transactions[idx1]);
      scope.unpaid[scope.unpaid.length - 1].transactionInDeposit = true;
    }
  }
};

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

swkroaApp.controller('depositController', ['$scope', 'depositService', 'transactionService', 'codesetService',
    function($scope, depositService, transactionService, codesetService) {

  $('#createdMessage').hide();
  $('#updatedMessage').hide();

  $scope.view     = "listing";
  $scope.original = null;

  depositService.getDeposits().success(function(data) {
    $scope.deposits = data;
  });

  codesetService.getCodeValuesForCodeSet('TRANSACTION_ENTRY_TYPE').success(function(data) {
    $scope.entryTypes = data;
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

  $scope.addInvoiceToDeposit = function(transaction) {
    transaction.transactionInDeposit = true;

    var tx = angular.copy(transaction);
    tx.amountPaid = tx.amountRemaining;
    tx.amountRemaining = 0;

    // flip the polarity of the transaction entries
    for (var idx = 0; idx < tx.transactionEntries.length; idx++) {
      tx.transactionEntries[idx].transactionEntryAmount = Math.abs(tx.transactionEntries[idx].transactionEntryAmount);
    }

    $scope.deposit.transactions.push(tx);
    $scope.deposit.depositAmount += tx.amountPaid;
  };

  $scope.removeInvoiceFromDeposit = function(transaction) {
    for (var idx1 = 0; idx1 < $scope.unpaid.length; idx1++) {
      if ($scope.unpaid[idx1].transactionUID == transaction.transactionUID) {
        $scope.unpaid[idx1].transactionInDeposit = false;
        break;
      }
    }

    var idx2 = $scope.deposit.transactions.indexOf(transaction);
    $scope.deposit.transactions.splice(idx2, 1);

    $scope.deposit.depositAmount -= transaction.amountPaid;
  };
}]);

var toggleTransactionDetails = function(transaction) {
  var img       = $(transaction).children()[0];
  var collapsed = $(img).hasClass("fa-caret-right");

  var parentDiv = $(transaction).parent();
  var parentCol = $(parentDiv).parent();
  var parentRow = $(parentCol).parent();

  if (collapsed) {
    $(parentRow).siblings().removeClass("hide");
    $(img).removeClass("fa-caret-right");
    $(img).addClass("fa-caret-down");
  } else {
    $(parentRow).siblings().addClass("hide");
    $(img).addClass("fa-caret-right");
    $(img).removeClass("fa-caret-down");
  }
};

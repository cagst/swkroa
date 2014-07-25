/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 *
 * Version: 1.0.0
 */

var swkroaApp = angular.module('swkroaApp', ['ui.bootstrap', 'ui.utils'])
                       .controller('swkroaController', ['$scope', '$http', '$filter', function($scope, $http, $filter, currencyFilter) {

  $scope.query = "";

  $http.get('../svc/codeset/TRANSACTION_ENTRY_TYPE/').success(function(data) {
    $scope.transactionEntryTypes = data;
  });

  $scope.queryKeydown = function($event) {
    if ($event.keyCode == 13 && $scope.query.length >= 2) {
      $scope.getMemberships();
    }
  };

  $scope.getMemberships = function() {
    $scope.membership = null;

    var url = "../svc/memberships?q=";
    if ($scope.query && $scope.query.length > 0) {
      url = url + $scope.query;
    }

    $http.get(url).success(function(data) {
      $scope.memberships = data;
    });
  };

  $scope.getMembership = function(membershipUID) {
    $http.get('../svc/membership/' + membershipUID).success(function(data) {
      $scope.membership = data;
    });
  };

  $scope.dateOptions = {
    'year-format': "'yy'",
    'starting-day': 1,
    'show-weeks': false
  };

  $scope.removeMembership = function() {
    $scope.membership.active = false;
    $http.put("../svc/membership", $scope.membership).success(function(data) {
      $scope.membership = null;
    })
    .error(function(data) {
      // if we failed to save (remove) the membership
      // set it back to active
      // TODO: Need to add a message for the user
      $scope.membership.active = true;
    });
    $('#deleteMembership').modal('hide');
  };

  $scope.selectComment = function(comment) {
    $scope.commentIdx = $scope.membership.comments.indexOf(comment);
    $scope.comment = angular.copy(comment);
  };

  $scope.addComment = function() {
    $scope.commentIdx = -1;
    $scope.comment = {active: true, commentDate: new Date()};
  };

  $scope.saveComment = function() {
    $scope.comment.parentEntityName = "MEMBERSHIP";
    $scope.comment.parentEntityUID  = $scope.membership.membershipUID;

    $http.put("../svc/comments", $scope.comment).success(function(data) {
      if ($scope.commentIdx == -1) {
        $scope.membership.comments.push(data);
      } else {
        $scope.membership.comments[$scope.commentIdx] = data;
      }
    });
    $scope.comment = null;
    $('#modifyComment').modal('hide');
  };

  $scope.removeComment = function() {
    $scope.comment.active = false;
    $http.put("../svc/comments", $scope.comment).success(function(data) {
      $scope.membership.comments.splice($scope.commentIdx, 1);
    })
    .error(function(data) {
      // TODO: Need to add a message for the user
      $scope.comment.active = true;
      $scope.membership.comments[$scope.commentIdx] = $scope.comment;
    });
    $scope.comment = null;
    $('#deleteComment').modal('hide');
  };

//  $scope.runningBalance = function(index) {
//    var balance = 0;
//    var selectedTransactions = $scope.membership.transactions.slice(0, index + 1);
//    angular.forEach(selectedTransactions, function(transaction, idx) {
//      balance += (transaction.transactionAmount - transaction.unrelatedAmount);
//    });
//
//    return balance;
//  }

  $scope.deleteTransaction = function(transaction) {
    $scope.transactionIdx = $scope.membership.transactions.indexOf(transaction);
    $scope.transaction = angular.copy(transaction);
  };

  $scope.editTransaction = function(transaction) {
    $scope.transactionIdx = $scope.membership.transactions.indexOf(transaction);
    $scope.transaction = angular.copy(transaction);

    syncTransactionEntryType($scope);
    syncMember($scope);
    syncRelatedTransactions($scope);

    $("#modifyTransaction").modal('toggle');
    $scope.calculateTransactionAmount();
  };

  $scope.calculateTransactionAmount = function() {
    var amount = 0;
    for (var idx = 0; idx < $scope.transaction.transactionEntries.length; idx++) {
      if ($scope.transaction.transactionEntries[idx].active) {
        amount = amount + $scope.transaction.transactionEntries[idx].transactionEntryAmount;
      }
    }

    $scope.transactionAmount = $filter('currency')(amount);
  };

  $scope.addTransaction = function() {
    $scope.transactionIdx = -1;
    $scope.transaction = {active: true, transactionDate: new Date(), transactionEntries: new Array()};
  };

  $scope.removeTransaction = function() {
    $scope.transaction.active = false;
    $http.put("../svc/transaction", $scope.transaction).success(function(data) {
      $scope.membership.transactions.splice($scope.transactionIdx, 1);
    })
    .error(function(data) {
      // TODO: Need to add a message for the user
      $scope.transaction.active = true;
      $scope.membership.transactions[$scope.transactionIdx] = $scope.transaction;
    });
    $scope.transaction = null;
    $('#deleteTransaction').modal('hide');
  };

  $scope.saveTransaction = function() {
    $scope.transaction.membershipUID  = $scope.membership.membershipUID;

    $http.put("../svc/transaction", $scope.transaction).success(function(data) {
      if ($scope.transactionIdx == -1) {
        $scope.membership.transactions.push(data);
      } else {
        $scope.membership.transactions[$scope.transactionIdx] = data;
      }
    });
    $scope.transaction = null;
    $('#modifyTransaction').modal('hide');
  };

  $scope.addTransactionEntry = function() {
    var entry = {transactionEntryUID: 0, active: true, transactionEntryAmount: 0.0};
    $scope.transaction.transactionEntries.push(entry);
  };

  $scope.deleteTransactionEntry = function(entry) {
    var idx = $scope.transaction.transactionEntries.indexOf(entry);
    if (entry.transactionEntryUID == 0) {
      $scope.transaction.transactionEntries.splice(idx, 1);
    } else {
      entry.active = false;
      $scope.transaction.transactionEntries[idx] = entry;
    }

    $scope.calculateTransactionAmount();
  };
}]);

var syncTransactionEntryType = function(scope) {
  for (var idx1 = 0; idx1 < scope.transaction.transactionEntries.length; idx1++) {
    for (var idx2 = 0; idx2 < scope.transactionEntryTypes.length; idx2++) {
      if (scope.transaction.transactionEntries[idx1].transactionEntryType.codeValueUID == scope.transactionEntryTypes[idx2].codeValueUID) {
        scope.transaction.transactionEntries[idx1].transactionEntryType = scope.transactionEntryTypes[idx2];
        break;
      }
    }
  }
}

var syncMember = function(scope) {
  for (var idx1 = 0; idx1 < scope.transaction.transactionEntries.length; idx1++) {
    for (var idx2 = 0; idx2 < scope.membership.allMembers.length; idx2++) {
      if (scope.transaction.transactionEntries[idx1].member.memberUID == scope.membership.allMembers[idx2].memberUID) {
        scope.transaction.transactionEntries[idx1].member = scope.membership.allMembers[idx2];
        break;
      }
    }
  }
}

var syncRelatedTransactions = function(scope) {
  for (var idx1 = 0; idx1 < scope.transaction.transactionEntries.length; idx1++) {
    if (scope.transaction.transactionEntries[idx1].relatedTransactionUID > 0) {
      for (var idx2 = 0; idx2 < scope.membership.transactions.length; idx2++) {
        if (scope.transaction.transactionEntries[idx1].relatedTransactionUID == scope.membership.transactions[idx2].transactionUID) {
          scope.transaction.transactionEntries[idx1].relatedTransaction = scope.membership.transactions[idx2];
          break;
        }
      }
    }
  }
}

var toggleTransactionDetails = function(transaction) {
  var img       = $(transaction).children()[0];
  var collapsed = $(img).hasClass("fa-plus");

  var parentDiv = $(transaction).parent();
  var parentCol = $(parentDiv).parent();
  var parentRow = $(parentCol).parent();

  if (collapsed) {
    $(parentRow).siblings().removeClass("hide");
    $(img).removeClass("fa-plus");
    $(img).addClass("fa-minus");
  } else {
    $(parentRow).siblings().addClass("hide");
    $(img).addClass("fa-plus");
    $(img).removeClass("fa-minus");
  }
};

$(document).on('shown.bs.modal', function (event) {
  $('[autofocus]', this).focus();
});

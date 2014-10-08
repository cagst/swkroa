/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Membership pages.
 *
 * Author:  Craig Gaskill
 * Version: 1.0.0
 */

swkroaApp.controller('swkroaController', ['$scope', '$http', '$filter', function($scope, $http, $filter, currencyFilter) {

  $scope.query = "";

  $http.get('/api/codeset/TRANSACTION_ENTRY_TYPE/').success(function(data) {
    $scope.transactionEntryTypes = data;
  });

  $scope.queryKeydown = function($event) {
    if ($event.keyCode == 13 && $scope.query.length >= 2) {
      $scope.getMemberships();
    }
  };

  $scope.getMemberships = function() {
    $scope.membership = null;

    var url = "/api/memberships?q=";
    if ($scope.query && $scope.query.length > 0) {
      url = url + $scope.query;
    }

    $http.get(url).success(function(data) {
      $scope.memberships = data;
    });
  };

  $scope.getMembership = function(membershipUID) {
    $http.get('/api/membership/' + membershipUID).success(function(data) {
      $scope.selectedMembership = data;
    });
  };

  $scope.removeMembership = function() {
    $scope.selectedMembership.active = false;
    $http.put("/api/membership", $scope.selectedMembership).success(function(data) {
      $scope.selectedMembership = null;
    })
    .error(function(data) {
      // if we failed to save (remove) the membership
      // set it back to active
      // TODO: Need to add a message for the user
      $scope.selectedMembership.active = true;
    });
    $('#deleteMembership').modal('hide');
  };

  $scope.selectComment = function(comment) {
    $scope.commentIdx = $scope.selectedMembership.comments.indexOf(comment);
    $scope.comment = angular.copy(comment);
  };

  $scope.addComment = function() {
    $scope.commentIdx = -1;
    $scope.comment = {active: true, commentDate: new Date()};
  };

  $scope.saveComment = function() {
    $scope.comment.parentEntityName = "MEMBERSHIP";
    $scope.comment.parentEntityUID  = $scope.selectedMembership.membershipUID;

    $http.put("/api/comments", $scope.comment).success(function(data) {
      if ($scope.commentIdx == -1) {
        $scope.selectedMembership.comments.push(data);
      } else {
        $scope.selectedMembership.comments[$scope.commentIdx] = data;
      }
    });
    $scope.comment = null;
    $('#modifyComment').modal('hide');
  };

  $scope.removeComment = function() {
    $scope.comment.active = false;
    $http.put("/api/comments", $scope.comment).success(function(data) {
      $scope.selectedMembership.comments.splice($scope.commentIdx, 1);
    }).error(function(data) {
      // TODO: Need to add a message for the user
      $scope.comment.active = true;
      $scope.selectedMembership.comments[$scope.commentIdx] = $scope.comment;
    });
    $scope.comment = null;
    $('#deleteComment').modal('hide');
  };

//  $scope.runningBalance = function(index) {
//    var balance = 0;
//    var selectedTransactions = $scope.selectedMembership.transactions.slice(0, index + 1);
//    angular.forEach(selectedTransactions, function(transaction, idx) {
//      balance += (transaction.transactionAmount - transaction.unrelatedAmount);
//    });
//
//    return balance;
//  }

  $scope.deleteTransaction = function(transaction) {
    $scope.transactionIdx = $scope.selectedMembership.transactions.indexOf(transaction);
    $scope.transaction = angular.copy(transaction);
  };

  $scope.editTransaction = function(transaction) {
    $scope.transactionIdx = $scope.selectedMembership.transactions.indexOf(transaction);
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
    $http.put("/api/transaction", $scope.transaction).success(function(data) {
      $scope.selectedMembership.transactions.splice($scope.transactionIdx, 1);
    })
    .error(function(data) {
      // TODO: Need to add a message for the user
      $scope.transaction.active = true;
      $scope.selectedMembership.transactions[$scope.transactionIdx] = $scope.transaction;
    });
    $scope.transaction = null;
    $('#deleteTransaction').modal('hide');
  };

  $scope.saveTransaction = function() {
    $scope.transaction.membershipUID  = $scope.selectedMembership.membershipUID;

    $http.put("/api/transaction", $scope.transaction).success(function(data) {
      if ($scope.transactionIdx == -1) {
        $scope.selectedMembership.transactions.push(data);
      } else {
        $scope.selectedMembership.transactions[$scope.transactionIdx] = data;
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
    if (scope.transaction.transactionEntries[idx1].member) {
      for (var idx2 = 0; idx2 < scope.selectedMembership.allMembers.length; idx2++) {
        if (scope.transaction.transactionEntries[idx1].member.memberUID == scope.selectedMembership.allMembers[idx2].memberUID) {
          scope.transaction.transactionEntries[idx1].member = scope.selectedMembership.allMembers[idx2];
          break;
        }
      }
    }
  }
}

var syncRelatedTransactions = function(scope) {
  for (var idx1 = 0; idx1 < scope.transaction.transactionEntries.length; idx1++) {
    if (scope.transaction.transactionEntries[idx1].relatedTransactionUID > 0) {
      for (var idx2 = 0; idx2 < scope.selectedMembership.transactions.length; idx2++) {
        if (scope.transaction.transactionEntries[idx1].relatedTransactionUID == scope.selectedMembership.transactions[idx2].transactionUID) {
          scope.transaction.transactionEntries[idx1].relatedTransaction = scope.selectedMembership.transactions[idx2];
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

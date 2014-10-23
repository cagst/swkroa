/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Membership pages.
 *
 * Author:  Craig Gaskill
 * Version: 1.0.0
 */

swkroaApp.config(function($stateProvider, $urlRouterProvider) {
  $urlRouterProvider.otherwise("/home");

  $stateProvider
    .state('home', {
      url: "/home",
      views: {
        '': {
          templateUrl: "/partials/membership/listing/main.html"
        },
        'list@home': {
          templateUrl: "/partials/membership/listing/list.html"
        },
        'detail@home': {
          templateUrl: "/partials/membership/listing/detail.html"
        }
      }
    })
    .state('add', {
      url: "/add",
      views: {
        '': {
          templateUrl: "/partials/membership/modify/main.html"
        }
      }
    })
    .state('edit', {
      url: "/edit",
      views: {
        '': {
          templateUrl: "/partials/membership/modify/main.html"
        }
      }
    });
});

swkroaApp.controller('membershipController', ['$scope', '$http', '$state', '$filter', function($scope, $http, $state, $filter, currencyFilter) {
  $scope.searched = false;

  $http.get('/api/codeset/TRANSACTION_ENTRY_TYPE/').success(function(data) {
    $scope.transactionEntryTypes = data;
  });

  $scope.queryKeydown = function($event, query) {
    $scope.membership = null;

    if ($event.keyCode === 13 && query.length >= 2) {
      $scope.getMemberships(query);
    }
  };

  $scope.getMemberships = function(query) {
    $scope.membership = null;
    $scope.searched   = true;

    var url = "/api/memberships?q=";
    if (query.length >= 2) {
      url = url + query;
    }

    $http.get(url).success(function(data) {
      $scope.memberships = data;
    });
  };

  $scope.getMembership = function(membership) {
    $http.get('/api/memberships/' + membership.membershipUID).success(function(data) {
      var idx = $scope.memberships.indexOf(membership);
      $scope.memberships[idx] = data;
      $scope.share = {
        membership: data,
        successMessage: null
      }
    });
  };

  $scope.removeMembership = function() {
    $scope.selectedMembership.active = false;
    $http.put("/api/memberships", $scope.share.membership).success(function(data) {
      $scope.share.membership = null;
    })
    .error(function(data) {
      // if we failed to save (remove) the membership
      // set it back to active
      // TODO: Need to add a message for the user
      $scope.share.membership.active = true;
    });
    $('#deleteMembership').modal('hide');
  };

  $scope.selectComment = function(comment) {
    $scope.commentIdx = $scope.share.membership.comments.indexOf(comment);
    $scope.comment = angular.copy(comment);
  };

  $scope.addComment = function() {
    $scope.commentIdx = -1;
    $scope.comment = {active: true, commentDate: new Date()};
  };

  $scope.saveComment = function() {
    $scope.comment.parentEntityName = "MEMBERSHIP";
    $scope.comment.parentEntityUID  = $scope.share.membership.membershipUID;

    $http.put("/api/comments", $scope.comment).success(function(data) {
      if ($scope.commentIdx == -1) {
        $scope.share.membership.comments.push(data);
      } else {
        $scope.share.membership.comments[$scope.commentIdx] = data;
      }
    });
    $scope.comment = null;
    $('#modifyComment').modal('hide');
  };

  $scope.removeComment = function() {
    $scope.comment.active = false;
    $http.put("/api/comments", $scope.comment).success(function(data) {
      $scope.share.membership.comments.splice($scope.commentIdx, 1);
    })
    .error(function(data) {
      // TODO: Need to add a message for the user
      $scope.comment.active = true;
      $scope.share.membership.comments[$scope.commentIdx] = $scope.comment;
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
    $scope.transactionIdx = $scope.share.membership.transactions.indexOf(transaction);
    $scope.transaction = angular.copy(transaction);
  };

  $scope.editTransaction = function(transaction) {
    $scope.transactionIdx = $scope.share.membership.transactions.indexOf(transaction);
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
      $scope.share.membership.transactions.splice($scope.transactionIdx, 1);
    })
    .error(function(data) {
      // TODO: Need to add a message for the user
      $scope.transaction.active = true;
      $scope.share.membership.transactions[$scope.transactionIdx] = $scope.transaction;
    });
    $scope.transaction = null;
    $('#deleteTransaction').modal('hide');
  };

  $scope.saveTransaction = function() {
    $scope.transaction.membershipUID  = $scope.share.membership.membershipUID;

    $http.put("/api/transaction", $scope.transaction).success(function(data) {
      if ($scope.transactionIdx == -1) {
        $scope.share.membership.transactions.push(data);
      } else {
        $scope.share.membership.transactions[$scope.transactionIdx] = data;
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
      for (var idx2 = 0; idx2 < scope.share.membership.members.length; idx2++) {
        if (scope.transaction.transactionEntries[idx1].member.memberUID == scope.share.membership.members[idx2].memberUID) {
          scope.transaction.transactionEntries[idx1].member = scope.share.membership.members[idx2];
          break;
        }
      }
    }
  }
}

var syncRelatedTransactions = function(scope) {
  for (var idx1 = 0; idx1 < scope.transaction.transactionEntries.length; idx1++) {
    if (scope.transaction.transactionEntries[idx1].relatedTransactionUID > 0) {
      for (var idx2 = 0; idx2 < scope.share.membership.transactions.length; idx2++) {
        if (scope.transaction.transactionEntries[idx1].relatedTransactionUID == scope.share.membership.transactions[idx2].transactionUID) {
          scope.transaction.transactionEntries[idx1].relatedTransaction = scope.share.membership.transactions[idx2];
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

$(document).on('ready', function (event) {
  $("#query").focus();
});

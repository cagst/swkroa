/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Membership pages.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.controller('membershipController',
                     ['$scope',
                      '$http',
                      'codesetService',
                      'contactService',
                      'membershipService',
                      'transactionService',
                      '$filter',
    function($scope, $http, codesetService, contactService, membershipService, transactionService, $filter, currencyFilter)
  {

  // Define functions
  $scope.queryKeydown = function($event) {
    if ($event.keyCode == 13 && $scope.query.length >= 2) {
      $scope.firstPage();
    }
  };

  $scope.getFilters = function() {
    var filterText = "";

    if ($scope.filterStatus == 'ACTIVE') {
      if (filterText) {
        filterText = filterText + ", ";
      }
      filterText = filterText + "Active";
    } else if ($scope.filterStatus == 'INACTIVE') {
      if (filterText) {
        filterText = filterText + ", ";
      }
      filterText = filterText + "Inactive";
    }

    return filterText;
  };

  $scope.showFilterOptions = function() {
    $scope.filtering = 'on';
  };

  $scope.applyFilter = function() {
    $scope.filtering  = "off";
    $scope.filterText = $scope.getFilters();
    if ($scope.query && $scope.query.length > 0) {
      $scope.firstPage();
    }
  };

  $scope.cancelFilter = function() {
    $scope.filtering = "off";
  };

  $scope.getMembers = function(start) {
    $scope.membership = null;

    $('#createdMessage').hide();
    $('#updatedMessage').hide();

    var params = "";

    if ($scope.query && $scope.query.length > 0) {
      params = '?q=' + $scope.query;
    }

    if ($scope.filterStatus && $scope.filterStatus.length > 0) {
      if (params.length == 0) {
        params = '?';
      } else {
        params = params + '&';
      }

      params = params + 'status=' + $scope.filterStatus + '&start=' + start + '&limit=' + $scope.increment;
    }

    $http.get("/api/members/" + params).success(function(data) {
      $scope.members = data.items;
      $scope.pages   = Math.ceil(data.totalItemCount / $scope.increment);

      $scope.increments.length = 0;
      for (var idx = 0; idx < $scope.pages; idx++) {
        $scope.increments.push(idx+1);
      }
    });
  };

  $scope.firstPage = function() {
    $scope.setPage(1);
  };

  $scope.prevPage = function() {
    $scope.setPage($scope.page - 1);
  };

  $scope.setPage = function(page) {
    $scope.page = page;
    $scope.getMembers(($scope.page - 1) * $scope.increment, $scope.increment);
  };

  $scope.nextPage = function() {
    $scope.setPage($scope.page + 1);
  };

  $scope.lastPage = function() {
    $scope.setPage($scope.pages);
  };

  $scope.getMembership = function(member) {
    $('#createdMessage').hide();
    $('#updatedMessage').hide();

    $scope.member = member;

    membershipService.getMembership(member.membershipUID).then(function(response) {
      if (response.status == 200) {
        $scope.membership = response.data;
      }
    });
  };

  $scope.selectComment = function(comment) {
    $scope.commentIdx = $scope.membership.comments.indexOf(comment);
    $scope.comment    = angular.copy(comment);
  };

  $scope.addComment = function() {
    $scope.commentIdx = -1;
    $scope.comment = {active: true, commentDate: new Date()};
  };

  $scope.removeComment = function() {
    $scope.comment.active = false;
    $http.put("/api/comments", $scope.comment).success(function(data) {
      $scope.membership.comments.splice($scope.commentIdx, 1);
    }).error(function(data) {
      // TODO: Need to add a message for the user
      $scope.comment.active = true;
      $scope.membership.comments[$scope.commentIdx] = $scope.comment;
    });
    $scope.comment = null;
    $('#deleteComment').modal('hide');
  };

  $scope.saveComment = function() {
    $scope.comment.parentEntityName = "MEMBERSHIP";
    $scope.comment.parentEntityUID  = $scope.membership.membershipUID;

    $http.put("/api/comments", $scope.comment).success(function(data) {
      if ($scope.commentIdx == -1) {
        $scope.membership.comments.push(data);
      } else {
        $scope.membership.comments[$scope.commentIdx] = data;
      }
    });
    $scope.comment = null;
    $('#modifyComment').modal('hide');
  };

  $scope.deleteTransaction = function(transaction) {
    $scope.transactionIdx = $scope.membership.transactions.indexOf(transaction);
    $scope.transaction    = angular.copy(transaction);
  };

  $scope.editTransaction = function(transaction) {
    $scope.transactionIdx = $scope.membership.transactions.indexOf(transaction);
    $scope.transaction    = angular.copy(transaction);

    syncTransactionEntryType($scope);
    syncMember($scope);
    syncRelatedTransactions($scope);

    $("#modifyTransaction").modal('toggle');
    $scope.calculateTransactionAmount();
  };

  $scope.addTransaction = function() {
    $scope.transactionIdx = -1;
    $scope.transaction = {active: true, transactionDate: new Date(), transactionEntries: new Array()};
  };

  $scope.removeTransaction = function() {
    $('#deleteTransaction').modal('hide');
    $scope.transaction.active = false;

    transactionService.saveTransaction($scope.transaction).success(function(data) {
      $scope.membership.transactions.splice($scope.transactionIdx, 1);
    })
    .error(function(data) {
      $scope.transaction.active = true;
      $scope.membership.transactions[$scope.transactionIdx] = $scope.transaction;
    });

    $scope.transaction = null;
  };

  $scope.saveTransaction = function() {
    $('#modifyTransaction').modal('hide');
    $scope.transaction.membershipUID  = $scope.membership.membershipUID;

    transactionService.saveTransaction($scope.transaction).success(function(data, status) {
      if (status == 201) {
        $scope.membership.transactions.push(data);
      } else {
        $scope.membership.transactions[$scope.transactionIdx] = data;
      }
    });

    $scope.transaction = null;
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

  // Set initial values
  $scope.codesetService = codesetService;
  $scope.contactService = contactService;

  $scope.query            = "";
  $scope.filterStatus     = "ACTIVE";
  $scope.filterText       = $scope.getFilters();
  $scope.filtering        = "off";
  $scope.showCalculations = false;

  $scope.searched      = false;
  $scope.fullyLoaded   = false;
  $scope.original      = null;
  $scope.message       = null;
  $scope.increment     = 11;
  $scope.increments    = [];
  $scope.start         = 0;
  $scope.page          = 0;
  $scope.pages         = 1;

  $('#createdMessage').hide();
  $('#updatedMessage').hide();

  codesetService.getCodeValuesForCodeSet('TRANSACTION_ENTRY_TYPE').success(function(data) {
    $scope.entryTypes = data;
  });

  codesetService.getCodeValuesForCodeSet('CLOSE_REASONS').success(function(data) {
    $scope.closeReasons = data;
  });
}]);

var syncTransactionEntryType = function(scope) {
  for (var idx1 = 0; idx1 < scope.transaction.transactionEntries.length; idx1++) {
    for (var idx2 = 0; idx2 < scope.entryTypes.length; idx2++) {
      if (scope.transaction.transactionEntries[idx1].transactionEntryType.codeValueUID == scope.entryTypes[idx2].codeValueUID) {
        scope.transaction.transactionEntries[idx1].transactionEntryType = scope.entryTypes[idx2];
        break;
      }
    }
  }
}

var syncMember = function(scope) {
  for (var idx1 = 0; idx1 < scope.transaction.transactionEntries.length; idx1++) {
    if (scope.transaction.transactionEntries[idx1].member) {
      for (var idx2 = 0; idx2 < scope.membership.members.length; idx2++) {
        if (scope.transaction.transactionEntries[idx1].member.memberUID == scope.membership.members[idx2].memberUID) {
          scope.transaction.transactionEntries[idx1].member = scope.membership.members[idx2];
          break;
        }
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

$(document).on('shown.bs.modal', function (event) {
  $('[autofocus]', this).focus();
});

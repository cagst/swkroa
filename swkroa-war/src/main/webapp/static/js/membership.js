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
      $scope.getMemberships();
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

    if ($scope.filterBalance == 'DELINQUENT') {
      if (filterText) {
        filterText = filterText + ", ";
      }
      filterText = filterText + "Delinquent";
    } else if ($scope.filterBalance == 'PAID') {
      if (filterText) {
        filterText = filterText + ", ";
      }
      filterText = filterText + "Paid";
    } else if ($scope.filterBalance == 'CREDIT') {
      if (filterText) {
        filterText = filterText + ", ";
      }
      filterText = filterText + "Credit";
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
      $scope.getMemberships();
    }
  };

  $scope.cancelFilter = function() {
    $scope.filtering = "off";
  };

  $scope.getMemberships = function() {
    $scope.membership = null;

    $('#createdMessage').hide();
    $('#updatedMessage').hide();

    membershipService.getMemberships($scope.query, $scope.filterStatus, $scope.filterBalance).then(function(response) {
      if (response.status == 200) {
        $scope.memberships = response.data;
        $scope.searched    = true;
      }
    });
  };

  $scope.getMembership = function(membershipUID) {
    $('#createdMessage').hide();
    $('#updatedMessage').hide();

    membershipService.getMembership(membershipUID).then(function(response) {
      if (response.status == 200) {
        $scope.membership = response.data;
        for (var idx = 0; idx < $scope.memberships.length; idx++) {
          if ($scope.memberships[idx].membershipUID == $scope.membership.membershipUID) {
            $scope.memberships[idx] = $scope.membership;
            break;
          }
        }
      }
    });
  };

  $scope.addMembership = function() {
    membershipService.getMembership(0).then(function(response) {
      if (response.status == 200) {
        $scope.membership      = response.data;
        $scope.original        = angular.copy(response.data);
        $scope.fixedDuesAmount = ($scope.membership.fixedDuesAmount > 0);
      }
    });

    if (!$scope.fullyLoaded) {
      loadAll($scope, $http);
    } else {
      syncAllItems($scope);
    }

    $scope.view = "add";
  };

  $scope.editMembership = function() {
    if (!$scope.fullyLoaded) {
      loadAll($scope, $http);
    } else {
      syncAllItems($scope);
    }

    $scope.fixedDuesAmount = ($scope.membership.fixedDuesAmount > 0);

    $scope.original = angular.copy($scope.membership);
    $scope.view     = "edit";
  };

  $scope.openMembership = function() {
    $scope.membership.active          = true;
    $scope.membership.closeReasonUID  = null;
    $scope.membership.closeReasonText = null;
    $scope.membership.closeDate       = null;

    $('#openMembershipsDlg').modal('hide');

    membershipService.saveMembership($scope.membership).then(function(response) {
      if (response.status == 200) {
        var idx = $scope.memberships.indexOf($scope.membership);
        $scope.memberships.splice(idx, 1);
        $scope.getMemberships();
      } else {
        $scope.membership.active = false;
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

  $scope.openDueDate = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.openedDueDate = true;
  };

  $scope.openPrimaryJoinDate = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.openedPrimaryJoinDate = true;
  };

  $scope.openSpouseJoinDate = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.openedSpouseJoinDate = true;
  };

  $scope.toggleDuesAmount = function(fixed) {
    $scope.fixedDuesAmount = fixed;
  };

  $scope.addSpouse = function() {
    var spouseMember = null;
    for (idx = 0; idx < $scope.memberTypes.length; idx++) {
      if ($scope.memberTypes[idx].memberTypeMeaning === 'SPOUSE') {
        spouseMember = $scope.memberTypes[idx];
        break;
      }
    }

    $scope.membership.spouse = {
      active: true,
      memberType: spouseMember
    };
  };

  $scope.removeSpouse = function() {
    if ($scope.membership.spouse.memberUID > 0) {
      $scope.membership.spouse.active = false;
    } else {
      $scope.membership.spouse = null;
    }
  };

  $scope.addCounty = function() {
    $scope.membership.membershipCounties.push({
      netMineralAcres: 0,
      surfaceAcres: 0,
      active: true
    });
  };

  $scope.removeCounty = function(county) {
    if (county.membershipCountyUID > 0) {
      county.active = false;
    } else {
      var idx = $scope.membership.membershipCounties.indexOf(county);
      $scope.membership.membershipCounties.splice(idx, 1);
    }
  };

  $scope.validateVotingCounty = function(membership, membershipCounty) {
    if (membershipCounty.votingCounty) {
      angular.forEach(membership.membershipCounties, function(cnty) {
        cnty.votingCounty = false;
      });

      membershipCounty.votingCounty = true;
    } else {
      membershipCounty.votingCounty = false;
    }
  };

  $scope.addMember = function() {
    var familyMember = null;
    for (idx = 0; idx < $scope.memberTypes.length; idx++) {
      if ($scope.memberTypes[idx].memberTypeMeaning === 'FAMILY_MEMBER') {
        familyMember = $scope.memberTypes[idx];
        break;
      }
    }

    $scope.membership.members.push({
      active: true,
      memberType: familyMember
    });
  };

  $scope.removeMember = function(member) {
    if (member.memberUID > 0) {
      member.active = false;
    } else {
      var idx = $scope.membership.members.indexOf(member);
      $scope.membership.members.splice(idx, 1);
    }
  };

  $scope.generateOwnerId = function(member) {
    var firstName  = member.person.firstName;
    var lastName   = member.person.lastName;
    var ownerIdent = member.ownerIdent;

    if (firstName  && firstName.length > 2 &&
        lastName   && lastName.length > 2 &
        (!ownerIdent || ownerIdent.length == 0)) {

      membershipService.generateOwnerId(firstName, lastName).then(function(data) {
        member.ownerIdent = data;
      });
    };
  };

  $scope.hasChanges = function(membership) {
    return angular.equals(membership, $scope.original);
  };

  $scope.cancelChanges = function() {
    if ($scope.membership.membershipUID == 0) {
      $scope.membership = null;
    }

    $scope.view = "listing";
  };

  $scope.save = function() {
    if ($scope.fixedDuesAmount == false) {
      $scope.membership.fixedDuesAmount = null;
    }

    membershipService.saveMembership($scope.membership).then(function(response) {
      if (response.status == 201) {
        if ($scope.memberships) {
          $scope.memberships.push(response.data);
        } else {
          $scope.memberships = [response.data];
        }

        $scope.membership = response.data;
        $scope.view       = "listing";

        $('#createdMessage').show();
      } else if (response.status == 200) {
        var idx = $scope.memberships.indexOf($scope.membership);

        $scope.memberships[idx] = response.data;
        $scope.membership       = response.data;
        $scope.view             = "listing";

        $('#updatedMessage').show();
      }
    });
  };

  $scope.canClose = function() {
    if ($scope.closeReason) {
      return ($scope.closeReason.codeValueUID > 0);
    } else {
      return false;
    }
  };

  $scope.closeMembership = function() {
    var membershipIds = [];
    membershipIds.push($scope.membership.membershipUID);

    membershipService.closeMemberships(membershipIds, $scope.closeReason, $scope.closeText).success(function(data) {
      $('#closeMembershipsDlg').modal('hide');
      $scope.getMemberships();
    });
  };

  $scope.openMembership = function() {
    $scope.membership.active          = true;
    $scope.membership.closeReasonUID  = null;
    $scope.membership.closeReasonText = null;
    $scope.membership.closeDate       = null;

    $('#openMembershipsDlg').modal('hide');

    membershipService.saveMembership($scope.membership).then(function(response) {
      if (response.status == 200) {
        var idx = $scope.memberships.indexOf($scope.membership);
        $scope.memberships.splice(idx, 1);
        $scope.getMemberships();
      } else {
        $scope.membership.active = false;
      }
    });
  };

  $scope.toggleShowCalculations = function() {
    if ($scope.showCalculations) {
      $scope.showCalculations = false;
    } else {
      $scope.showCalculations = true;
    }
  };

  // Set initial values
  $scope.codesetService = codesetService;
  $scope.contactService = contactService;

  $scope.query            = "";
  $scope.view             = "listing";
  $scope.filterStatus     = "ACTIVE";
  $scope.filterBalance    = "ALL"
  $scope.filterText       = $scope.getFilters();
  $scope.filtering        = "off";
  $scope.showCalculations = false;

  $scope.searched    = false;
  $scope.fullyLoaded = false;
  $scope.original    = null;
  $scope.message     = null;

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

var loadAll = function($scope, $http) {
  if ($scope.fullyLoaded) {
    return
  }

  $scope.states = $scope.contactService.getStates();

  var syncItems    = 0;
  var syncCount    = 0;

  syncItems++;
  $http.get('/api/codesets/ENTITY_TYPE/').success(function(data) {
    $scope.entityTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  $scope.codesetService.getCodeValuesForCodeSet('TITLE').success(function(data) {
    $scope.titles = data;
  });

  $scope.codesetService.getCodeValuesForCodeSet('ADDRESS_TYPE').success(function(data) {
    $scope.addressTypes = data;
  });

  $scope.codesetService.getCodeValuesForCodeSet('PHONE_TYPE').success(function(data) {
    $scope.phoneTypes = data;
  });

  $scope.codesetService.getCodeValuesForCodeSet('EMAIL_TYPE').success(function(data) {
    $scope.emailTypes = data;
  });

  syncItems++;
  $http.get('/api/membertypes').success(function(data) {
    $scope.memberTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $http.get('/api/counties/').success(function(data) {
    $scope.counties = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  $scope.fullyLoaded = true
};

var syncAllItems = function(scope) {
  // sync up our code lists
  if (scope.membership.entityType) {
    for (var idx = 0; idx < scope.entityTypes.length; idx++) {
      if (scope.membership.entityType.codeValueUID == scope.entityTypes[idx].codeValueUID) {
        scope.membership.entityType = scope.entityTypes[idx];
        break;
      }
    }
  }

  if (scope.membership.primaryMember) {
    if (scope.membership.primaryMember.memberType) {
      for (var idx = 0; idx < scope.memberTypes.length; idx++) {
        if (scope.membership.primaryMember.memberType.memberTypeMeaning == scope.memberTypes[idx].memberTypeMeaning) {
          scope.membership.primaryMember.memberType = scope.memberTypes[idx];
          break;
        }
      }
    }
  }

  if (scope.membership.primarySpouse && scope.membership.primarySpouse.person) {
    if (scope.membership.primarySpouse.memberType) {
      for (var idx = 0; idx < scope.memberTypes.length; idx++) {
        if (scope.membership.primarySpouse.memberType.memberTypeMeaning == scope.memberTypes[idx].memberTypeMeaning) {
          scope.membership.primarySpouse.memberType = scope.memberTypes[idx];
          break;
        }
      }
    }
  }

  for (var idx1 = 0; idx1 < scope.membership.membershipCounties.length; idx1++) {
    for (var idx2 = 0; idx2 < scope.counties.length; idx2++) {
      if (scope.membership.membershipCounties[idx1].county.countyUID == scope.counties[idx2].countyUID) {
        scope.membership.membershipCounties[idx1].county = scope.counties[idx2];
        break;
      }
    }
  }
};

$(document).on('shown.bs.modal', function (event) {
  $('[autofocus]', this).focus();
});

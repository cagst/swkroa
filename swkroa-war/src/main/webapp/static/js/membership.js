/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Membership pages.
 *
 * Author:  Craig Gaskill
 * Version: 1.0.0
 */

swkroaApp.controller('swkroaController', ['$scope', '$http', '$filter', 'contactService',
    function($scope, $http, $filter, contactService, currencyFilter)
  {

  $scope.contactService = contactService;

  $scope.query = "";
  $scope.view  = "listing";

  $scope.searched    = false;
  $scope.fullyLoaded = false;
  $scope.original    = null;
  $scope.message     = null;

  $('#createdMessage').hide();
  $('#updatedMessage').hide();

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
      $scope.searched    = true;
    });
  };

  $scope.getMembership = function(membershipUID) {
    $http.get('/api/memberships/' + membershipUID).success(function(data) {
      $scope.membership = data;
    });
  };

  $scope.addMembership = function() {
    if (!$scope.fullyLoaded) {
      loadAll($scope, $http);
    } else {
      syncAllItems($scope);
    }

    $http.get('/api/memberships/0').success(function(data) {
      $scope.membership = data;
      $scope.fixedDuesAmount = ($scope.membership.duesAmount > 0);

      $scope.original = angular.copy(data);
      syncAllItems($scope);
    });

    $scope.view = "add";
  };

  $scope.editMembership = function() {
    if (!$scope.fullyLoaded) {
      loadAll($scope, $http);
    } else {
      syncAllItems($scope);
    }

    $scope.original = angular.copy($scope.membership);
    $scope.view = "edit";
  };

  $scope.removeMembership = function() {
    $scope.membership.active = false;
    $http.put("/api/membership", $scope.membership).success(function(data) {
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
    $http.put("/api/transaction", $scope.transaction).success(function(data) {
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

    $http.put("/api/transaction", $scope.transaction).success(function(data) {
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

    if ($scope.fixedDuesAmount == false) {
      $scope.membership.duesAmount = null;
    }
  };

  $scope.addSpouse = function() {
    $scope.membership.spouse = {
      active: true
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

      $http.get("/api/generateOwnerId/" + firstName + "/" + lastName).success(function(data) {
        member.ownerIdent = JSON.parse(data);
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
    $http.post("/api/memberships", $scope.membership).success(function(data, status) {
      if (status == 201) {
        $scope.memberships.push(data);
        $scope.membership = data;

        $('#createdMessage').show();
      } else {
        var idx = $scope.memberships.indexOf($scope.membership);

        $scope.memberships[idx] = data;
        $scope.membership       = data;

        $('#updatedMessage').show();
      }

      $scope.view = "listing";
    });
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
      for (var idx2 = 0; idx2 < scope.membership.allMembers.length; idx2++) {
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

var loadAll = function($scope, $http) {
  if ($scope.fullyLoaded) {
    return
  }

  $scope.states = $scope.contactService.getStates();

  var syncItems    = 0;
  var syncCount    = 0;

  syncItems++;
  $http.get('/api/codeset/ENTITY_TYPE/').success(function(data) {
    $scope.entityTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $http.get('/api/codeset/TITLE/').success(function(data) {
    $scope.titles = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $scope.contactService.getAddressTypes().then(function(data) {
    $scope.addressTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $scope.contactService.getPhoneTypes().then(function(data) {
    $scope.phoneTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $scope.contactService.getEmailTypes().then(function(data) {
    $scope.emailTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $http.get('/api/membertype').success(function(data) {
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

/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 *
 * Version: 1.0.0
 * 
 */

swkroaApp.controller('modifyMembershipController', ['$scope', '$http', '$window', 'contactService', function($scope, $http, $window, contactService) {
  var membershipId = $("#membershipId").val();
  var original     = null;
  var syncItems    = 0;
  var syncCount    = 0;

  $scope.contactService = contactService;
  $scope.states = contactService.getStates();

  syncItems++;
  $http.get('/api/codeset/ENTITY_TYPE/').success(function(data) {
    $scope.entityTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope, contactService);
    }
  });

  syncItems++;
  $http.get('/api/codeset/TITLE/').success(function(data) {
    $scope.titles = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope, contactService);
    }
  });

  syncItems++;
  contactService.getAddressTypes().then(function(data) {
    $scope.addressTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope, contactService);
    }
  });

  syncItems++;
  contactService.getPhoneTypes().then(function(data) {
    $scope.phoneTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope, contactService);
    }
  });

  syncItems++;
  contactService.getEmailTypes().then(function(data) {
    $scope.emailTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope, contactService);
    }
  });

  syncItems++;
  $http.get('/api/membertype').success(function(data) {
    $scope.memberTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope, contactService);
    }
  });

  syncItems++;
  $http.get('/api/counties/').success(function(data) {
    $scope.counties = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope, contactService);
    }
  });

  syncItems++;
  $http.get('/api/membership/' + membershipId).success(function(data) {
    $scope.membership = data;
    $scope.fixedDuesAmount = ($scope.membership.duesAmount > 0);

    original = angular.copy(data);

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope, contactService);
    }
  });

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

  $scope.addPrimaryPerson = function(member) {
    member.person = {
      active: true
    };
  }

  $scope.removePrimaryPerson = function(member) {
    member.person = null;
  }

  $scope.addSpouse = function() {
    $scope.membership.primarySpouse = {
      active: true
    };
  };

  $scope.removeSpouse = function() {
    if ($scope.membership.primarySpouse.memberUID > 0) {
      $scope.membership.primarySpouse.active = false;
    } else {
      $scope.membership.primarySpouse = null;
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

  $scope.addMember = function() {
    var familyMember = null;
    for (idx = 0; idx < $scope.memberTypes.length; idx++) {
      if ($scope.memberTypes[idx].memberTypeMeaning === 'FAMILY_MEMBER') {
        familyMember = $scope.memberTypes[idx];
        break;
      }
    }

    $scope.membership.additionalMembers.push({
      active: true,
      memberType: familyMember
    });
  };

  $scope.removeMember = function(member) {
    if (member.memberUID > 0) {
      member.active = false;
    } else {
      var idx = $scope.membership.additionalMembers.indexOf(member);
      $scope.membership.additionalMembers.splice(idx, 1);
    }
  };

  $scope.generateOwnerId = function(member) {
    var firstName  = member.person.firstName;
    var lastName   = member.person.lastName;
    var ownerIdent = member.ownerIdent;

    if (firstName  && firstName.length > 2 &&
        lastName   && lastName.length > 2 &
        (!ownerIdent || ownerIdent.length == 0)) {
      $.get("/api/generateOwnerId/" + firstName + "/" + lastName, function(data) {
        member.ownerIdent = data;
        $scope.$digest();
      });
    };
  };

  $scope.hasChanges = function(membership) {
    return angular.equals(membership, original);
  };

  $scope.save = function() {
    $http.post("/api/membership", $scope.membership).success(function(data) {
      $window.location.href = data.replace(/"/g, "");
    });
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
  }
}]);

var syncAllItems = function(scope, contactService) {
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

    if (scope.membership.primaryMember.person && scope.membership.primaryMember.person.title) {
      for (var idx = 0; idx < scope.titles.length; idx++) {
        if (scope.membership.primaryMember.person.title.codeValueUID == scope.titles[idx].codeValueUID) {
          scope.membership.primaryMember.person.title = scope.titles[idx];
          break;
        }
      }
    }

    if (scope.membership.primaryMember.person && scope.membership.primaryMember.person.gender) {
      for (var idx = 0; idx < scope.genders.length; idx++) {
        if (scope.membership.primaryMember.person.gender.codeValueUID == scope.genders[idx].codeValueUID) {
          scope.membership.primaryMember.person.gender = scope.genders[idx];
          break;
        }
      }
    }
  }

  contactService.syncAddressTypes(scope.membership.primaryMember.addresses, scope.addressTypes);
  contactService.syncPhoneTypes(scope.membership.primaryMember.phoneNumbers, scope.phoneTypes);
  contactService.syncEmailTypes(scope.membership.primaryMember.emailAddresses, scope.emailTypes);

  if (scope.membership.primarySpouse && scope.membership.primarySpouse.person) {
    if (scope.membership.primarySpouse.memberType) {
      for (var idx = 0; idx < scope.memberTypes.length; idx++) {
        if (scope.membership.primarySpouse.memberType.memberTypeMeaning == scope.memberTypes[idx].memberTypeMeaning) {
          scope.membership.primarySpouse.memberType = scope.memberTypes[idx];
          break;
        }
      }
    }

    if (scope.membership.primarySpouse.person.title) {
      for (var idx = 0; idx < scope.titles.length; idx++) {
        if (scope.membership.primarySpouse.person.title.codeValueUID == scope.titles[idx].codeValueUID) {
          scope.membership.primarySpouse.person.title = scope.titles[idx];
          break;
        }
      }
    }

    if (scope.membership.primarySpouse.person.gender) {
      for (var idx = 0; idx < scope.genders.length; idx++) {
        if (scope.membership.primarySpouse.person.gender.codeValueUID == scope.genders[idx].codeValueUID) {
          scope.membership.primarySpouse.person.gender = scope.genders[idx];
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

  for (var idx1 = 0; idx1 < scope.membership.additionalMembers.length; idx1++) {
    if (scope.membership.additionalMembers[idx1].person && scope.membership.additionalMembers[idx1].person.title) {
      for (var idx2 = 0; idx2 < scope.titles.length; idx2++) {
        if (scope.membership.additionalMembers[idx1].person.title.codeValueUID == scope.titles[idx2].codeValueUID) {
          scope.membership.additionalMembers[idx1].person.title = scope.titles[idx2];
          break;
        }
      }
    }

    if (scope.membership.additionalMembers[idx1].person && scope.membership.additionalMembers[idx1].person.gender) {
      for (var idx2 = 0; idx2 < scope.genders.length; idx2++) {
        if (scope.membership.additionalMembers[idx1].person.gender.codeValueUID == scope.genders[idx2].codeValueUID) {
          scope.membership.additionalMembers[idx1].person.gender = scope.genders[idx2];
          break;
        }
      }
    }

    contactService.syncAddressTypes(scope.membership.additionalMembers[idx1].addresses, scope.addressTypes);
    contactService.syncPhoneTypes(scope.membership.additionalMembers[idx1].phoneNumbers, scope.phoneTypes);
    contactService.syncEmailTypes(scope.membership.additionalMembers[idx1].emailAddresses, scope.emailTypes);
  }
}

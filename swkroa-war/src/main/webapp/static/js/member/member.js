/**
 * (c) 2016 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for the Member pages.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.controller('memberController',
                     ['$scope',
                      '$http',
                      'codesetService',
                      'contactService',
                      'membershipService',
  function ($scope, $http, codesetService, contactService, membershipService) {
    $scope.contactService = contactService;
    $scope.codesetService = codesetService;

    codesetService.getCodeValuesForCodeSet('ADDRESS_TYPE').success(function(data) {
      $scope.addressTypes = data;
    });

    codesetService.getCodeValuesForCodeSet('PHONE_TYPE').success(function(data) {
      $scope.phoneTypes = data;
    });

    codesetService.getCodeValuesForCodeSet('EMAIL_TYPE').success(function(data) {
      $scope.emailTypes = data;
    });

    var membershipId = $('#membershipUID').val();

    var including = ['LOAD_MEMBERS', 'LOAD_CONTACTS'];

    $scope.view = "view";
    $scope.fullyLoaded = false;
    $scope.isPrimaryMember = ('true' === $('#isPrimaryMember').val());

    membershipService.getMembership(membershipId, including).then(function(response) {
      if (responseSuccessful(response)) {
        $scope.membership = response.data;
      }
    });

    $scope.editMembership = function() {
      if (!$scope.fullyLoaded) {
        loadAll($scope, $http);
      } else {
        syncAllItems($scope);
      }

      $scope.original = angular.copy($scope.membership);
      $scope.view     = "edit";
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
      }
    };

    $scope.hasChanges = function(membership) {
      return angular.equals(membership, $scope.original);
    };

    $scope.cancelChanges = function() {
      $scope.view = "view";
    };

  }]);

var loadAll = function($scope, $http) {
  if ($scope.fullyLoaded) {
    return
  }

  $scope.states = $scope.contactService.getStates();

  var syncItems    = 0;
  var syncCount    = 0;

  syncItems++;

  $scope.codesetService.getCodeValuesForCodeSet('TITLE').success(function(data) {
    $scope.titles = data;
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

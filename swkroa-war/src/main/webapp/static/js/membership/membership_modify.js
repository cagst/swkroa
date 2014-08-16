/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 *
 * Version: 1.0.0
 * 
 */

var msModifyApp = angular.module('msModifyApp', ['ui.bootstrap', 'ui.utils']);

msModifyApp.controller('modifyController', ['$scope', '$http', '$window', function($scope, $http, $window) {
  var membershipId = $("#membershipId").val();
  var original     = null;
  var syncItems    = 0;
  var syncCount    = 0;

  syncItems++;
  $http.get('../svc/codeset/ENTITY_TYPE/').success(function(data) {
    $scope.entityTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $http.get('../svc/codeset/TITLE/').success(function(data) {
    $scope.titles = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $http.get('../svc/codeset/ADDRESS_TYPE').success(function(data) {
    $scope.addressTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $http.get('../svc/codeset/PHONE_TYPE').success(function(data) {
    $scope.phoneTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $http.get('../svc/codeset/EMAIL_TYPE').success(function(data) {
    $scope.emailTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $http.get('../svc/membertype').success(function(data) {
    $scope.memberTypes = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $http.get('../svc/counties/').success(function(data) {
    $scope.counties = data;

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
    }
  });

  syncItems++;
  $http.get('../svc/membership/' + membershipId).success(function(data) {
    $scope.membership = data;
    $scope.fixedDuesAmount = ($scope.membership.duesAmount > 0);

    original = angular.copy(data);

    syncCount++;
    if (syncCount == syncItems) {
      syncAllItems($scope);
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
      $.get("../svc/generateOwnerId/" + firstName + "/" + lastName, function(data) {
        member.ownerIdent = data;
        $scope.$digest();
      });
    };
  };

  $scope.addAddress = function(member) {
    if (!member.addresses) {
      member.addresses = new Array();
    }

    member.addresses.push({
      active: true
    });
  };

	$scope.removeAddress = function(member, address) {
		if (address.addressUID > 0) {
			address.active = false;
		} else {
			var idx = member.addresses.indexOf(address);
			member.addresses.splice(idx, 1);
		}
	};

	$scope.addPhone = function(member) {
		if (!member.phoneNumbers) {
			member.phoneNumbers = new Array();
		}
		
		member.phoneNumbers.push({
			active: true
		});
	};

	$scope.removePhone = function(member, phone) {
		if (phone.phoneUID > 0) {
			phone.active = false;
		} else {
			var idx = member.phoneNumbers.indexOf(phone);
			member.phoneNumbers.splice(idx, 1);
		}
	};

	$scope.addEmail = function(member) {
		if (!member.emailAddresses) {
			member.emailAddresses = new Array();
		}
		member.emailAddresses.push({
			active: true
		});
	};

	$scope.removeEmail = function(member, email) {
		if (email.emailAddressUID > 0) {
			email.active = false;
		} else {
			var idx = member.emailAddresses.indexOf(email);
			member.emailAddresses.splice(idx, 1);
		}
	};

	$scope.hasChanges = function(membership) {
		return angular.equals(membership, original);
	};

	$scope.save = function() {
		$http.post("../svc/membership", $scope.membership).success(function(data) {
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

	for (var idx1 = 0; idx1 < scope.membership.primaryMember.addresses.length; idx1++) {
		for (var idx2 = 0; idx2 < scope.addressTypes.length; idx2++) {
			if (scope.membership.primaryMember.addresses[idx1].addressType.codeValueUID == scope.addressTypes[idx2].codeValueUID) {
				scope.membership.primaryMember.addresses[idx1].addressType = scope.addressTypes[idx2];
				break;
			}
		}
	}

	for (var idx1 = 0; idx1 < scope.membership.primaryMember.phoneNumbers.length; idx1++) {
		for (var idx2 = 0; idx2 < scope.phoneTypes.length; idx2++) {
			if (scope.membership.primaryMember.phoneNumbers[idx1].phoneType.codeValueUID == scope.phoneTypes[idx2].codeValueUID) {
				scope.membership.primaryMember.phoneNumbers[idx1].phoneType = scope.phoneTypes[idx2];
				break;
			}
		}
	}

	for (var idx1 = 0; idx1 < scope.membership.primaryMember.emailAddresses.length; idx1++) {
		for (var idx2 = 0; idx2 < scope.emailTypes.length; idx2++) {
			if (scope.membership.primaryMember.emailAddresses[idx1].emailType.codeValueUID == scope.emailTypes[idx2].codeValueUID) {
				scope.membership.primaryMember.emailAddresses[idx1].emailType = scope.emailTypes[idx2];
				break;
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

		for (var idx2 = 0; idx2 < scope.membership.additionalMembers[idx1].addresses.length; idx2++) {
			for (var idx3 = 0; idx3 < scope.addressTypes.length; idx3++) {
				if (scope.membership.additionalMembers[idx1].addresses[idx2].addressType.codeValueUID == scope.addressTypes[idx3].codeValueUID) {
					scope.membership.additionalMembers[idx1].addresses[idx2].addressType = scope.addressTypes[idx3];
					break;
				}
			}
		}

		for (var idx2 = 0; idx2 < scope.membership.additionalMembers[idx1].phoneNumbers.length; idx2++) {
			for (var idx3 = 0; idx3 < scope.phoneTypes.length; idx3++) {
				if (scope.membership.additionalMembers[idx1].phoneNumbers[idx2].phoneType.codeValueUID == scope.phoneTypes[idx3].codeValueUID) {
					scope.membership.additionalMembers[idx1].phoneNumbers[idx2].phoneType = scope.phoneTypes[idx3];
					break;
				}
			}
		}

		for (var idx2 = 0; idx2 < scope.membership.additionalMembers[idx1].emailAddresses.length; idx2++) {
			for (var idx3 = 0; idx3 < scope.emailTypes.length; idx3++) {
				if (scope.membership.additionalMembers[idx1].emailAddresses[idx2].emailType.codeValueUID == scope.emailTypes[idx3].codeValueUID) {
					scope.membership.additionalMembers[idx1].emailAddresses[idx2].emailType = scope.emailTypes[idx3];
					break;
				}
			}
		}
	}
}

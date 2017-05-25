/**
 * (c) 2016 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for the Member pages.
 *
 * Author:  Craig Gaskill
 */

(function(window, angular) {
  'use strict';

  angular.module('swkroaApp').controller('MemberController', MemberController);

  MemberController.$inject = ['$http', 'CodeSetService', 'ContactService', 'MembershipService'];

  function MemberController($http, codeSetService, contactService, membershipService) {
    var vm = this;

    var membershipId = $('#membershipUID').val();
    var including    = ['LOAD_MEMBERS', 'LOAD_CONTACTS', 'LOAD_COUNTIES'];

    vm.view = "view";
    vm.fullyLoaded = false;
    vm.isPrimaryMember = ('true' === $('#isPrimaryMember').val());

    $('#updatedMessage').hide();

    /********************************************
     * Define binding methods
     ********************************************/

    vm.editMembership = editMembership;
    vm.addSpouse = addSpouse;
    vm.removeSpouse = removeSpouse;
    vm.addCounty = addCounty;
    vm.removeCounty = removeCounty;
    vm.validateVotingCounty = validateVotingCounty;
    vm.addMember = addMember;
    vm.removeMember = removeMember;
    vm.generateOwnerId = generateOwnerId;
    vm.hasChanges = hasChanges;
    vm.cancelChanges = cancelChanges;
    vm.saveMember = saveMember;

    vm.getAddressTypeDisplay = getAddressTypeDisplay;
    vm.getPhoneTypeDisplay = getPhoneTypeDisplay;
    vm.getEmailTypeDisplay = getEmailTypeDisplay;

    vm.addAddress = addAddress;
    vm.ensurePrimaryAddress = ensurePrimaryAddress;
    vm.removeAddress = removeAddress;

    vm.addPhone = addPhone;
    vm.ensurePrimaryPhone = ensurePrimaryPhone;
    vm.removePhone = removePhone;

    vm.addEmail = addEmail;
    vm.ensurePrimaryEmail = ensurePrimaryEmail;
    vm.removeEmail = removeEmail;

    activate();

    /********************************************
     * Implement Methods
     ********************************************/

    function editMembership() {
      if (!vm.fullyLoaded) {
        loadAll(vm, $http);
      }

      vm.original = angular.copy(vm.membership);
      vm.view     = "edit";
    }

    function addSpouse() {
      var spouseMember = null;
      for (var idx = 0; idx < vm.memberTypes.length; idx++) {
        if (vm.memberTypes[idx].memberTypeMeaning === 'SPOUSE') {
          spouseMember = vm.memberTypes[idx];
          break;
        }
      }

      vm.membership.spouse = {
        active: true,
        memberType: spouseMember
      };
    }

    function removeSpouse() {
      if (vm.membership.spouse.memberUID > 0) {
        vm.membership.spouse.active = false;
      } else {
        vm.membership.spouse = null;
      }
    }

    function addCounty() {
      vm.membership.membershipCounties.push({
        netMineralAcres: 0,
        surfaceAcres: 0,
        active: true
      });
    }

    function removeCounty(county) {
      if (county.membershipCountyUID > 0) {
        county.active = false;
      } else {
        var idx = vm.membership.membershipCounties.indexOf(county);
        vm.membership.membershipCounties.splice(idx, 1);
      }
    }

    function validateVotingCounty(membership, membershipCounty) {
      if (membershipCounty.votingCounty) {
        angular.forEach(membership.membershipCounties, function(cnty) {
          cnty.votingCounty = false;
        });

        membershipCounty.votingCounty = true;
      } else {
        membershipCounty.votingCounty = false;
      }
    }

    function addMember() {
      var familyMember = null;
      for (var idx = 0; idx < vm.memberTypes.length; idx++) {
        if (vm.memberTypes[idx].memberTypeMeaning === 'FAMILY_MEMBER') {
          familyMember = vm.memberTypes[idx];
          break;
        }
      }

      vm.membership.members.push({
        active: true,
        memberType: familyMember
      });
    }

    function removeMember(member) {
      if (member.memberUID > 0) {
        member.active = false;
      } else {
        var idx = vm.membership.members.indexOf(member);
        vm.membership.members.splice(idx, 1);
      }
    }

    function generateOwnerId(member) {
      var firstName  = member.person.firstName;
      var lastName   = member.person.lastName;
      var ownerIdent = member.ownerIdent;

      if (firstName && firstName.length > 2 &&
        lastName && lastName.length > 2 &&
        (!ownerIdent || ownerIdent.length === 0)) {

        membershipService.generateOwnerId(firstName, lastName).then(function(data) {
          member.ownerIdent = data;
        });
      }
    }

    function hasChanges() {
      return (vm.original && !angular.equals(vm.membership, vm.original));
    }

    function cancelChanges() {
      vm.membership = angular.copy(vm.original);
      vm.original = null;
      vm.view = "view";
    }

    function saveMember() {
      membershipService.saveMembership(vm.membership).then(function(response) {
        membershipService.getMembership(membershipId, including).then(function(response) {
          if (responseSuccessful(response)) {
            vm.membership = response.data;
          }
        });

        vm.view = "view";
        vm.original = null;

        $('#updatedMessage').show();
      });
    }

    function getAddressTypeDisplay(address) {
      return contactService.getAddressTypeDisplay(address, vm.addressTypes);
    }

    function getPhoneTypeDisplay(phone) {
      return contactService.getPhoneTypeDisplay(phone, vm.phoneTypes);
    }

    function getEmailTypeDisplay(email) {
      return contactService.getEmailTypeDisplay(email, vm.emailTypes);
    }

    function activate() {
      codeSetService.getCodeValuesForCodeSet('ADDRESS_TYPE').then(function(response) {
        if (responseSuccessful(response)) {
          vm.addressTypes = response.data;
        }
      });

      codeSetService.getCodeValuesForCodeSet('PHONE_TYPE').then(function(response) {
        if (responseSuccessful(response)) {
          vm.phoneTypes = response.data;
        }
      });

      codeSetService.getCodeValuesForCodeSet('EMAIL_TYPE').then(function(response) {
        if (responseSuccessful(response)) {
          vm.emailTypes = response.data;
        }
      });

      membershipService.getMembership(membershipId, including).then(function(response) {
        if (responseSuccessful(response)) {
          vm.membership = response.data;
        }
      });
    }

    function addAddress(member) {
      contactService.addAddress(member);
    }

    function ensurePrimaryAddress(member, address) {
      contactService.ensurePrimaryAddress(member, address)
    }

    function removeAddress(member, address) {
      contactService.removeAddress(member, address)
    }

    function addPhone(member) {
      contactService.addPhone(member)
    }

    function ensurePrimaryPhone(member, phone) {
      contactService.ensurePrimaryPhone(member, phone);
    }

    function removePhone(member, phone) {
      contactService.removePhone(member, phone)
    }

    function addEmail(member) {
      contactService.addEmail(member);
    }

    function ensurePrimaryEmail(member, email) {
      contactService.ensurePrimaryEmail(member, email)
    }

    function removeEmail(member, email) {
      contactService.removeEmail(member, email)
    }

    function loadAll() {
      if (vm.fullyLoaded) {
        return
      }

      contactService.getAllStates().then(function(response) {
        if (responseSuccessful(response)) {
          vm.states = response.data;
        }
      });

      contactService.getCountries().then(function(response) {
        if (responseSuccessful(response)) {
          vm.countries = response.data;
        }
      });

      codeSetService.getCodeValuesForCodeSet('TITLE').success(function(data) {
        vm.titles = data;
      });

      $http.get('/api/membertypes').success(function(data) {
        vm.memberTypes = data;
      });

      $http.get('/api/counties/').success(function(data) {
        vm.counties = data;
      });

      vm.fullyLoaded = true
    }

  }
})(window, window.angular);

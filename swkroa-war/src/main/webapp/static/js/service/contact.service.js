/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Defines the service for Memberships.
 *
 * Author: Craig Gaskill
 */
(function(window, angular) {
  'use strict';

  angular.module('swkroaApp').service('ContactService', ContactService);

  ContactService.$inject = ['$http'];

  function ContactService($http) {
    var vm = this;
    var rootUrl = "/api/countries";

    vm.getCountries = getCountries;
    vm.getAllStates = getAllStates;
    vm.getStates = getStates;

    vm.addAddress = addAddress;
    vm.removeAddress = removeAddress;
    vm.ensurePrimaryAddress = ensurePrimaryAddress;
    vm.getAddressTypeDisplay = getAddressTypeDisplay;

    vm.addPhone = addPhone;
    vm.removePhone = removePhone;
    vm.ensurePrimaryPhone = ensurePrimaryPhone;
    vm.getPhoneTypeDisplay = getPhoneTypeDisplay;

    vm.addEmail = addEmail;
    vm.removeEmail = removeEmail;
    vm.ensurePrimaryEmail = ensurePrimaryEmail;
    vm.getEmailTypeDisplay = getEmailTypeDisplay;

    /********************************************
     * Implement Methods
     ********************************************/

    function getCountries() {
      return $http.get(rootUrl);
    }

    function getAllStates() {
      return $http.get(rootUrl + "/states");
    }

    function getStates(countryCode) {
      return $http.get(rootUrl + "/" + countryCode + "/states");
    }

    function addAddress(entity) {
      if (!entity.addresses) {
        entity.addresses = [];
      }

      entity.addresses.push({
        country: 'US',
        active: true
      });
    }

    function removeAddress(entity, address) {
      if (address.addressUID > 0) {
        address.active = false;
      } else {
        var idx = entity.addresses.indexOf(address);
        entity.addresses.splice(idx, 1);
      }
    }

    function ensurePrimaryAddress(entity, address) {
      if (address.primary) {
        var pos = entity.addresses.indexOf(address);

        for (var idx = 0; idx < entity.addresses.length; idx++) {
          if (entity.addresses[idx].primary && pos !== idx) {
            entity.addresses[idx].primary = false;
          }
        }
      }
    }

    function addPhone(entity) {
      if (!entity.phoneNumbers) {
        entity.phoneNumbers = [];
      }

      entity.phoneNumbers.push({
        active: true
      });
    }

    function removePhone(entity, phone) {
      if (phone.phoneUID > 0) {
        phone.active = false;
      } else {
        var idx = entity.phoneNumbers.indexOf(phone);
        entity.phoneNumbers.splice(idx, 1);
      }
    }

    function ensurePrimaryPhone(entity, phone) {
      if (phone.primary) {
        var pos = entity.phoneNumbers.indexOf(phone);

        for (var idx = 0; idx < entity.phoneNumbers.length; idx++) {
          if (entity.phoneNumbers[idx].primary && pos !== idx) {
            entity.phoneNumbers[idx].primary = false;
          }
        }
      }
    }

    function addEmail(entity) {
      if (!entity.emailAddresses) {
        entity.emailAddresses = [];
      }
      entity.emailAddresses.push({
        active: true
      });
    }

    function removeEmail(entity, email) {
      if (email.emailAddressUID > 0) {
        email.active = false;
      } else {
        var idx = entity.emailAddresses.indexOf(email);
        entity.emailAddresses.splice(idx, 1);
      }
    }

    function ensurePrimaryEmail(entity, email) {
      if (email.primary) {
        var pos = entity.emailAddresses.indexOf(email);

        for (var idx = 0; idx < entity.emailAddresses.length; idx++) {
          if (entity.emailAddresses[idx].primary && pos !== idx) {
            entity.emailAddresses[idx].primary = false;
          }
        }
      }
    }

    function getAddressTypeDisplay(address, types) {
      for (var idx1 = 0; idx1 < types.length; idx1++) {
        if (address.addressTypeCD === types[idx1].codeValueUID) {
          return types[idx1].display;
        }
      }
    }

    function getPhoneTypeDisplay(phone, types) {
      for (var idx1 = 0; idx1 < types.length; idx1++) {
        if (phone.phoneTypeCD === types[idx1].codeValueUID) {
          return types[idx1].display;
        }
      }
    }

    function getEmailTypeDisplay(email, types) {
      for (var idx1 = 0; idx1 < types.length; idx1++) {
        if (email.emailTypeCD === types[idx1].codeValueUID) {
          return types[idx1].display;
        }
      }
    }
  }

})(window, window.angular);

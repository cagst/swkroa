/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for the Maintain User pages.
 *
 * Author:  Craig Gaskill
 */
(function(window, angular, $) {
  'use strict';

  angular.module('swkroaApp').controller('UserController', UserController);

  UserController.$inject = ['$http', 'CodeSetService', 'ContactService'];

  function UserController($http, codeSetService, contactService) {
    var vm = this;

    vm.view = 'home';
    vm.successMessage = null;
    vm.errorMessage = null;

    vm.isOpen = {
      expireDate: false
    };

    activate();

    /********************************************
     * Define binding methods
     ********************************************/

    vm.openExpireDate = openExpireDate;
    vm.getUser = getUser;
    vm.unlockUser = unlockUser;
    vm.disableUser = disableUser;
    vm.enableUser = enableUser;
    vm.resetUserPassword = resetUserPassword;
    vm.editUser = editUser;
    vm.newUser = newUser;

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

    vm.hasChanges = hasChanges;
    vm.cancelChanges = cancelChanges;
    vm.doesUsernameExist = doesUsernameExist;
    vm.validatePasswordFields = validatePasswordFields;
    vm.saveUser = saveUser;

    /********************************************
     * Implement Methods
     ********************************************/

    function activate() {
      $http.get('/api/users').then(function(response) {
        if (responseSuccessful(response)) {
          vm.users = response.data;
          $("#successMessage").hide();
        }
      });

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

      codeSetService.getCodeValuesForCodeSet('TITLE').then(function(response) {
        if (responseSuccessful(response)) {
          vm.titles = response.data;
        }
      });

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
    }

    function openExpireDate($event) {
      $event.preventDefault();
      $event.stopPropagation();

      vm.isOpen.expireDate = true;
    }

    function getUser(user) {
      var url = "/api/users/" + user.userUID;

      $http.get(url).then(function(response) {
        if (responseSuccessful(response)) {
          var idx = vm.users.indexOf(user);
          vm.users[idx] = response.data;
          vm.selectedUser = response.data;
        }
      });
    }

    function unlockUser() {
      var url = "/api/users/" + vm.selectedUser.userUID + "?actions=unlock";

      $http.put(url).then(function(response) {
        if (responseSuccessful(response)) {
          var idx = vm.users.indexOf(vm.selectedUser);
          vm.users[idx] = response.data;
          vm.selectedUser = response.data;
        }
      });
    }

    function disableUser() {
      var url = "/api/users/" + vm.selectedUser.userUID + "?action=disable";

      $http.put(url).then(function(response) {
        if (responseSuccessful(response)) {
          var idx = vm.users.indexOf(vm.selectedUser);
          vm.users[idx] = response.data;
          vm.selectedUser = response.data;
        }
      });
    }

    function enableUser() {
      var url = "/api/users/" + vm.selectedUser.userUID + "?action=enable";

      $http.put(url).then(function(response) {
        if (responseSuccessful(response)) {
          var idx = vm.users.indexOf(vm.selectedUser);
          vm.users[idx] = response.data;
          vm.selectedUser = response.data;
        }
      });
    }

    function resetUserPassword() {
      var url = "/api/users/" + vm.selectedUser.userUID + "?action=resetpwd";

      $http.put(url).then(function(response) {
        if (responseSuccessful(response)) {
          var idx = vm.users.indexOf(vm.selectedUser);
          vm.users[idx] = response.data;
          vm.selectedUser = response.data;

          $('#passwordReset').modal('show');
        }
      });
    }

    function editUser() {
      vm.original = angular.copy(vm.selectedUser);
      vm.view = 'edit';
    }

    function newUser() {
      vm.selectedUser = {
        userUID: 0,
        passwordTemporary: true,
        active: true
      };

      vm.view = 'add';
      vm.original = angular.copy(vm.selectedUser);
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

    function hasChanges() {
      return (vm.original && !angular.equals(vm.selectedUser, vm.original));
    }

    function cancelChanges() {
      vm.selectedUser = angular.copy(vm.original);
      vm.original = null;
      vm.view = 'home';
    }

    function doesUsernameExist() {
      if (vm.selectedUser.userUID === 0 && vm.selectedUser.username && vm.selectedUser.username.length > 0) {
        var url = "/api/users/" + vm.selectedUser.username + "/exists";

        $http.get(url).then(function(response) {
          if (responseSuccessful(response)) {
            vm.usernameExists = response.data;
          }
        });
      }
    }

    function validatePasswordFields() {
      vm.passwordError = null;

      if (vm.selectedUser.password && vm.selectedUser.password.length > 0 && vm.confirmPassword && vm.confirmPassword.length > 0) {
        if (vm.selectedUser.password !== vm.confirmPassword) {
          vm.passwordError = "The confirmation password does not match the password!";
        }
      }
    }

    function saveUser() {
      $http.post('/api/users', vm.selectedUser).
        success(function(data, status) {
          if (status === 201) {
            vm.users.push(data);
            vm.selectedUser = data;
            vm.successMessage = "User " + data.fullName + " was created successfully!";
          } else {
            var idx = vm.users.indexOf(vm.selectedUser);

            vm.users[idx] = data;
            vm.selectedUser = data;
            vm.successMessage = "User " + data.fullName + " was updated successfully!";
          }

          vm.view = 'home';
          vm.original = null;
        }).
        error(function(data, status) {
          switch (status) {
            case 400: // bad request
              vm.errorMessage = data.message;
              break;
            case 409: // conflict
              vm.errorMessage = "User " + data.fullName + " has been updated by another user!";
              break;
            default:
              vm.errorMessage = "Unknown Exception, unable to save user!";
              break;
          }
          $('#errorMessage').show();
        });
    }

  }

})(window, window.angular, window.jQuery);

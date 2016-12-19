/**
 * (c) 2016 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for Registering existing memberships.
 *
 * Author:  Craig Gaskill
 */
(function(window, angular) {
  'use strict';

  angular.module('swkroaApp').controller('RegistrationController', RegistrationController);

  RegistrationController.$inject = ['$http', 'CodeSetService'];

  function RegistrationController($http, codeSetService) {
    var vm = this;
    var rootUrl = "/api/register";

    vm.errorText = "";
    vm.step = "IDENTIFY";

    vm.registerUser = {
      ownerId: "",
      firstName: "",
      lastName: "",
      phoneNumber: "",
      zipCode: "",
      emailAddress: "",
      username: "",
      password: "",
      confirm: "",
      question1: "",
      answer1: "",
      question2: "",
      answer2: "",
      question3: "",
      answer3: ""
    };

    vm.enableVerify = enableVerify;
    vm.enableComplete = enableComplete;
    vm.registerIdentification = registerIdentification;
    vm.registerVerify = registerVerify;
    vm.registerComplete = registerComplete;

    /********************************************
     * Define binding methods
     ********************************************/

    function enableVerify() {
      var cnt = (vm.registerUser.firstName.length > 0 ? 1 : 0);
      cnt += (vm.registerUser.lastName.length > 0 ? 1 : 0);
      cnt += (vm.registerUser.phoneNumber.length > 0 ? 1 : 0);
      cnt += (vm.registerUser.zipCode.length > 0 ? 1 : 0);

      return (cnt > 1);
    }

    function enableComplete() {
      var cnt = (vm.registerUser.emailAddress.length > 0 ? 1 : 0);
      cnt += (vm.registerUser.username.length > 0 ? 1 : 0);
      cnt += (vm.registerUser.password.length > 0 ? 1 : 0);
      cnt += (vm.registerUser.confirm.length > 0 ? 1 : 0);

      if (vm.registerUser.password === vm.registerUser.confirm && vm.registerUser.password.length > 5) {
        cnt++;
      }

      cnt += (vm.registerUser.question1 > 0 ? 1 : 0);
      cnt += (vm.registerUser.answer1.length > 0 ? 1 : 0);
      cnt += (vm.registerUser.question2 > 0 ? 1 : 0);
      cnt += (vm.registerUser.answer2.length > 0 ? 1 : 0);
      cnt += (vm.registerUser.question3 > 0 ? 1 : 0);
      cnt += (vm.registerUser.answer3.length > 0 ? 1 : 0);

      return (cnt == 11);
    }

    function registerIdentification() {
      var url = rootUrl + "/identification/" + vm.registerUser.ownerId;

      $http.get(url)
        .then(function(response) {
          if (responseSuccessful(response)) {
            vm.errorText = "";
            vm.step = 'VERIFY';
          } else {
            vm.errorText = response.data;
          }
        });
    }

    function registerVerify() {
      $http({
        method: 'POST',
        url: rootUrl + '/verification/' + vm.registerUser.ownerId,
        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
        data: $.param({
          'firstName': vm.registerUser.firstName,
          'lastName': vm.registerUser.lastName,
          'phoneNumber': vm.registerUser.phoneNumber,
          'zipCode': vm.registerUser.zipCode})
      }).then(function(response) {
        if (responseSuccessful(response)) {
          codeSetService.getCodeValuesForCodeSet('SECURITY_QUESTION').success(function(data) {
            vm.questions = data;
          });

          vm.errorText = "";
          vm.step = "COMPLETE";
          vm.registerUser.emailAddress = response.data;
        } else {
          vm.errorText = response.data;
        }
      });
    }

    function registerComplete() {
      $http({
        method: 'POST',
        url: rootUrl + '/completion/' + vm.registerUser.ownerId,
        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
        data: $.param({
          'firstName': vm.registerUser.firstName,
          'lastName': vm.registerUser.lastName,
          'phoneNumber': vm.registerUser.phoneNumber,
          'zipCode': vm.registerUser.zipCode,
          'emailAddress': vm.registerUser.emailAddress,
          'username': vm.registerUser.username,
          'password': vm.registerUser.password,
          'confirm': vm.registerUser.confirm,
          'question1': vm.registerUser.question1,
          'answer1' : vm.registerUser.answer1,
          'question2': vm.registerUser.question2,
          'answer2' : vm.registerUser.answer2,
          'question3': vm.registerUser.question3,
          'answer3' : vm.registerUser.answer3
        })
      }).then(function (response) {
        if (responseSuccessful(response)) {
          vm.errorText = "";
          vm.step = "FINISH";
        } else {
          vm.errorText = response.data;
        }
      });
    }

  }
})(window, window.angular);

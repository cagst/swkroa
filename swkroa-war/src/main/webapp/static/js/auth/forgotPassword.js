/**
 * (c) 2016 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for Forgot Password.
 *
 * Author:  Craig Gaskill
 */
(function(window, angular) {
  'use strict';

  angular.module('swkroaApp').controller('ForgotPasswordController', ForgotPasswordController);

  ForgotPasswordController.$inject = ['$http'];

  function ForgotPasswordController($http) {
    var vm = this;
    var rootUrl = "/api/forgotPassword";

    vm.errorText = "";
    vm.step = "IDENTIFY";

    vm.forgotPassword = {
      username: "",
      ownerId: ""
    };

    /********************************************
     * Define binding methods
     ********************************************/

    vm.registerIdentification = registerIdentification;
    vm.enableVerify = enableVerify;

    /********************************************
     * Implement binding methods
     ********************************************/

    function registerIdentification() {
      var url = rootUrl + "/identification/" + vm.forgotPassword.username;

      $http({
        method: 'GET',
        url: rootUrl + "/identification/" + vm.forgotPassword.username,
        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8', 'Accept': 'text/plain'}
      }).then(function (response) {
        if (responseSuccessful(response)) {
          vm.errorText = "";
          vm.step = 'VERIFY';
        } else {
          vm.errorText = response.data;
        }
      });
    }

    function enableVerify() {
      var cnt = (vm.forgotPassword.username.length > 0 ? 1 : 0);
      cnt += (vm.forgotPassword.ownerId.length > 0 ? 1 : 0);

      return (cnt > 1);
    }
  }
})(window, window.angular);

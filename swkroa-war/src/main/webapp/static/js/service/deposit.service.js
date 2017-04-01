/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Deposit Service.
 *
 * Author:  Craig Gaskill
 */
(function(window, angular) {
  'use strict';

  angular.module('swkroaApp').service('DepositService', DepositService);

  DepositService.$inject = ['$http'];

  function DepositService($http) {
    const vm = this;
    const rootUrl = "/api/deposits";

    vm.getDeposits = getDeposits;
    vm.getDeposit = getDeposit;
    vm.saveDeposit = saveDeposit;

    /********************************************
     * Implement Methods
     ********************************************/

    function getDeposits() {
      return $http.get(rootUrl);
    }

    function getDeposit(depositUID) {
      return $http.get(rootUrl + '/' + depositUID);
    }

    function saveDeposit(deposit) {
      return $http.post(rootUrl, deposit);
    }
  }
})(window, window.angular);

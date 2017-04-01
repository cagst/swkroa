/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Defines a service for retrieving and persisting Code Values.
 *
 * Author: Craig Gaskill
 */
(function(window, angular) {
  'use strict';

  angular.module('swkroaApp').service('CodeSetService', CodeSetService);

  CodeSetService.$inject = ['$http'];

  function CodeSetService($http) {
    const vm = this;
    const rootUrl = "/api/codesets";

    vm.getCodeSets = getCodeSets;
    vm.getCodeValuesForCodeSet = getCodeValuesForCodeSet;
    vm.saveCodeSet = saveCodeSet;
    vm.saveCodeValue = saveCodeValue;

    /********************************************
     * Implement Methods
     ********************************************/

    function getCodeSets() {
      return $http.get(rootUrl);
    }

    function getCodeValuesForCodeSet(codeSetMeaning) {
      return $http.get(rootUrl + "/" + codeSetMeaning);
    }

    function saveCodeSet(codeSet) {
      return $http.post(rootUrl + "/", codeSet);
    }

    function saveCodeValue(codeSetMeaning, codeValue) {
      return $http.post(rootUrl + "/" + codeSetMeaning, codeValue);
    }
  }

})(window, window.angular);

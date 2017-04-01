/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Defines a service for retrieving and persisting Member Types.
 *
 * Author: Craig Gaskill
 */
(function(window, angular) {
  'use strict';

  angular.module('swkroaApp').service('MemberTypeService', MemberTypeService);

  MemberTypeService.$inject = ['$http'];

  function MemberTypeService($http) {
    const vm = this;
    const rootUrl = "/api/membertypes";

    vm.getMemberTypes = getMemberTypes;
    vm.getAllMemberTypesByMemberTypeId = getAllMemberTypesByMemberTypeId;

    /********************************************
     * Implement Methods
     ********************************************/

    function getMemberTypes() {
      return $http.get(rootUrl);
    }

    function getAllMemberTypesByMemberTypeId(memberTypeId) {
      return $http.get(rootUrl + "/" + memberTypeId);
    }
  }

})(window, window.angular);

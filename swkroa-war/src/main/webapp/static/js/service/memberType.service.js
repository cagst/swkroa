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
    this.getMemberTypes = function() {
      var promise = $http.get('/api/membertypes');

      promise.success = function(fn) {
        promise.then(function(response) {
          if (responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      promise.error = function(fn) {
        promise.then(function(response) {
          if (!responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      return promise;
    };

    this.getAllMemberTypesByMemberTypeId = function(memberTypeId) {
      var promise = $http.get('/api/membertypes/' + memberTypeId);

      promise.success = function(fn) {
        promise.then(function(response) {
          if (responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      promise.error = function(fn) {
        promise.then(function(response) {
          if (!responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      return promise;
    };

  }

})(window, window.angular);

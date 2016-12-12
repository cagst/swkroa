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
    this.getCodeSets = function() {
      var promise = $http.get('/api/codesets');

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

    this.getCodeValuesForCodeSet = function(codeSetMeaning) {
      var promise = $http.get('/api/codesets/' + codeSetMeaning);

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

    this.saveCodeSet = function(codeSet) {
      var promise = $http.post('/api/codesets', codeset);

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

    this.saveCodeValue = function(codeSetMeaning, codeValue) {
      var promise = $http.post('/api/codesets/' + codeSetMeaning, codeValue);

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

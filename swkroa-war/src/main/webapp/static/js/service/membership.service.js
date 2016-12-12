/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Defines a service for retrieving and persisting a Membership.
 *
 * Author: Craig Gaskill
 */
(function(window, angular) {
  'use strict';

  angular.module('swkroaApp').service('MembershipService', MembershipService);

  MembershipService.$inject = ['$http'];

  function MembershipService($http) {
    var vm = this;
    var rootUrl = "/api/memberships";

    vm.getMembership = getMembership;
    vm.getMemberships = getMemberships;
    vm.getDelinquentMemberships = getDelinquentMemberships;
    vm.getMembershipsDueInXDays = getMembershipsDueInXDays;
    vm.saveMembership = saveMembership;
    vm.generateOwnerId = generateOwnerId;
    vm.closeMemberships = closeMemberships;
    vm.renewMemberships = renewMemberships;

    /********************************************
     * Implement Methods
     ********************************************/

    function getMembership(membershipUID, including) {
      var url = rootUrl + '/' + membershipUID;

      if (Object.prototype.toString.call(including) === '[object Array]') {
        url = url + "?including=" + including.toString();
      }

      var promise = $http.get(url);

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
    }

    function getMemberships(query, status, balance) {
      var params = "";

      if (query && query.length > 0) {
        params = "?q=" + query;
      }

      if (status && status.length > 0) {
        if (params.length == 0) {
          params = "?";
        } else {
          params = params + "&";
        }

        params = params + "status=" + status;
      }

      if (balance && balance.length > 0) {
        if (params.length == 0) {
          params = "?";
        } else {
          params = params + "&";
        }

        params = params + "balance=" + balance;
      }

      var promise = $http.get(rootUrl + params);

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
    }

    function getDelinquentMemberships() {
      var promise = $http.get(rootUrl + "?balance=DELINQUENT");

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
    }

    function getMembershipsDueInXDays(days) {
      var promise = $http.get(rootUrl + "?dueInDays=" + days);

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
    }

    function saveMembership(membership) {
      var promise = $http.post(rootUrl, membership);

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
    }

    function generateOwnerId(firstName, lastName) {
      var promise = $http.get(rootUrl + '/ownerId/' + firstName + "/" + lastName);

      promise.success = function (fn) {
        promise.then(function (response) {
          if (responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      promise.error = function (fn) {
        promise.then(function (response) {
          if (!responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };
    }

    function closeMemberships(membershipIdsArg, closeReasonArg, closeTextArg) {
      var closeReasonText = "";
      if (closeTextArg) {
        closeReasonText = closeTextArg;
      }

      var data = {
        membershipIds: membershipIdsArg,
        closeReason: closeReasonArg,
        closeText: closeReasonText
      };

      var promise = $http.post(rootUrl + '/close', JSON.stringify(data));

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
    }

    function renewMemberships(membershipIdsArg, transDateArg, transDescArg, transMemoArg) {
      var transDesc = "";
      var transMemo = "";

      if (transDescArg) {
        transDesc = transDescArg;
      }

      if (transMemoArg) {
        transMemo = transMemoArg;
      }

      var data = {
        membershipIds: membershipIdsArg,
        transactionDate: transDateArg,
        transactionDescription: transDesc,
        transactionMemo: transMemo
      };

      var promise = $http.post(rootUrl + '/renew', JSON.stringify(data));

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
    }
  }

})(window, window.angular);

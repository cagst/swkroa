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

      return $http.get(url);
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

      return $http.get(rootUrl + params);
    }

    function getDelinquentMemberships() {
      return $http.get(rootUrl + "?balance=DELINQUENT");
    }

    function getMembershipsDueInXDays(days) {
      return $http.get(rootUrl + "?dueInDays=" + days);
    }

    function saveMembership(membership) {
      return $http.post(rootUrl, membership);
    }

    function generateOwnerId(firstName, lastName) {
      return $http.get(rootUrl + '/ownerId/' + firstName + "/" + lastName);
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

      return $http.post(rootUrl + '/close', JSON.stringify(data));
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

      return $http.post(rootUrl + '/renew', JSON.stringify(data));
    }
  }

})(window, window.angular);

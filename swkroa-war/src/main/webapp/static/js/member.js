/**
 * (c) 2016 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for the Member pages.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.controller('memberController',
                     ['$scope',
                      '$http',
                      'codesetService',
                      'contactService',
                      'membershipService',
                      'transactionService',
                      '$filter',
  function ($scope, $http, codesetService, contactService, membershipService, transactionService, $filter) {
    var membershipId = $('#membershipUID').val();

    membershipService.getMembership(membershipId).then(function(response) {
      if (responseSuccessful(response)) {
        $scope.membership = response.data;
      }
    });

}]);

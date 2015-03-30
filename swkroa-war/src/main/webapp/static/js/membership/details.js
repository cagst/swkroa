/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Membership pages.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.controller('membershipController',
                     ['$scope',
                      '$http',
                      'codesetService',
                      'contactService',
                      'membershipService',
                      'transactionService',
                      '$filter',
    function($scope, $http, codesetService, contactService, membershipService, transactionService, $filter, currencyFilter)
  {

  $scope.getMembership = function() {
    var membershipUID = $('#membershipUID').val();

    membershipService.getMembership(membershipUID).then(function(response) {
      if (response.status == 200) {
        $scope.membership = response.data;
      }
    });
  };

  $scope.setPage = function(page) {
    $scope.view = page;
  };

  $scope.getMembership();
  $scope.setPage("MEMBERSHIP");

}]);

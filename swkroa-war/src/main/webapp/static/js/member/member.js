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
  function ($scope, $http, codesetService, contactService, membershipService) {
    $scope.contactService = contactService;

    codesetService.getCodeValuesForCodeSet('ADDRESS_TYPE').success(function(data) {
      $scope.addressTypes = data;
    });

    codesetService.getCodeValuesForCodeSet('PHONE_TYPE').success(function(data) {
      $scope.phoneTypes = data;
    });

    codesetService.getCodeValuesForCodeSet('EMAIL_TYPE').success(function(data) {
      $scope.emailTypes = data;
    });

    var membershipId = $('#membershipUID').val();

    var including = ['LOAD_MEMBERS', 'LOAD_CONTACTS'];

    membershipService.getMembership(membershipId, including).then(function(response) {
      if (responseSuccessful(response)) {
        $scope.membership = response.data;
      }
    });

}]);

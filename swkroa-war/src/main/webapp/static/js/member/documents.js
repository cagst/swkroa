/**
 * (c) 2016 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for the Member pages.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.controller('documentController',
                     ['$scope',
                      '$http',
                      'codesetService',
                      'contactService',
                      'membershipService',
  function ($scope, $http, codesetService, contactService, membershipService) {
    var membershipId = $('#membershipUID').val();

    var including = ['LOAD_DOCUMENTS'];

    membershipService.getMembership(membershipId, including).then(function(response) {
      if (responseSuccessful(response)) {
        $scope.documents = response.data.documents;
      }
    });

}]);

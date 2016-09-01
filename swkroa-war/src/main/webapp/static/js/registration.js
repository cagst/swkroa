/**
 * (c) 2016 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for Registering existing memberships.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.controller('registrationController',
                     ['$scope',
                      '$http',
                      'WizardHandler',
function($scope, $http, WizardHandler) {
  $scope.registerIdentification = function () {
    var ownerId   = $('#ownerId').val();
    var firstName = $('#firstName').val();
    var lastName  = $('#lastName').val();

    var data = {'ownerId': ownerId, 'firstName': firstName, 'lastName': lastName};

    $http.post('/api/register/identification', data)
      .success(function(data) {
        WizardHandler.wizard().next();
      })
      .error(function(data) {
        // do nothing...yet
      });
  };
}]);

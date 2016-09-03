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
  angular.element(document).ready(function () {
    $("#errorMessage").hide();
    $scope.enableIdentify();
    $scope.enableVerify();
  });

  $scope.enableIdentify = function() {
    var ownerId  = $('#ownerId').val();
    var disabled = (ownerId.length == 0);

    $('#identifyButton').prop('disabled', disabled);

    $("#errorMessage").hide();
    $('#identifyError').hide();
    $('#verifyError').hide();
  };

  $scope.enableVerify = function() {
    var firstName   = $('#firstName').val();
    var lastName    = $('#lastName').val();
    var phoneNumber = $('#phoneNumber').val();
    var zipCode     = $('#zipCode').val();

    var cnt = (firstName.length > 0 ? 1 : 0);
    cnt += (lastName.length > 0 ? 1 : 0);
    cnt += (phoneNumber.length > 0 ? 1 : 0);
    cnt += (zipCode.length > 0 ? 1 : 0);

    $('#verifyButton').prop('disabled', cnt < 2);

    $("#errorMessage").hide();
    $('#identifyError').hide();
    $('#verifyError').hide();
  };

  $scope.registerIdentification = function () {
    var url = "/api/register/identification/" + $('#ownerId').val();

    $http.get(url)
      .then(function(response) {
        if (responseSuccessful(response)) {
          $scope.member = response.data;
          $("#errorMessage").hide();
          WizardHandler.wizard().next();
        } else {
          $('#errorMessage').show();
          $('#identifyError').show();
        }
      });
  };

  $scope.registerVerify = function () {
    var firstName   = $('#firstName').val();
    var lastName    = $('#lastName').val();
    var phoneNumber = $('#phoneNumber').val();
    var zipCode     = $('#zipCode').val();

    var suppliedCnt = 0;
    var verifiedCnt = 0;

    if (firstName.length > 0) {
      suppliedCnt++;

      if (firstName.toUpperCase() === $scope.member.person.firstName.toUpperCase()) {
        verifiedCnt++;
      }
    }

    if (lastName.length > 0) {
      suppliedCnt++;

      if (lastName.toUpperCase() === $scope.member.person.lastName.toUpperCase()) {
        verifiedCnt++;
      }
    }

    if (suppliedCnt === verifiedCnt) {
      $("#errorMessage").hide();
      WizardHandler.wizard().next();
    } else {
      $('#errorMessage').show();
      $('#verifyError').show();
    }
  };
}]);

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
          $("#errorMessage").hide();
          WizardHandler.wizard().next();
        } else {
          $('#errorMessage').show();
          $('#identifyError').show();
        }
      });
  };

  $scope.registerVerify = function () {
    var ownerId     = $('#ownerId').val();

    var firstName   = $('#firstName').val();
    var lastName    = $('#lastName').val();
    var phoneNumber = $('#phoneNumber').val();
    var zipCode     = $('#zipCode').val();

//    var params = "?ownerId=" + ownerId;
    var params = "";

    if (firstName && firstName.length > 0) {
      if (params.length == 0) {
        params = "?";
      } else {
        params = params + "&";

      }
      params = params + "firstName=" + firstName;
    }

    if (lastName && lastName.length > 0) {
      if (params.length == 0) {
        params = "?";
      } else {
        params = params + "&";
      }

      params = params + "lastName=" + lastName;
    }

    if (zipCode && zipCode.length > 0) {
      if (params.length == 0) {
        params = "?";
      } else {
        params = params + "&";
      }

      params = params + "zipCode=" + zipCode;
    }

    if (phoneNumber && phoneNumber.length > 0) {
      if (params.length == 0) {
        params = "?"
      } else {
        params = params + "&";
      }

      params = params + "phoneNumber=" + phoneNumber;
    }

    $http.get("/api/register/verification/" + ownerId + params)
      .then(function(response) {
        if (responseSuccessful(response)) {
          $("#errorMessage").hide();
          WizardHandler.wizard().next();
        } else {
          $('#errorMessage').show();
          $('#verifyError').show();
        }
    });
  };
}]);

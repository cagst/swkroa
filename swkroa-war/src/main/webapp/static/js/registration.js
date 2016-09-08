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
    $scope.enableComplete();
  });

  $scope.enableIdentify = function() {
    var ownerId  = $('#ownerId').val();
    var disabled = (ownerId.length == 0);

    $('#identifyButton').prop('disabled', disabled);
    $("#errorMessage").hide();
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
  };

  $scope.enableComplete = function () {
    var emailAddress = $('#emailAddress').val();
    var username     = $('#username').val();
    var password     = $('#password').val();
    var confirm      = $('#confirm').val();

    var cnt = (emailAddress.length > 0 ? 1 : 0);
    cnt += (username.length > 0 ? 1 : 0);
    cnt += (password.length > 0 ? 1 : 0);
    cnt += (confirm.length > 0 ? 1 : 0);

    if (password === confirm) {
      cnt++;
    }

    $('#completeButton').prop('disabled', cnt != 5);
    $("#errorMessage").hide();
  };

  $scope.registerIdentification = function () {
    var url = "/api/register/identification/" + $('#ownerId').val();

    $http.get(url)
      .then(function(response) {
        if (responseSuccessful(response)) {
          WizardHandler.wizard().next();

          $("#errorMessage").hide();
          $("#firstName").focus();
        } else {
          $('#errorMessageText').text(response.data);
          $('#errorMessage').show();
        }
      });
  };

  $scope.registerVerify = function () {
    var ownerId     = $('#ownerId').val();

    var firstName   = $('#firstName').val();
    var lastName    = $('#lastName').val();
    var phoneNumber = $('#phoneNumber').val();
    var zipCode     = $('#zipCode').val();

    $http({
      method: 'POST',
      url: '/api/register/verification/' + ownerId,
      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
      data: $.param({
        'firstName': firstName,
        'lastName': lastName,
        'phoneNumber': phoneNumber,
        'zipCode': zipCode})
    }).then(function(response) {
      if (responseSuccessful(response)) {
        WizardHandler.wizard().next();

        $scope.emailAddress = response.data;

        $("#errorMessage").hide();
        $("#emailAddress").focus();
      } else {
        $('#errorMessageText').text(response.data);
        $('#errorMessage').show();
      }
    });
  };

  $scope.registerComplete = function () {
    var ownerId = $('#ownerId').val();

    var firstName   = $('#firstName').val();
    var lastName    = $('#lastName').val();
    var phoneNumber = $('#phoneNumber').val();
    var zipCode     = $('#zipCode').val();

    var emailAddress = $('#emailAddress').val();
    var username     = $('#username').val();
    var password     = $('#password').val();
    var confirm      = $('#confirm').val();

    $http({
      method: 'POST',
      url: '/api/register/completion/' + ownerId,
      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
      data: $.param({
        'firstName': firstName,
        'lastName': lastName,
        'phoneNumber': phoneNumber,
        'zipCode': zipCode,
        'emailAddress': emailAddress,
        'username': username,
        'password': password,
        'confirm': confirm
      })
    }).then(function (response) {
      if (responseSuccessful(response)) {
        $scope.emailAddress = response.data;

        $("#errorMessage").hide();
        $("#emailAddress").focus();

        WizardHandler.wizard().next();
      } else {
        $('#errorMessageText').text(response.data);
        $('#errorMessage').show();
      }
    });
  }

}]);

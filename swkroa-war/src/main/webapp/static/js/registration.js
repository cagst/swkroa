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
                      'codesetService',
function($scope, $http, codesetService) {
  $scope.errorText = "";
  $scope.step = "IDENTIFY";

  $scope.registerUser = {
    ownerId: "",
    firstName: "",
    lastName: "",
    phoneNumber: "",
    zipCode: "",
    emailAddress: "",
    username: "",
    password: "",
    confirm: "",
    question1: "",
    answer1: "",
    question2: "",
    answer2: "",
    question3: "",
    answer3: ""
  };

  $scope.enableVerify = function() {
    var cnt = ($scope.registerUser.firstName.length > 0 ? 1 : 0);
    cnt += ($scope.registerUser.lastName.length > 0 ? 1 : 0);
    cnt += ($scope.registerUser.phoneNumber.length > 0 ? 1 : 0);
    cnt += ($scope.registerUser.zipCode.length > 0 ? 1 : 0);

    return (cnt > 1);
  };

  $scope.enableComplete = function () {
    var cnt = ($scope.registerUser.emailAddress.length > 0 ? 1 : 0);
    cnt += ($scope.registerUser.username.length > 0 ? 1 : 0);
    cnt += ($scope.registerUser.password.length > 0 ? 1 : 0);
    cnt += ($scope.registerUser.confirm.length > 0 ? 1 : 0);

    if ($scope.registerUser.password === $scope.registerUser.confirm && $scope.registerUser.password.length > 5) {
      cnt++;
    }

    cnt += ($scope.registerUser.question1 > 0 ? 1 : 0);
    cnt += ($scope.registerUser.answer1.length > 0 ? 1 : 0);
    cnt += ($scope.registerUser.question2 > 0 ? 1 : 0);
    cnt += ($scope.registerUser.answer2.length > 0 ? 1 : 0);
    cnt += ($scope.registerUser.question3 > 0 ? 1 : 0);
    cnt += ($scope.registerUser.answer3.length > 0 ? 1 : 0);

    return (cnt == 11);
  };

  $scope.registerIdentification = function () {
    var url = "/api/register/identification/" + $scope.registerUser.ownerId;

    $http.get(url)
      .then(function(response) {
        if (responseSuccessful(response)) {
          $scope.errorText = "";
          $scope.step = 'VERIFY';
        } else {
          $scope.errorText = response.data;
        }
      });
  };

  $scope.registerVerify = function () {
    $http({
      method: 'POST',
      url: '/api/register/verification/' + $scope.registerUser.ownerId,
      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
      data: $.param({
        'firstName': $scope.registerUser.firstName,
        'lastName': $scope.registerUser.lastName,
        'phoneNumber': $scope.registerUser.phoneNumber,
        'zipCode': $scope.registerUser.zipCode})
    }).then(function(response) {
      if (responseSuccessful(response)) {
        codesetService.getCodeValuesForCodeSet('SECURITY_QUESTION').success(function(data) {
          $scope.questions = data;
        });

        $scope.errorText = "";
        $scope.step = "COMPLETE";
        $scope.registerUser.emailAddress = response.data;
      } else {
        $scope.errorText = response.data;
      }
    });
  };

  $scope.registerComplete = function () {
    $http({
      method: 'POST',
      url: '/api/register/completion/' + $scope.registerUser.ownerId,
      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
      data: $.param({
        'firstName': $scope.registerUser.firstName,
        'lastName': $scope.registerUser.lastName,
        'phoneNumber': $scope.registerUser.phoneNumber,
        'zipCode': $scope.registerUser.zipCode,
        'emailAddress': $scope.registerUser.emailAddress,
        'username': $scope.registerUser.username,
        'password': $scope.registerUser.password,
        'confirm': $scope.registerUser.confirm,
        'question1': $scope.registerUser.question1,
        'answer1' : $scope.registerUser.answer1,
        'question2': $scope.registerUser.question2,
        'answer2' : $scope.registerUser.answer2,
        'question3': $scope.registerUser.question3,
        'answer3' : $scope.registerUser.answer3
      })
    }).then(function (response) {
      if (responseSuccessful(response)) {
        $scope.errorText = "";
        $scope.step = "FINISH";
      } else {
        $scope.errorText = response.data;
      }
    });
  }

}]);

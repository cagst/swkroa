/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for the User Profile pages.
 *
 * Author:  Craig Gaskill
 * Version: 1.0.0
 */

swkroaApp.controller('profileController',
  ['$scope', '$http', 'contactService', '$state',
  function($scope, $http, contactService, $state) {

  $scope.contactService = contactService;

  $http.get('/api/profile').success(function(data) {
    $scope.user = data;
    $("#successMessage").hide();
  });

  contactService.getAddressTypes().then(function(data) {
    $scope.addressTypes = data;
  });

  contactService.getPhoneTypes().then(function(data) {
    $scope.phoneTypes = data;
  });

  contactService.getEmailTypes().then(function(data) {
    $scope.emailTypes = data;
  });
}]);

swkroaApp.controller('modifyUserController', ['$scope', '$http', '$state', function($scope, $http, $state) {
  var original = angular.copy($scope.share.user);

  $("#errorMessage").hide();

  $scope.states = $scope.contactService.getStates();

  $http.get('/api/codeset/TITLE/').success(function(data) {
    $scope.titles = data;
  });

  $scope.hasChanges = function(user) {
    return angular.equals(user, original);
  };

  $scope.doesUsernameExist = function() {
    if ($scope.share.user.userUID == 0 &&  $scope.share.user.username && $scope.share.user.username.length > 0) {
      var url = "/api/users/" + $scope.share.user.username + "/exists";

      $http.get(url).success(function(data) {
        $scope.usernameExists = data;
      });
    }
  };

  $scope.validatePasswordFields = function() {
    $scope.passwordError = null;

    if ($scope.share.user.password && $scope.share.user.password.length > 0 &&
        $scope.confirmPassword && $scope.confirmPassword.length > 0) {

      if ($scope.share.user.password != $scope.confirmPassword) {
        $scope.passwordError = "The confirmation password does not match the password!";
      }
    }
  };

  $scope.save = function() {
    $http.put('/api/users', $scope.share.user).
      success(function(data) {
        if ($scope.share.user.userUID == 0) {
          $scope.users.push(data);
          $scope.share.user = data;
          $scope.share.successMessage = "User " + data.fullName + " was created successfully!";
        } else {
          var idx = $scope.users.indexOf($scope.user);

          $scope.users[idx] = data;
          $scope.share.user = data;
          $scope.share.successMessage = "User " + data.fullName + " was updated successfully!";
        }

        $state.go("home");
      }).
      error(function(data, status) {
        switch (status) {
        case 400: // bad request
          $scope.errorMessage = "Username " + data.username + " is already in use!";
          break;
        case 409: // conflict
          $scope.errorMessage = "User " + data.fullName + " has been updated by another user!";
          break;
        default:
          $scope.errorMessage = "Unknown Exception, unable to save user!";
          break;
        }
        $("#errorMessage").show();
      });
  };
}]);

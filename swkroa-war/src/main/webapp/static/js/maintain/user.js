/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for the Maintain User pages.
 *
 * Author:  Craig Gaskill
 */
swkroaApp.controller('userController',
  ['$scope', '$http', 'codesetService', 'contactService',
  function($scope, $http, codesetService, contactService) {

  $scope.contactService = contactService;
  $scope.status = {
    opened: false
  };

  $scope.openExpireDate = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.status.opened = true;
  };

  $http.get('/api/users').success(function(data) {
    $scope.users = data;
    $("#successMessage").hide();
  });

  codesetService.getCodeValuesForCodeSet('ADDRESS_TYPE').success(function(data) {
    $scope.addressTypes = data;
  });

  codesetService.getCodeValuesForCodeSet('PHONE_TYPE').success(function(data) {
    $scope.phoneTypes = data;
  });

  codesetService.getCodeValuesForCodeSet('EMAIL_TYPE').success(function(data) {
    $scope.emailTypes = data;
  });

  $scope.getUser = function(user) {
    var url = "/api/users/" + user.userUID;

    $http.get(url).success(function(data) {
      var idx = $scope.users.indexOf(user);
      $scope.users[idx] = data;
      $scope.share = {
        user: data,
        successMessage: null
      }
    });
  };

  $scope.unlockUser = function() {
    var url = "/api/users/" + $scope.share.user.userUID + "?actions=unlock";

    $http.put(url).success(function(data) {
      var idx = $scope.users.indexOf($scope.share.user);
      $scope.users[idx] = data;
      $scope.share.user = data;
    });
  };

  $scope.disableUser = function() {
    var url = "/api/users/" + $scope.share.user.userUID + "?action=disable";

    $http.put(url).success(function(data) {
      var idx = $scope.users.indexOf($scope.share.user);
      $scope.users[idx] = data;
      $scope.share.user = data;
    });
  };

  $scope.enableUser = function() {
    var url = "/api/users/" + $scope.share.user.userUID + "?action=enable";

    $http.put(url).success(function(data) {
      var idx = $scope.users.indexOf($scope.share.user);
      $scope.users[idx] = data;
      $scope.share.user = data;
    });
  };

  $scope.resetUserPassword = function() {
    var url = "/api/users/" + $scope.share.user.userUID + "?action=resetpwd";

    $http.put(url).success(function(data) {
      var idx = $scope.users.indexOf($scope.share.user);
      $scope.users[idx] = data;
      $scope.share.user = data;
      $('#passwordReset').modal('show');
    });
  };

  $scope.newUser = function() {
    $scope.share = {
      user: {
        userUID: 0,
        passwordTemporary: true,
        active: true
      },
      successMessage: null
    };

    $scope.view = 'add';
  };
}]);

swkroaApp.controller('modifyUserController',
    ['$scope', '$http', 'codesetService',
    function($scope, $http, codesetService) {
  var original = angular.copy($scope.share.user);

  $("#errorMessage").hide();

  $scope.states = $scope.contactService.getStates();

  codesetService.getCodeValuesForCodeSet('TITLE').success(function(data) {
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
    $http.post('/api/users', $scope.share.user).
      success(function(data, status) {
        if (status == 201) {
          $scope.users.push(data);
          $scope.share.user = data;
          $scope.share.successMessage = "User " + data.fullName + " was created successfully!";
        } else {
          var idx = $scope.users.indexOf($scope.user);

          $scope.users[idx] = data;
          $scope.share.user = data;
          $scope.share.successMessage = "User " + data.fullName + " was updated successfully!";
        }

        $scope.view = 'listing';
      }).
      error(function(data, status) {
        switch (status) {
        case 400: // bad request
          $scope.errorMessage = data.message;
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

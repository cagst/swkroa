/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for the Maintain User pages.
 *
 * Author:  Craig Gaskill
 * Version: 1.0.0
 */

swkroaApp.config(function($stateProvider, $urlRouterProvider) {
  $urlRouterProvider.otherwise("/home");

  $stateProvider
    .state('home', {
      url: "/home",
      views: {
        '': {
          templateUrl: "/partials/maintain/user/main.html"
        },
        'list@home': {
          templateUrl: "/partials/maintain/user/list.html"
        },
        'detail@home': {
          templateUrl: "/partials/maintain/user/detail.html"
        }
      }
    })
    .state('add', {
      url: "/add",
      views: {
        '': {
          templateUrl: "/partials/maintain/user/modify.html"
        },
        'contact@add': {
          templateUrl: "/partials/maintain/user/contact.html"
        }
      }
    })
    .state('edit', {
      url: "/edit",
      views: {
        '': {
          templateUrl: "/partials/maintain/user/modify.html"
        },
        'contact@edit': {
          templateUrl: "/partials/maintain/user/contact.html"
        }
      }
    });
});

swkroaApp.controller('userController',
  ['$scope', '$http', 'contactService', '$state',
  function($scope, $http, contactService, $state) {

  $scope.contactService = contactService;

  $http.get('/api/users').success(function(data) {
    $scope.users = data;
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

    $state.go("add");
  };
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

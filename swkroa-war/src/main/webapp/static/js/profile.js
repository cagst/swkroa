/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for the User Profile pages.
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
          templateUrl: "/partials/profile_view.html"
        }
      }
    })
    .state('edit', {
      url: "/edit",
      views: {
        '': {
          templateUrl: "/partials/profile_modify.html"
        },
        'contact@edit': {
          templateUrl: "/partials/profile_contact.html"
        }
      }
    });
});

swkroaApp.controller('profileController',
  ['$scope', '$http', 'codesetService', 'contactService', '$state',
  function($scope, $http, codesetService, contactService, $state) {

  $scope.contactService = contactService;

  $http.get('/api/profile').success(function(data) {
      $scope.share = {
        user: data,
        successMessage: null
      }
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

  $scope.changePassword = function() {
    $scope.errorMessage = null;
    $http({
      method: 'POST',
      url: '/api/changepwd',
      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
      data: $.param({'oldPassword': $scope.oldPassword,
                     'newPassword': $scope.newPassword,
                     'confirmPassword': $scope.confirmPassword})
    }).success(function(data) {
      $scope.share = {
        user: data,
        successMessage: "Password was successfully changed!"
      };
      $("#passwordChangeDlg").modal('hide');
    }).error(function(data, status) {
      $scope.errorMessage = data.message;
      $("#errorMessageAlert").show();
    });
  };
}]);

swkroaApp.controller('modifyProfileController', ['$scope', '$http', 'codesetService', '$state', function($scope, $http, codesetService, $state) {
  var original = angular.copy($scope.share.user);

  $("#errorMessage").hide();

  $scope.states = $scope.contactService.getStates();

//  $http.get('/api/codeset/TITLE/').success(function(data) {
//    $scope.titles = data;
//  });

  codesetService.getCodeValuesForCodeSet('TITLE').success(function(data) {
    $scope.titles = data;
  });

  $scope.hasChanges = function(user) {
    return angular.equals(user, original);
  };

  $scope.save = function() {
    $http.post('/profile', $scope.share.user).
      success(function(data) {
        if ($scope.share.user.userUID == 0) {
          $scope.share.user = data;
          $scope.share.successMessage = "User " + data.fullName + " was created successfully!";
        } else {
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

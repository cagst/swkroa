/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 *
 * Version: 1.0.0
 */

swkroaApp.config(function($stateProvider, $urlRouterProvider) {
  $urlRouterProvider.otherwise("/home");

  $stateProvider
    .state('home', {
      url: "/home",
      views: {
        '': {
          templateUrl: "/partials/maintain/user/partial_main.html"
        },
        'list@home': {
          templateUrl: "/partials/maintain/user/partial_list.html"
        },
        'detail@home': {
          templateUrl: "/partials/maintain/user/partial_detail.html"
        }
      }
    })
    .state('add', {
      url: "/add",
      views: {
        '': {
          templateUrl: "/partials/maintain/user/partial_modify.html"
        },
        'contact@add': {
          templateUrl: "/partials/maintain/user/partial_contact.html"
        }
      }
    })
    .state('edit', {
      url: "/edit",
      views: {
        '': {
          templateUrl: "/partials/maintain/user/partial_modify.html"
        },
        'contact@edit': {
          templateUrl: "/partials/maintain/user/partial_contact.html"
        }
      }
    });
});

swkroaApp.controller('userController', ['$scope', '$http', '$state', function($scope, $http, $state) {
  $http.get('/api/users').success(function(data) {
    $scope.users = data;
    $("#successMessage").hide();
  });

  $scope.getUser = function(user) {
//    $scope.user = user;
    $scope.share = {
      user: user,
      successMessage: ""
    }
  };

  $scope.unlockUser = function() {
    var url = "/api/users/" + $scope.user.userUID + "/unlock";

    $http.put(url).success(function(data) {
      for (idx = 0; idx < $scope.users.length; idx++) {
        if ($scope.user.userUID == $scope.users[idx].userUID) {
          $scope.users[idx] = data;
          $scope.share.user = data;
          break;
        }
      }
    });
  };

  $scope.disableUser = function() {
    var url = "/api/users/" + $scope.user.userUID + "/disable";

    $http.put(url).success(function(data) {
      for (idx = 0; idx < $scope.users.length; idx++) {
        if ($scope.user.userUID == $scope.users[idx].userUID) {
          $scope.users[idx] = data;
          $scope.share.user = data;
          break;
        }
      }
    });
  };

  $scope.enableUser = function() {
    var url = "/api/users/" + $scope.user.userUID + "/enable";

    $http.put(url).success(function(data) {
      for (idx = 0; idx < $scope.users.length; idx++) {
        if ($scope.user.userUID == $scope.users[idx].userUID) {
          $scope.users[idx] = data;
          $scope.share.user = data;
          break;
        }
      }
    });
  };

  $scope.newUser = function() {
    $scope.share = {
      user: {
        userUID: 0,
        passwordTemporary: true,
        active: true
      },
      successMessage: ""
    };

    $state.go("add");
  };
}]);

swkroaApp.controller('modifyUserController', ['$scope', '$http', 'contactService', '$state', function($scope, $http, contactService, $state) {
  var original          = angular.copy($scope.user);
  $scope.contactService = contactService;

  $scope.states = contactService.getStates();

  contactService.getAddressTypes().then(function(data) {
    $scope.addressTypes = data;
  });

  contactService.getPhoneTypes().then(function(data) {
    $scope.phoneTypes = data;
  });

  contactService.getEmailTypes().then(function(data) {
    $scope.emailTypes = data;
  });

  $http.get('/api/codeset/TITLE/').success(function(data) {
    $scope.titles = data;

    syncAllItems($scope);
  });

  $scope.hasChanges = function(user) {
    return angular.equals(user, original);
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
      error(function(data, status,headers, config) {
        $scope.errorMessage = "Unknown Exception, unable to save user.";
      })
  };
}]);

var syncAllItems = function(scope) {
  if (scope.share.user.title) {
    for (var idx = 0; idx < scope.titles.length; idx++) {
      if (scope.share.user.title.codeValueUID == scope.titles[idx].codeValueUID) {
        scope.share.user.title = scope.titles[idx];
        break;
      }
    }
  }
}

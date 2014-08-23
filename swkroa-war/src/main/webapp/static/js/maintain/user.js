/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 *
 * Version: 1.0.0
 */

var maintainUserApp = angular.module('maintainUserApp', ['ui.router']);

maintainUserApp.config(function($stateProvider, $urlRouterProvider) {
  $urlRouterProvider.otherwise("/home");

  $stateProvider
    .state('home', {
      url: "/home",
      views: {
        '': {
          templateUrl: "../partials/maintain/user/partial_main.html"
        },

        'list@home': {
          templateUrl: "../partials/maintain/user/partial_list.html"
        },

        'detail@home': {
          templateUrl: "../partials/maintain/user/partial_detail.html"
        }
      }
    })
    .state('edit', {
      url: "/edit",
      templateUrl: "../partials/maintain/user/partial_modify.html"
    });
});

maintainUserApp.controller('userController', ['$scope', '$http', function($scope, $http) {
  $http.get('../api/users').success(function(data) {
    $scope.users = data;
  });

  $scope.getUser = function(user) {
    $scope.selectedUser = user;
  };

  $scope.addUser = function(user) {
    $http.post('../api/users', user).success(function(data) {
    });
  };

  $scope.editUser = function(user) {
    $http.put('../api/users', user).success(function(data) {
    });
  };

  $scope.unlockUser = function() {
    var url = "../api/users/" + $scope.selectedUser.userUID + "/unlock";

    $http.put(url).success(function(data) {
      for (idx = 0; idx < $scope.users.length; idx++) {
        if ($scope.selectedUser.userUID == $scope.users[idx].userUID) {
          $scope.users[idx] = data;
          $scope.selectedUser = data;
          break;
        }
      }
    });
  };

  $scope.disableUser = function() {
    var url = "../api/users/" + $scope.selectedUser.userUID + "/disable";

    $http.put(url).success(function(data) {
      for (idx = 0; idx < $scope.users.length; idx++) {
        if ($scope.selectedUser.userUID == $scope.users[idx].userUID) {
          $scope.users[idx] = data;
          $scope.selectedUser = data;
          break;
        }
      }
    });
  };

  $scope.enableUser = function() {
    var url = "../api/users/" + $scope.selectedUser.userUID + "/enable";

    $http.put(url).success(function(data) {
      for (idx = 0; idx < $scope.users.length; idx++) {
        if ($scope.selectedUser.userUID == $scope.users[idx].userUID) {
          $scope.users[idx] = data;
          $scope.selectedUser = data;
          break;
        }
      }
    });
  };
}]);

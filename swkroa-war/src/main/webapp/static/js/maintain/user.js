/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 *
 * Version: 1.0.0
 */

var userModule = angular.module('swkroaApp', ['restangular']);

userModule.controller('userListController', ['$scope', '$http', 'Restangular', function($scope, $http, Restangular) {
  var userService = Restangular.all('../swkroa-war/svc/users');

  $scope.users = userService.getList().$object;

  $scope.getUser = function(userUID) {
    $scope.selectedUser = userService.get(userUID).$object;
  };

  $scope.addUser = function(user) {
    $http.post('../svc/users', user).success(function(data) {
    });
  };

  $scope.editUser = function(user) {
    $http.put('../svc/users', user).success(function(data) {
    });
  };

  $scope.unlockUser = function() {
    $http.put('../svc/users/unlock', $scope.selectedUser).success(function(data) {
      for (idx = 0; idx < $scope.users.length; idx++) {
        if ($scope.selectedUser.userUID == $scope.users[idx].userUID) {
          $scope.users.slice(idx, 1);
        }
      }

      $scope.selectedUser = data;
      $scope.users.add($scope.selectedUser);
    });
  };

  $scope.removeUser = function() {
    $scope.selectedUser.active = false;
    $http.put('../svc/user', $scope.selectedUser).success(function(data) {
      $scope.selectedUser = data;
    })
    .error(function(data) {
      // if we failed to save (remove the codevalue)
      // set it back to active
      // TODO: Need to add a message
      $scope.selectedUser.active = true;
    });
    $('#confirmDeletion').modal('hide');
  };
}]);

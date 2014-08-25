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
      views: {
        '': {
          templateUrl: "../partials/maintain/user/partial_modify.html"
        },
        'contact@edit': {
          templateUrl: "../partials/maintain/user/partial_contact.html"
        }
      }
    });
});

maintainUserApp.controller('userController', ['$scope', '$http', function($scope, $http) {
  $http.get('../api/users').success(function(data) {
    $scope.users = data;
  });

  $scope.getUser = function(user) {
    $scope.selectedUser = user;
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

  $scope.addUser = function(user) {
    $http.post('../api/users', user).success(function(data) {
    });
  };

  $scope.editUser = function(user) {
    $http.put('../api/users', user).success(function(data) {
    });
  };

}]);

maintainUserApp.controller('modifyUserController', ['$scope', '$http', function($scope, $http) {
  $scope.addUser = function(user) {
    $http.post('../api/users', user).success(function(data) {
    });
  };

  $scope.editUser = function(user) {
    $http.put('../api/users', user).success(function(data) {
    });
  };

  $scope.addAddress = function(user) {
    if (!user.addresses) {
      user.addresses = new Array();
    }

    user.addresses.push({
      active: true
    });
  };

  $scope.removeAddress = function(user, address) {
    if (address.addressUID > 0) {
      address.active = false;
    } else {
      var idx = user.addresses.indexOf(address);
      user.addresses.splice(idx, 1);
    }
  };

  $scope.addPhone = function(user) {
    if (!user.phoneNumbers) {
      user.phoneNumbers = new Array();
    }

    user.phoneNumbers.push({
      active: true
    });
  };

  $scope.removePhone = function(user, phone) {
    if (phone.phoneUID > 0) {
      phone.active = false;
    } else {
      var idx = user.phoneNumbers.indexOf(phone);
      user.phoneNumbers.splice(idx, 1);
    }
  };

  $scope.addEmail = function(user) {
    if (!user.emailAddresses) {
      user.emailAddresses = new Array();
    }
    user.emailAddresses.push({
      active: true
    });
  };

  $scope.removeEmail = function(user, email) {
    if (email.emailAddressUID > 0) {
      email.active = false;
    } else {
      var idx = user.emailAddresses.indexOf(email);
      user.emailAddresses.splice(idx, 1);
    }
  };

}]);

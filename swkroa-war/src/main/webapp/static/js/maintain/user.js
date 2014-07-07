/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 *
 * Version: 1.0.0
 */

var userApp = angular.module('userApp', []);

userApp.controller('userListController', ['$scope', '$http', function($scope, $http) {
	$http.get('../svc/user').success(function(data) {
		$scope.users = data;
	});

	$scope.addUser = function(codeValue) {
		$http:post('../svc/codevalue', codeValue).success(function(data) {
		});
	};

	$scope.editUser = function(codeValue) {
		$http.put('../svc/codevalue', codeValue).success(function(data) {
		});
	};

	$scope.removeUser = function() {
		$scope.codevalue.active = false;
		$http.put('../svc/codevalue', $scope.codevalue).success(function(data) {
			$scope.codevalue = data;
		})
		.error(function(data) {
			// if we failed to save (remove the codevalue)
			// set it back to active
			// TODO: Need to add a message
			$scope.codevalue.active = true;
		});
		$('#confirmDeletion').modal('hide');
	};
}]);

/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 *
 * Version: 1.0.0
 */

var swkroaApp = angular.module('swkroaApp', [])
                       .controller('swkroaController', ['$scope', '$http', function($scope, $http) {

	$http.get('../svc/codeset/MEMBERSHIP_TYPE/').success(function(data) {
		$scope.membershipTypes = data;
	});

	$scope.getMembershipListing = function() {
		var url = '../svc/report/membershiplisting/';
		if ($scope.membershipType) {
			url = url + $scope.membershipType.meaning;
		} else {
			url = url + "ALL";
		}

		$http.get(url).success(function(data) {
			$scope.results = data;
		});
	};
}]);

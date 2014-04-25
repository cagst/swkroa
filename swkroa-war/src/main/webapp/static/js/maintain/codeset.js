/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 *
 * Version: 1.0.0
 */

var csApp = angular.module('csApp', ['xeditable']);

csApp.run(function(editableOptions) {
	editableOptions.theme = 'bs3';
});

csApp.controller('csListController', function($scope, $http) {
	$http.get('../svc/codesets').success(function(data) {
		$scope.codesets = data;
	});

	$scope.getCodeValues = function(codeSet) {
		$scope.selectedCodeSet = codeSet;
		$http.get('../svc/codesets/' + codeSet.meaning).success(function(data) {
			$scope.codevalues = data;
		});
	};

	$scope.addCodeValue = function(codeValue) {
		$http:post('../svc/codevalue', codeValue).success(function(data) {
		});
	};

	$scope.editCodeValue = function(codeValue) {
		$http.put('../svc/codevalue', codeValue).success(function(data) {
		});
	};

	$scope.selectedCodeValue = function(codevalue) {
		$scope.codevalue = codevalue;
	};

	$scope.removeCodeValue = function() {
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

	$scope.validate = function(display) {
		if (display.length == 0) {
			return "Display is required!";
		} else {
			return true;
		}
	};

	$scope.saveCodeValue = function(codeValue) {
		$http.put("../svc/codevalue", codeValue).success(function(data) {
			codeValue.codeValueUID = data.codeValueUID;
			codeValue.meaning = data.meaning;
			codeValue.codeValueUpdateCount = data.codeValueUpdateCount;
		});
	};

	$scope.addCodeValue = function() {
		$scope.inserted = {
				codeSetUID: $scope.selectedCodeSet.codeSetUID,
				codeValueUID: 0,
				display: '',
				meaning: null,
				active: true,
				codeValueUpdateCount: 0
		};

		$scope.codevalues.push($scope.inserted);
	};
});

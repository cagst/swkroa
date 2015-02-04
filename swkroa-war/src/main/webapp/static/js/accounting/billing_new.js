/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Deposit pages.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.controller('billingController', ['$scope', '$http', 'codesetService', 'membershipService',
    function($scope, $http, codesetService, membershipService) {

  $scope.getMemberhipsDueIn = function(days) {
    membershipService.getMembershipsDueInXDays(days).success(function(data) {
      $scope.membershipsDue = data;
      $scope.checkAll = true;
      $scope.totalAmount = 0;

      for (var idx = 0; idx < $scope.membershipsDue.length; idx++) {
          $scope.membershipsDue[idx].selected = true;
          $scope.totalAmount += $scope.membershipsDue[idx].effectiveDuesAmount;
      }

      $scope.totalMemberships = $scope.membershipsDue.length;
    });
  };

  $scope.toggleCheckAll = function() {
    for (var idx = 0; idx < $scope.membershipsDue.length; idx++) {
        $scope.membershipsDue[idx].selected = $scope.checkAll;
    }

    calculateTotals($scope);
  };

  $scope.toggleCheck = function(membership) {
    membership.selected = !membership.selected;
    calculateTotals($scope);
  };

  $scope.days = 30;
  $scope.totalMemberships = 0;
  $scope.totalAmount = 0;
}]);

var calculateTotals = function($scope) {
    $scope.totalMemberships = 0;
    $scope.totalAmount = 0;

    for (var idx = 0; idx < $scope.membershipsDue.length; idx++) {
        if ($scope.membershipsDue[idx].selected) {
          $scope.totalAmount += $scope.membershipsDue[idx].effectiveDuesAmount;
          $scope.totalMemberships += 1;
        }
    }
};
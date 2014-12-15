/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Deposit pages.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.controller('delinquencyController', ['$scope', 'codesetService', 'membershipService',
    function($scope, codesetService, membershipService) {

  codesetService.getCodeValuesForCodeSet('CLOSE_REASONS').success(function(data) {
    $scope.closeReasons = data;
  });

  membershipService.getDelinquentMemberships().success(function(data) {
    $scope.delinquencies = data;
    $scope.checkAll = true;

    for (var idx = 0; idx < $scope.delinquencies.length; idx++) {
        $scope.delinquencies[idx].selected = true;
    }
  });

  $scope.toggleCheckAll = function() {
    for (var idx = 0; idx < $scope.delinquencies.length; idx++) {
        $scope.delinquencies[idx].selected = $scope.checkAll;
    }
  };

}]);

/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Deposit pages.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.controller('delinquencyController', ['$scope', 'codesetService', 'membershipService',
    function($scope, codesetService, membershipService) {

  $scope.delinquencies = [];

  $scope.getDelinquencies = function() {
    membershipService.getDelinquentMemberships().success(function(data) {
      $scope.delinquencies = data;
      $scope.checkAll = true;

      for (var idx = 0; idx < $scope.delinquencies.length; idx++) {
          $scope.delinquencies[idx].selected = true;
      }
    });
  };

  $scope.toggleCheckAll = function() {
    for (var idx = 0; idx < $scope.delinquencies.length; idx++) {
        $scope.delinquencies[idx].selected = $scope.checkAll;
    }
  };

  $scope.canExport = function() {
    var membershipsSelected = false;
    for (var idx = 0; idx < $scope.delinquencies.length; idx++) {
      if ($scope.delinquencies[idx].selected) {
        membershipsSelected = true;
      }
    };

    return membershipsSelected;
  };

  $scope.canClose = function() {
    if ($scope.closeReason) {
      return ($scope.canExport() && ($scope.closeReason.codeValueUID > 0));
    } else {
      return false;
    }
  };

  $scope.closeMemberships = function() {
    var memberships = [];
    for (var idx = 0; idx < $scope.delinquencies.length; idx++) {
      if ($scope.delinquencies[idx].selected) {
        memberships.push($scope.delinquencies[idx].membershipUID);
      }
    };

    membershipService.closeMemberships(memberships, $scope.closeReason, $scope.closeText).success(function(data) {
      $('#closeMembershipsDlg').modal('hide');
      $scope.getDelinquencies();
    });
  };

  codesetService.getCodeValuesForCodeSet('CLOSE_REASONS').success(function(data) {
    $scope.closeReasons = data;
  });

  $scope.getDelinquencies();

}]);

var generateMembershipReminderLetters = function(reportyType, altAction) {
  $('#reminderLetterDlg').modal('hide');
  submitReportForm(reportyType, altAction);
};

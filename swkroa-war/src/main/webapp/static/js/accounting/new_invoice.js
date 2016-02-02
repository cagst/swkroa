/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Dues Renewal pages.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.controller('invoiceController', ['$scope', '$http', 'codesetService', 'membershipService',
    function($scope, $http, codesetService, membershipService) {

  $scope.membershipsDue = [];

  $scope.getMemberhipsDueIn = function(days) {
    showProcessingDialog();

    membershipService.getMembershipsDueInXDays(days).success(function(data) {
      $scope.membershipsDue = data;
      $scope.checkAll = true;
      $scope.totalAmount = 0;

      for (var idx = 0; idx < $scope.membershipsDue.length; idx++) {
          $scope.membershipsDue[idx].selected = true;
          $scope.totalAmount += $scope.membershipsDue[idx].calculatedDuesAmount;
      }

      $scope.totalMemberships = $scope.membershipsDue.length;

      hideProcessingDialog();
    });
  };

  $scope.toggleCheckAll = function() {
    for (var idx = 0; idx < $scope.membershipsDue.length; idx++) {
        $scope.membershipsDue[idx].selected = $scope.checkAll;
    }

    calculateTotals($scope);
  };

  $scope.toggleCheck = function() {
    calculateTotals($scope);
  };

  $scope.canExport = function() {
    var membershipsSelected = false;
    for (var idx = 0; idx < $scope.membershipsDue.length; idx++) {
      if ($scope.membershipsDue[idx].selected) {
        membershipsSelected = true;
      }
    }

    return membershipsSelected;
  };

  $scope.renewingMemberships = function() {
    $('#renewMembershipsDlg').modal('hide');

    var memberships = [];
    for (var idx = 0; idx < $scope.membershipsDue.length; idx++) {
      if ($scope.membershipsDue[idx].selected) {
        memberships.push($scope.membershipsDue[idx].membershipUID);
      }
    }

    showProcessingDialog();

    membershipService.renewMemberships(memberships, $scope.transactionDate, $scope.transactionDescription, $scope.transactionMemo).success(function(data) {
      $scope.getMemberhipsDueIn($scope.days);
      hideProcessingDialog();
    });
  };

  $scope.openTransactionDate = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.openedTransactionDate = true;
  };

  determineRenewalPeriod($scope);

  $scope.days = 30;
  $scope.totalMemberships = 0;
  $scope.totalAmount = 0;
  $scope.transactionDate = new Date();
}]);

var calculateTotals = function($scope) {
    $scope.totalMemberships = 0;
    $scope.totalAmount = 0;

    for (var idx = 0; idx < $scope.membershipsDue.length; idx++) {
        if ($scope.membershipsDue[idx].selected) {
          $scope.totalAmount += $scope.membershipsDue[idx].calculatedDuesAmount;
          $scope.totalMemberships += 1;
        }
    }
};

var determineRenewalPeriod = function($scope) {
  var currentYear = new Date().getFullYear();

  $scope.membershipRenewalPeriod = "(" + currentYear + " - " + (currentYear + 1) + ")";
};

var generateMembershipRenewalLetters = function(reportyType) {
  $('#renewalLetterDlg').modal('hide');
  submitReportForm(reportyType);
};

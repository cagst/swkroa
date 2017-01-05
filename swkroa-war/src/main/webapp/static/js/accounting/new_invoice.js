/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Dues Renewal pages.
 *
 * Author:  Craig Gaskill
 */
(function(window, angular, $) {
  'use strict';

  angular.module('swkroaApp').controller('InvoiceController', InvoiceController);

  InvoiceController.$inject = ['MembershipService'];

  function InvoiceController(membershipService) {
    var vm = this;

    vm.membershipsDue = [];
    vm.days = 30;
    vm.totalMemberships = 0;
    vm.totalAmount = 0;
    vm.transactionDate = new Date();

    activate();

    /********************************************
     * Define binding methods
     ********************************************/
    vm.getMembershipsDueIn = getMembershipsDueIn;
    vm.toggleCheckAll = toggleCheckAll;
    vm.toggleCheck = toggleCheck;
    vm.canExport = canExport;
    vm.renewingMemberships = renewingMemberships;
    vm.openTransactionDate = openTransactionDate;

    /********************************************
     * Implement Methods
     ********************************************/

    function getMembershipsDueIn() {
      showProcessingDialog();

      membershipService.getMembershipsDueInXDays(vm.days).then(function(response) {
        if (responseSuccessful(response)) {
          vm.membershipsDue = response.data;
          vm.checkAll = true;
          vm.totalAmount = 0;

          for (var idx = 0; idx < vm.membershipsDue.length; idx++) {
            vm.membershipsDue[idx].selected = true;
            vm.totalAmount += vm.membershipsDue[idx].calculatedDuesAmount;
          }

          vm.totalMemberships = vm.membershipsDue.length;

          hideProcessingDialog();
        }
      });
    }

    function toggleCheckAll() {
      for (var idx = 0; idx < vm.membershipsDue.length; idx++) {
        vm.membershipsDue[idx].selected = vm.checkAll;
      }

      calculateTotals();
    }

    function toggleCheck() {
      calculateTotals();
    }

    function canExport() {
      var membershipsSelected = false;
      for (var idx = 0; idx < vm.membershipsDue.length; idx++) {
        if (vm.membershipsDue[idx].selected) {
          membershipsSelected = true;
        }
      }

      return membershipsSelected;
    }

    function renewingMemberships() {
      $('#renewMembershipsDlg').modal('hide');

      var memberships = [];
      for (var idx = 0; idx < vm.membershipsDue.length; idx++) {
        if (vm.membershipsDue[idx].selected) {
          memberships.push(vm.membershipsDue[idx].membershipUID);
        }
      }

      membershipService.renewMemberships(memberships, vm.transactionDate, vm.transactionDescription, vm.transactionMemo);

      $('#transactionJobSubmittedDlg').modal('show');
    }

    function openTransactionDate($event) {
      $event.preventDefault();
      $event.stopPropagation();

      vm.openedTransactionDate = true;
    }

    function calculateTotals() {
      vm.totalMemberships = 0;
      vm.totalAmount = 0;

      for (var idx = 0; idx < vm.membershipsDue.length; idx++) {
        if (vm.membershipsDue[idx].selected) {
          vm.totalAmount += vm.membershipsDue[idx].calculatedDuesAmount;
          vm.totalMemberships += 1;
        }
      }
    }

    function activate() {
      var currentYear = new Date().getFullYear();

      vm.membershipRenewalPeriod = "(" + currentYear + " - " + (currentYear + 1) + ")";
      vm.transactionDescription  = "Invoice " + currentYear + " - " + (currentYear + 1);
    }

  }

})(window, window.angular, window.jQuery);

var generateMembershipRenewalLetters = function(reportyType) {
  $('#renewalLetterDlg').modal('hide');
  submitReportForm(reportyType);
};

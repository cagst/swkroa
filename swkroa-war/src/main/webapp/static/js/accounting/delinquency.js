/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Deposit pages.
 *
 * Author:  Craig Gaskill
 */
(function(window, angular, $) {
  'use strict';

  angular.module('swkroaApp').controller('DelinquencyController', DelinquencyController);

  DelinquencyController.$inject = ['CodeSetService', 'MembershipService'];

  function DelinquencyController(codeSetService, membershipService) {
    var vm = this;

    vm.delinquencies = [];

    /********************************************
     * Define binding methods
     ********************************************/

    vm.getDelinquencies = getDelinquencies;
    vm.toggleCheckAll = toggleCheckAll;
    vm.canExport = canExport;
    vm.canClose = canClose;
    vm.closeMemberships = closeMemberships;

    activate();

    /********************************************
     * Implement Methods
     ********************************************/

    function activate() {
      codeSetService.getCodeValuesForCodeSet('CLOSE_REASONS').then(function(response) {
        if (responseSuccessful(response)) {
          vm.closeReasons = response.data;
        }
      });

      vm.getDelinquencies();
    }

    function getDelinquencies() {
      membershipService.getDelinquentMemberships().then(function(response) {
        if (responseSuccessful(response)) {
          vm.delinquencies = response.data;
          vm.checkAll = true;

          for (var idx = 0; idx < vm.delinquencies.length; idx++) {
            vm.delinquencies[idx].selected = true;
          }
        }
      });
    }

    function toggleCheckAll() {
      for (var idx = 0; idx < vm.delinquencies.length; idx++) {
        vm.delinquencies[idx].selected = vm.checkAll;
      }
    }

    function canExport() {
      var membershipsSelected = false;
      for (var idx = 0; idx < vm.delinquencies.length; idx++) {
        if (vm.delinquencies[idx].selected) {
          membershipsSelected = true;
        }
      }

      return membershipsSelected;
    }

    function canClose() {
      if (vm.closeReason) {
        return (vm.canExport() && (vm.closeReason.codeValueUID > 0));
      } else {
        return false;
      }
    }

    function closeMemberships() {
      var memberships = [];
      for (var idx = 0; idx < vm.delinquencies.length; idx++) {
        if (vm.delinquencies[idx].selected) {
          memberships.push(vm.delinquencies[idx].membershipUID);
        }
      }

      membershipService.closeMemberships(memberships, vm.closeReason, vm.closeText).then(function(response) {
        if (responseSuccessful(response)) {
          $('#closeMembershipsDlg').modal('hide');
          vm.getDelinquencies();
        }
      });
    }
  }

})(window, window.angular, window.jQuery);

var generateMembershipReminderLetters = function(reportyType, altAction) {
  $('#reminderLetterDlg').modal('hide');
  submitReportForm(reportyType, altAction);
};

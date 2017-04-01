/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Dues pages.
 *
 * Author:  Craig Gaskill
 */
(function(window, angular, $) {
  'use strict';

  angular.module('swkroaApp').controller('InvoiceController', InvoiceController);

  InvoiceController.$inject = ['$http', 'TransactionService'];

  function InvoiceController($http, transactionService) {
    const vm = this;

    vm.increment     = 10;
    vm.increments    = [];
    vm.invoiceGroups = [];
    vm.start         = 0;
    vm.page          = 0;
    vm.pages         = 1;

    /********************************************
     * Define binding methods
     ********************************************/

    vm.getInvoiceGroups = getInvoiceGroups;
    vm.generateInvoicesReport = generateInvoicesReport;

    vm.firstPage = firstPage;
    vm.prevPage = prevPage;
    vm.nextPage = nextPage;
    vm.lastPage = lastPage;

    activate();

    /********************************************
     * Implement Methods
     ********************************************/

    function activate() {
      $http.get('/api/jobs/pending/RENEWAL').then(function(response) {
        if (responseSuccessful(response)) {
          vm.jobsPending = (response.data.length > 0);
        }
      });

      vm.firstPage();
    }

    function getInvoiceGroups(start, limit) {
      transactionService.getInvoiceGroups(start, limit).then(function(response) {
        if (responseSuccessful(response)) {
          vm.invoiceGroups = response.data.items;
          vm.pages = Math.ceil(response.data.totalItemCount / vm.increment);

          for (let idx = 0; idx < vm.pages; idx++) {
            vm.increments.push(idx + 1);
          }
        }
      })
    }

    function generateInvoicesReport(reportType, startDate, endDate) {
      $("#reportType").val(reportType);
      $("#start_date").val(startDate);
      $("#end_date").val(endDate);

      submitReportForm(reportType);
    }

    function firstPage() {
      setPage(1);
    }

    function prevPage() {
      setPage(vm.page - 1);
    }

    function nextPage() {
      setPage(vm.page + 1);
    }

    function lastPage() {
      setPage(vm.pages);
    }

    function setPage(page) {
      vm.page = page;
      vm.getInvoiceGroups((vm.page - 1) * vm.increment, vm.increment);
    }
  }
})(window, window.angular, window.jQuery);

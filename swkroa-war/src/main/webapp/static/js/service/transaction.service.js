/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Defines a service for retrieving and persisting Transactions.
 *
 * Author: Craig Gaskill
 */
(function(window, angular) {
  angular.module('swkroaApp').service('TransactionService', TransactionService);

  TransactionService.$inject = ['$http'];

  function TransactionService($http) {
    var vm = this;
    var rootUrl = "/api/transactions/invoices";

    vm.getInvoiceGroups = getInvoiceGroups;
    vm.getPaymentGroups = getPaymentGroups;
    vm.getUnpaidTransactions = getUnpaidTransactions;
    vm.saveTransaction = saveTransaction;

    /********************************************
     * Implement Methods
     ********************************************/

    function getInvoiceGroups(start, limit) {
      return $http.get(rootUrl + "?start=" + start + "&limit=" + limit);
    }

    function getPaymentGroups(start, limit) {
      return $http.get(rootUrl + '?start=' + start + '&limit=' + limit);
    }

    function getUnpaidTransactions() {
      return $http.get(rootUrl + '/unpaid');
    }

    function saveTransaction(transaction) {
      return $http.post(rootUrl, transaction);
    }
  }

})(window, window.angular);

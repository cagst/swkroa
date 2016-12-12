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
    this.getInvoiceGroups = function(start, limit) {
      var promise = $http.get('/api/transactions/invoices?start=' + start + '&limit=' + limit);

      promise.success = function(fn) {
        promise.then(function(response) {
          if (responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      promise.error = function(fn) {
        promise.then(function(response) {
          if (!responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      return promise;
    };

    this.getPaymentGroups = function(start, limit) {
      var promise = $http.get('/api/transactions/payments?start=' + start + '&limit=' + limit);

      promise.success = function(fn) {
        promise.then(function(response) {
          if (responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      promise.error = function(fn) {
        promise.then(function(response) {
          if (!responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      return promise;
    };

    this.getUnpaidTransactions = function() {
      var promise = $http.get('/api/transactions/unpaid');

      promise.success = function(fn) {
        promise.then(function(response) {
          if (responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      promise.error = function(fn) {
        promise.then(function(response) {
          if (!responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      return promise;
    };

    this.saveTransaction = function(transaction) {
      var promise = $http.post('/api/transactions', transaction);

      promise.success = function(fn) {
        promise.then(function(response) {
          if (responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      promise.error = function(fn) {
        promise.then(function(response) {
          if (!responseSuccessful(response)) {
            fn(response.data, response.status);
          }
        });
      };

      return promise;
    };
  }

})(window, window.angular);

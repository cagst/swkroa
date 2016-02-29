/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Dues pages.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.controller('invoiceController',
                     ['$scope',
                      '$http',
                      'transactionService',
    function($scope, $http, transactionService) {

  $scope.increment     = 10;
  $scope.increments    = [];
  $scope.invoiceGroups = [];
  $scope.start         = 0;
  $scope.page          = 0;
  $scope.pages         = 1;

  $http.get('/api/jobs/pending/RENEWAL').then(function(response) {
    $scope.jobsPending = (response.data.length > 0);
  });

  $scope.getInvoiceGroups = function(start, limit) {
    transactionService.getInvoiceGroups(start, limit).success(function(data) {
      $scope.invoiceGroups = data.items;
      $scope.pages = Math.ceil(data.totalItemCount / $scope.increment);

      for (var idx = 0; idx < $scope.pages; idx++) {
        $scope.increments.push(idx+1);
      }
    }
  )};

  $scope.firstPage = function() {
    $scope.setPage(1);
  };

  $scope.prevPage = function() {
    $scope.setPage($scope.page - 1);
  };

  $scope.setPage = function(page) {
    $scope.page = page;
    $scope.getInvoiceGroups(($scope.page - 1) * $scope.increment, $scope.increment);
  };

  $scope.nextPage = function() {
    $scope.setPage($scope.page + 1);
  };

  $scope.lastPage = function() {
    $scope.setPage($scope.pages);
  };

  $scope.generateInvoicesReport = function(reportType, startDate, endDate) {
    $("#reportType").val(reportType);
    $("#start_date").val(startDate);
    $("#end_date").val(endDate);

    submitReportForm(reportType);
  };

  $scope.firstPage();
}]);

/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Dues pages.
 *
 * Author:  Craig Gaskill
 */

swkroaApp.controller('duesController', ['$scope', '$http',
    function($scope, $http) {

  $scope.dues = [];

  $scope.getDues = function() {
    var url = "/api/dues/";

    $http.get(url).success(function(data) {
      $scope.dues = data;
    });
  };

  $scope.getDues();
}]);

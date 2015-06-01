/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 */

swkroaApp.controller('membertypeController', ['$scope', '$http', function($scope, $http) {
  $http.get('/api/membertypes').success(function(data) {
    $scope.types = data;
  });

  $scope.getType = function(type) {
    $scope.selected = type;
  };
}]);

/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 */

swkroaApp.run(function(editableOptions) {
  editableOptions.theme = 'bs3';
});

swkroaApp.controller('membertypeController', ['$scope', 'memberTypeService', function($scope, memberTypeService) {
  memberTypeService.getMemberTypes().success(function(data) {
    $scope.types = data;
  });

  $scope.getType = function(type) {
    $scope.selected = type;

    memberTypeService.getAllMemberTypesByMemberTypeId(type.memberTypeUID).success(function(data) {
      $scope.allMemberTypes = data;
    });
  };
}]);

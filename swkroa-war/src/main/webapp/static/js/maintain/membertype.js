/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 */

swkroaApp.run(function(editableOptions) {
  editableOptions.theme = 'bs3';
});

swkroaApp.controller('membertypeController', ['$scope', 'memberTypeService', '$http', function($scope, memberTypeService, $http) {
  memberTypeService.getMemberTypes().success(function(data) {
    $scope.types = data;
  });

  $scope.getType = function(type) {
    $scope.selected = type;

    memberTypeService.getAllMemberTypesByMemberTypeId(type.memberTypeUID).success(function(data) {
      $scope.allMemberTypes = data;
    });
  };

  $scope.saveMemberType = function(memberType) {
    $http.post('/api/membertypes/', memberType).then(function(response) {
      if (response.status == 201) {
        $scope.selected = response.data;
        $scope.types.push(response.data);
      } else if (response.status == 200) {
        $scope.selected = response.data;
        for (idx = 0; idx < $scope.types.length; idx++) {
          if ($scope.types[idx].memberTypeUID == memberType.memberTypeUID) {
            $scope.types[idx] = response.data;
            break;
          }
        }
      }
    });
  };

  $scope.newRate = function() {
    $scope.memberRate = {active: true, beginEffectiveDateTime: new Date(), duesAmount: $scope.selected.duesAmount};
  };

}]);

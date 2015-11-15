/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 */

swkroaApp.run(function(editableOptions) {
  editableOptions.theme = 'bs3';
});

swkroaApp.controller('membertypeController', ['$scope', 'memberTypeService', '$http', function($scope, memberTypeService, $http) {
  $scope.beginDateIsOpen = false;

  memberTypeService.getMemberTypes().success(function(data) {
    $scope.types = data;
  });

  $scope.openBeginDate = function($event) {
    $scope.beginDateIsOpen = true;
  };

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
    $scope.memberRate = {active: true, beginEffectiveDate: new Date(), duesAmount: $scope.selected.duesAmount};
  };

  $scope.saveNewRate = function() {
    $http.put('/api/membertypes/' + $scope.selected.previousMemberTypeUID, $scope.memberRate).success(function(data) {
      $scope.getType($scope.selected);
    });

    $('#changeRateDlg').modal('hide');
  }

}]);

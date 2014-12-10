/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 */

swkroaApp.run(function(editableOptions) {
  editableOptions.theme = 'bs3';
});

swkroaApp.controller('codesetController', ['$scope', 'codesetService', function($scope, codesetService) {
  codesetService.getCodeSets().then(function(response) {
    if (response.status == 200) {
      $scope.codesets = response.data;
    }
  });

  $scope.getCodeValues = function(codeSet) {
    $scope.selectedCodeSet = codeSet;

    codesetService.getCodeValuesForCodeSet(codeSet.meaning).then(function(response) {
      if (response.status == 200) {
        $scope.codevalues = response.data;
      }
    });
  };

  $scope.selectedCodeValue = function(codevalue) {
    $scope.codevalue = codevalue;
  };

  $scope.removeCodeValue = function() {
    $scope.codevalue.active = false;
    $('#confirmDeletion').modal('hide');

    codesetService.saveCodeValue($scope.selectedCodeSet.meaning, $scope.codevalue).then(function(response) {
      if (response.status == 200) {
        $scope.codevalue = data;
      } else {
        // we failed to save (remove) the codevalue
        // set it back to active
        $scope.codevalue.active = true;
      }
    });
  };

  $scope.validate = function(display) {
    if (display.length == 0) {
      return "Display is required!";
    } else {
      return true;
    }
  };

  $scope.saveCodeValue = function(codeValue) {
    codesetService.saveCodeValue($scope.selectedCodeSet.meaning, codeValue).then(function(response) {
      if (response.status == 201) {
        codeValue = response.data;
      } else if (response.status == 200) {
        codeValue = response.data;
      }
    });
  };

  $scope.addCodeValue = function() {
    $scope.inserted = {
      codeSetUID: $scope.selectedCodeSet.codeSetUID,
      codeValueUID: 0,
      display: '',
      meaning: null,
      active: true,
      codeValueUpdateCount: 0
    };

    $scope.codevalues.push($scope.inserted);
  };
}]);

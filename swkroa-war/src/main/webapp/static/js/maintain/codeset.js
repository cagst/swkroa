/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 */
(function(window, angular) {
  'use strict';

  angular.module('swkroaApp').run(function(editableOptions) {
    editableOptions.theme = 'bs3';
  });

  angular.module('swkroaApp').controller('CodeSetController', CodeSetController);

  CodeSetController.$inject = ['CodeSetService'];

  function CodeSetController(codeSetService) {
    var vm = this;

    vm.codeSets = null;
    vm.selectedCodeSet = null;
    vm.codeValues = null;
    vm.selectedCodeValue = null;

    /********************************************
     * Define binding methods
     ********************************************/

    vm.getCodeValues = getCodeValues;
    vm.selectedCodeValue = selectedCodeValue;
    vm.removeCodeValue = removeCodeValue;
    vm.validate = validate;
    vm.saveCodeValue = saveCodeValue;
    vm.addCodeValue = addCodeValue;

    activate();

    /********************************************
     * Implement Methods
     ********************************************/

    function getCodeValues(codeSet) {
      vm.selectedCodeSet = codeSet;

      codeSetService.getCodeValuesForCodeSet(codeSet.meaning).then(function(response) {
        if (responseSuccessful(response)) {
          vm.codeValues = response.data;
        }
      });
    }

    function selectedCodeValue(codeValue) {
      vm.selectedCodeValue = codeValue;
    }

    function removeCodeValue() {
      vm.selectedCodeValue.active = false;
      $('#confirmDeletion').modal('hide');

      codeSetService.saveCodeValue(vm.selectedCodeSet.meaning, vm.selectedCodeValue).then(function(response) {
        if (responseSuccessful(response)) {
          vm.selectedCodeValue = response.data;
        } else {
          // we failed to save (remove) the codevalue
          // set it back to active
          vm.selectedCodeValue.active = true;
        }
      });
    }

    function validate(display) {
      if (display.length == 0) {
        return "Display is required!";
      } else {
        return true;
      }
    }

    function saveCodeValue(codeValue) {
      codeSetService.saveCodeValue(vm.selectedCodeSet.meaning, codeValue).then(function(response) {
        if (response.status == 201) {
          vm.selectedCodeValue = response.data;
        } else if (response.status == 200) {
          vm.selectedCodeValue = response.data;
        }
      });
    }

    function addCodeValue() {
      vm.inserted = {
        codeSetUID: vm.selectedCodeSet.codeSetUID,
        codeValueUID: 0,
        display: '',
        meaning: null,
        active: true,
        codeValueUpdateCount: 0
      };

      vm.codeValues.push(vm.inserted);
    }

    function activate() {
      codeSetService.getCodeSets().then(function(reponse) {
        if (responseSuccessful(reponse)) {
          vm.codeSets = reponse.data;
        }
      });
    }
  }

})(window, window.angular);

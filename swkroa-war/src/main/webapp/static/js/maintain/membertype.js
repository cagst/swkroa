/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 */

(function(window, angular, $) {
  'use strict';

  angular.module('swkroaApp').run(function(editableOptions) {
    editableOptions.theme = 'bs3';
  });

  angular.module('swkroaApp').controller('MemberTypeController', MemberTypeController);

  MemberTypeController.$inject = ['$http', 'MemberTypeService'];

  function MemberTypeController($http, memberTypeService) {
    var vm = this;

    vm.selected = null;
    vm.allMemberTypes = null;
    vm.memberRate = null;

    vm.isOpen = {
      beginDate: false
    };

    /********************************************
     * Define binding methods
     ********************************************/

    vm.getType = getType;
    vm.openBeginDate = openBeginDate;
    vm.saveMemberType = saveMemberType;
    vm.newRate = newRate;
    vm.saveNewRate = saveNewRate;

    activate();

    /********************************************
     * Implement Methods
     ********************************************/

    function getType(type) {
      vm.selected = type;

      memberTypeService.getAllMemberTypesByMemberTypeId(type.memberTypeUID).success(function(data) {
        vm.allMemberTypes = data;
      });
    }

    function openBeginDate($event) {
      $event.preventDefault();
      $event.stopPropagation();

      vm.isOpen.beginDate = true;
    }

    function saveMemberType(memberType) {
      $http.post('/api/membertypes/', memberType).then(function(response) {
        if (response.status === 201) {
          vm.selected = response.data;
          vm.types.push(response.data);
        } else if (response.status === 200) {
          vm.selected = response.data;
          for (var idx = 0; idx < vm.types.length; idx++) {
            if (vm.types[idx].memberTypeUID === memberType.memberTypeUID) {
              vm.types[idx] = response.data;
              break;
            }
          }
        }
      });
    }

    function newRate() {
      vm.memberRate = {active: true, beginEffectiveDate: new Date(), duesAmount: vm.selected.duesAmount};
    }

    function saveNewRate() {
      $http.put('/api/membertypes/' + vm.selected.previousMemberTypeUID, vm.memberRate).then(function(response) {
        if (responseSuccessful(response)) {
          vm.getType(vm.selected);
        }
      });

      $('#changeRateDlg').modal('hide');
    }

    function activate() {
      memberTypeService.getMemberTypes().then(function(response) {
        if (responseSuccessful(response)) {
          vm.types = response.data;
        }
      });
    }
  }
})(window, window.angular, window.jQuery);

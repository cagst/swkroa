/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Membership pages.
 *
 * Author:  Craig Gaskill
 */
(function(window, angular, $) {
  'use strict';

  angular.module('swkroaApp').controller('MembershipController', MembershipController);

  MembershipController.$inject = ['$http', 'CodeSetService', 'ContactService', 'MembershipService', 'TransactionService'];

  function MembershipController($http, codeSetService, contactService, membershipService, transactionService) {
    var vm = this;

    vm.query            = "";
    vm.view             = "listing";
    vm.filterStatus     = "ACTIVE";
    vm.filterBalance    = "ALL";
    vm.filtering        = "off";
    vm.showCalculations = false;

    vm.searched    = false;
    vm.fullyLoaded = false;
    vm.original    = null;
    vm.message     = null;
    vm.created     = false;
    vm.updated     = false;

    vm.isOpen = {
      dueDate: false,
      primaryJoinDate: false,
      spouseJoinDate: false,
      commentDate: false,
      transactionDate: false
    };

    /********************************************
     * Define binding methods
     ********************************************/

    vm.onQueryKeydown = onQueryKeydown;

    // Filter Methods
    vm.getFilters = getFilters;
    vm.showFilterOptions = showFilterOptions;
    vm.applyFilter = applyFilter;
    vm.cancelFilter = cancelFilter;

    // Membership Methods
    vm.getMemberships = getMemberships;
    vm.getMembership = getMembership;
    vm.addMembership = addMembership;
    vm.editMembership = editMembership;
    vm.openMembership = openMembership;
    vm.closeMembership = closeMembership;

    // Comment Methods
    vm.selectComment = selectComment;
    vm.addComment = addComment;
    vm.removeComment = removeComment;
    vm.saveComment = saveComment;

    // Transaction Methods
    vm.deleteTransaction = deleteTransaction;
    vm.editTransaction = editTransaction;
    vm.calculateTransactionAmount = calculateTransactionAmount;
    vm.addTransaction = addTransaction;
    vm.removeTransaction = removeTransaction;
    vm.saveTransaction = saveTransaction;
    vm.addTransactionEntry = addTransactionEntry;
    vm.deleteTransactionEntry = deleteTransactionEntry;

    // Date-Picker Methods
    vm.openDueDate = openDueDate;
    vm.openPrimaryJoinDate = openPrimaryJoinDate;
    vm.openMemberJoinDate = openMemberJoinDate;
    vm.openSpouseJoinDate = openSpouseJoinDate;
    vm.openCommentDate = openCommentDate;
    vm.openTransactionDate = openTransactionDate;

    // Methods for Spouse
    vm.addSpouse = addSpouse;
    vm.removeSpouse = removeSpouse;

    // Methods for County
    vm.addCounty = addCounty;
    vm.removeCounty = removeCounty;
    vm.validateVotingCounty = validateVotingCounty;

    // Methods for Member
    vm.addMember = addMember;
    vm.removeMember = removeMember;
    vm.generateOwnerId = generateOwnerId;

    // Status Methods
    vm.hasChanges = hasChanges;
    vm.cancelChanges = cancelChanges;

    // Misc. Methods
    vm.saveMembership = saveMembership;
    vm.canClose = canClose;
    vm.toggleShowCalculations = toggleShowCalculations;

    vm.addAddress = addAddress;
    vm.ensurePrimaryAddress = ensurePrimaryAddress;
    vm.removeAddress = removeAddress;

    vm.addPhone = addPhone;
    vm.ensurePrimaryPhone = ensurePrimaryPhone;
    vm.removePhone = removePhone;

    vm.addEmail = addEmail;
    vm.ensurePrimaryEmail = ensurePrimaryEmail;
    vm.removeEmail = removeEmail;

    activate();

    /********************************************
     * Implement Methods
     ********************************************/

    function activate() {
      codeSetService.getCodeValuesForCodeSet('TRANSACTION_ENTRY_TYPE').then(function(response) {
        if (responseSuccessful(response)) {
          vm.entryTypes = response.data;
        }
      });

      codeSetService.getCodeValuesForCodeSet('CLOSE_REASONS').then(function(response) {
        if (responseSuccessful(response)) {
          vm.closeReasons = response.data;
        }
      });

      vm.filterText = getFilters();
    }

    function onQueryKeydown($event) {
      if ($event.keyCode == 13 && vm.query.length >= 2) {
        vm.getMemberships();
      }
    }

    function getFilters() {
      var filterText = "";

      if (vm.filterStatus == 'ACTIVE') {
        if (filterText) {
          filterText = filterText + ", ";
        }
        filterText = filterText + "Active";
      } else if (vm.filterStatus == 'INACTIVE') {
        if (filterText) {
          filterText = filterText + ", ";
        }
        filterText = filterText + "Inactive";
      }

      if (vm.filterBalance == 'DELINQUENT') {
        if (filterText) {
          filterText = filterText + ", ";
        }
        filterText = filterText + "Delinquent";
      } else if (vm.filterBalance == 'PAID') {
        if (filterText) {
          filterText = filterText + ", ";
        }
        filterText = filterText + "Paid";
      } else if (vm.filterBalance == 'CREDIT') {
        if (filterText) {
          filterText = filterText + ", ";
        }
        filterText = filterText + "Credit";
      }

      if (filterText.length === 0) {
        filterText = "All";
      }

      return filterText;
    }

    function showFilterOptions() {
      vm.filtering = 'on';
    }

    function applyFilter() {
      vm.filtering  = "off";
      vm.filterText = vm.getFilters();
      if (vm.query && vm.query.length > 0) {
        vm.getMemberships();
      }
    }

    function cancelFilter() {
      vm.filtering = "off";
    }

    function getMemberships() {
      vm.membership = null;

      vm.created = false;
      vm.updated = false;

      membershipService.getMemberships(vm.query, vm.filterStatus, vm.filterBalance).then(function(response) {
        if (response.status == 200) {
          vm.memberships = response.data;
          vm.searched    = true;
        }
      });
    }

    function getMembership(membershipUID) {
      vm.created = false;
      vm.updated = false;

      membershipService.getMembership(membershipUID).then(function(response) {
        if (response.status == 200) {
          vm.membership = response.data;
          for (var idx = 0; idx < vm.memberships.length; idx++) {
            if (vm.memberships[idx].membershipUID == vm.membership.membershipUID) {
              vm.memberships[idx] = vm.membership;
              break;
            }
          }
        }
      });
    }

    function addMembership() {
      membershipService.getMembership(0).then(function(response) {
        if (response.status == 200) {
          vm.membership      = response.data;
          vm.original        = angular.copy(response.data);
        }
      });

      if (!vm.fullyLoaded) {
        loadAll();
      }

      vm.view = "add";
    }

    function editMembership() {
      if (!vm.fullyLoaded) {
        loadAll();
      }

      vm.original = angular.copy(vm.membership);
      vm.view     = "edit";
    }

    function openMembership() {
      vm.membership.active          = true;
      vm.membership.closeReasonUID  = null;
      vm.membership.closeReasonText = null;
      vm.membership.closeDate       = null;

      $('#openMembershipsDlg').modal('hide');

      membershipService.saveMembership(vm.membership).then(function(response) {
        if (response.status == 200) {
          var idx = vm.memberships.indexOf(vm.membership);
          vm.memberships.splice(idx, 1);
          vm.getMemberships();
        } else {
          vm.membership.active = false;
        }
      });
    }

    function closeMembership() {
      var membershipIds = [];
      membershipIds.push(vm.membership.membershipUID);

      membershipService.closeMemberships(membershipIds, vm.closeReason, vm.closeText).success(function() {
        $('#closeMembershipsDlg').modal('hide');
        vm.getMemberships();
      });
    }

    function selectComment(comment) {
      vm.commentIdx = vm.membership.comments.indexOf(comment);
      vm.comment    = angular.copy(comment);
    }

    function addComment() {
      vm.commentIdx = -1;
      vm.comment = {active: true, commentDate: new Date()};
    }

    function removeComment() {
      vm.comment.active = false;
      $http.put("/api/comments", vm.comment).success(function(data) {
        vm.membership.comments.splice(vm.commentIdx, 1);
      }).error(function(data) {
        // TODO: Need to add a message for the user
        vm.comment.active = true;
        vm.membership.comments[vm.commentIdx] = vm.comment;
      });
      vm.comment = null;
      $('#deleteComment').modal('hide');
    }

    function saveComment() {
      vm.comment.parentEntityName = "MEMBERSHIP";
      vm.comment.parentEntityUID  = vm.membership.membershipUID;

      $http.put("/api/comments", vm.comment).success(function(data) {
        if (vm.commentIdx == -1) {
          vm.membership.comments.push(data);
        } else {
          vm.membership.comments[vm.commentIdx] = data;
        }
      });

      vm.comment = null;
      $('#modifyComment').modal('hide');
    }

    function deleteTransaction(transaction) {
      vm.transactionIdx = vm.membership.transactions.indexOf(transaction);
      vm.transaction    = angular.copy(transaction);
    }

    function editTransaction(transaction) {
      vm.transactionIdx = vm.membership.transactions.indexOf(transaction);
      vm.transaction    = angular.copy(transaction);

      $("#modifyTransaction").modal('toggle');
      vm.calculateTransactionAmount();
    }

    function calculateTransactionAmount() {
      var amount = 0;
      for (var idx = 0; idx < vm.transaction.transactionEntries.length; idx++) {
        if (vm.transaction.transactionEntries[idx].active) {
          amount = amount + vm.transaction.transactionEntries[idx].transactionEntryAmount;
        }
      }

      vm.transactionAmount = amount;
    }

    function addTransaction() {
      vm.transactionIdx = -1;
      vm.transaction = {active: true, transactionDate: new Date(), transactionEntries: []};
    }

    function removeTransaction() {
      $('#deleteTransaction').modal('hide');
      vm.transaction.active = false;

      transactionService.saveTransaction(vm.transaction)
        .success(function(data) {
          vm.membership.transactions.splice(vm.transactionIdx, 1);
      })
        .error(function(data) {
          vm.transaction.active = true;
          vm.membership.transactions[vm.transactionIdx] = vm.transaction;
        });

      vm.transaction = null;
    }

    function saveTransaction() {
      $('#modifyTransaction').modal('hide');
      vm.transaction.membershipUID  = vm.membership.membershipUID;

      transactionService.saveTransaction(vm.transaction).success(function(data, status) {
        if (status == 201) {
          vm.membership.transactions.push(data);
        } else {
          vm.membership.transactions[vm.transactionIdx] = data;
        }
      });

      vm.transaction = null;
    }

    function addTransactionEntry() {
      var entryType;

      for (var idx = 0; idx < vm.entryTypes.length; idx++) {
        if (vm.entryTypes[idx].meaning == 'TRANS_DUES_BASE') {
          entryType = vm.entryTypes[idx];
          break;
        }
      }

      var entry = {transactionEntryUID: 0, active: true, transactionEntryAmount: 0.0, transactionEntryType: entryType};
      vm.transaction.transactionEntries.push(entry);
    }

    function deleteTransactionEntry(entry) {
      var idx = vm.transaction.transactionEntries.indexOf(entry);
      if (entry.transactionEntryUID == 0) {
        vm.transaction.transactionEntries.splice(idx, 1);
      } else {
        entry.active = false;
        vm.transaction.transactionEntries[idx] = entry;
      }

      vm.calculateTransactionAmount();
    }

    function openDueDate($event) {
      $event.preventDefault();
      $event.stopPropagation();

      vm.isOpen.dueDate = true;
    }

    function openPrimaryJoinDate($event) {
      $event.preventDefault();
      $event.stopPropagation();

      vm.isOpen.primaryJoinDate = true;
    }

    function openMemberJoinDate($event, member) {
      $event.preventDefault();
      $event.stopPropagation();

      member.joinDateOpened = true;
    }

    function openSpouseJoinDate($event) {
      $event.preventDefault();
      $event.stopPropagation();

      vm.isOpen.spouseJoinDate = true;
    }

    function openCommentDate($event) {
      $event.preventDefault();
      $event.stopPropagation();

      vm.isOpen.commentDate = true;
    }

    function openTransactionDate($event) {
      $event.preventDefault();
      $event.stopPropagation();

      vm.isOpen.transactionDate = true;
    }

    function addSpouse() {
      var spouseMember = null;
      for (var idx = 0; idx < vm.memberTypes.length; idx++) {
        if (vm.memberTypes[idx].memberTypeMeaning === 'SPOUSE') {
          spouseMember = vm.memberTypes[idx];
          break;
        }
      }

      vm.membership.spouse = {
        active: true,
        memberType: spouseMember
      };
    }

    function removeSpouse() {
      if (vm.membership.spouse.memberUID > 0) {
        vm.membership.spouse.active = false;
      } else {
        vm.membership.spouse = null;
      }
    }

    function addCounty() {
      vm.membership.membershipCounties.push({
        netMineralAcres: 0,
        surfaceAcres: 0,
        active: true
      });
    }

    function removeCounty(county) {
      if (county.membershipCountyUID > 0) {
        county.active = false;
      } else {
        var idx = vm.membership.membershipCounties.indexOf(county);
        vm.membership.membershipCounties.splice(idx, 1);
      }
    }

    function validateVotingCounty(membership, membershipCounty) {
      if (membershipCounty.votingCounty) {
        angular.forEach(membership.membershipCounties, function(cnty) {
          cnty.votingCounty = false;
        });

        membershipCounty.votingCounty = true;
      } else {
        membershipCounty.votingCounty = false;
      }
    }

    function addMember() {
      var familyMember = null;
      for (var idx = 0; idx < vm.memberTypes.length; idx++) {
        if (vm.memberTypes[idx].memberTypeMeaning === 'FAMILY_MEMBER') {
          familyMember = vm.memberTypes[idx];
          break;
        }
      }

      vm.membership.members.push({
        active: true,
        memberType: familyMember
      });
    }

    function removeMember(member) {
      if (member.memberUID > 0) {
        member.active = false;
      } else {
        var idx = vm.membership.members.indexOf(member);
        vm.membership.members.splice(idx, 1);
      }
    }

    function generateOwnerId(member) {
      var firstName  = member.person.firstName;
      var lastName   = member.person.lastName;
      var ownerIdent = member.ownerIdent;

      if (firstName  && firstName.length > 2 &&
          lastName   && lastName.length > 2 &&
          (!ownerIdent || ownerIdent.length == 0)) {

        membershipService.generateOwnerId(firstName, lastName).then(function(response) {
          if (response.status == 200) {
            member.ownerIdent = response.data;
          }
        });
      }
    }

    function hasChanges(membership) {
      return angular.equals(membership, vm.original);
    }

    function cancelChanges() {
      if (vm.membership.membershipUID == 0) {
        vm.membership = null;
      } else {
        vm.membership = vm.original
      }

      vm.view = "listing";
    }

    function saveMembership() {
      membershipService.saveMembership(vm.membership).then(function(response) {
        if (response.status == 201) {
          if (vm.memberships) {
            vm.memberships.push(response.data);
          } else {
            vm.memberships = [response.data];
          }

          vm.membership = response.data;
          vm.view       = "listing";
          vm.created    = true;
        } else if (response.status == 200) {
          var idx = vm.memberships.indexOf(vm.membership);

          vm.memberships[idx] = response.data;
          vm.membership       = response.data;
          vm.view             = "listing";
          vm.updated          = true;
        }
      });
    }

    function canClose() {
      if (vm.closeReason) {
        return (vm.closeReason.codeValueUID > 0);
      } else {
        return false;
      }
    }

    function toggleShowCalculations() {
      vm.showCalculations = !vm.showCalculations;
    }

    function loadAll() {
      if (vm.fullyLoaded) {
        return
      }

      vm.states = contactService.getStates();

      codeSetService.getCodeValuesForCodeSet('ENTITY_TYPE/').then(function(response) {
        if (responseSuccessful(response)) {
          vm.entityTypes = response.data;
        }
      });

      codeSetService.getCodeValuesForCodeSet('TITLE').then(function(response) {
        if (responseSuccessful(response)) {
          vm.titles = response.data;
        }
      });

      codeSetService.getCodeValuesForCodeSet('ADDRESS_TYPE').then(function(response) {
        if (responseSuccessful(response)) {
          vm.addressTypes = response.data;
        }
      });

      codeSetService.getCodeValuesForCodeSet('PHONE_TYPE').then(function(response) {
        if (responseSuccessful(response)) {
          vm.phoneTypes = response.data;
        }
      });

      codeSetService.getCodeValuesForCodeSet('EMAIL_TYPE').then(function(response) {
        if (responseSuccessful(response)) {
          vm.emailTypes = response.data;
        }
      });

      $http.get('/api/membertypes').success(function(data) {
        vm.memberTypes = data;
      });

      $http.get('/api/counties/').success(function(data) {
        vm.counties = data;
      });

      vm.fullyLoaded = true
    }

    function addAddress(member) {
      contactService.addAddress(member);
    }

    function ensurePrimaryAddress(member, address) {
      contactService.ensurePrimaryAddress(member, address)
    }

    function removeAddress(member, address) {
      contactService.removeAddress(member, address)
    }

    function addPhone(member) {
      contactService.addPhone(member)
    }

    function ensurePrimaryPhone(member, phone) {
      contactService.ensurePrimaryPhone(member, phone);
    }

    function removePhone(member, phone) {
      contactService.removePhone(member, phone)
    }

    function addEmail(member) {
      contactService.addEmail(member);
    }

    function ensurePrimaryEmail(member, email) {
      contactService.ensurePrimaryEmail(member, email)
    }

    function removeEmail(member, email) {
      contactService.removeEmail(member, email)
    }

  }

})(window, window.angular, window.jQuery);

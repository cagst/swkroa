/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality needed for the Deposit pages.
 *
 * Author:  Craig Gaskill
 */
(function(window, angular, $) {
  'use strict';

  angular.module('swkroaApp').controller('DepositController', DepositController);

  DepositController.$inject = ['CodeSetService', 'DepositService', 'TransactionService'];

  function DepositController(codeSetService, depositService, transactionService) {
    var vm = this;

    $('#createdMessage').hide();
    $('#updatedMessage').hide();

    vm.view     = "listing";
    vm.deposit  = null;

    vm.isOpen = {
      depositDate: false
    };

    /********************************************
     * Define binding methods
     ********************************************/

    vm.addDeposit = addDeposit;
    vm.editDeposit = editDeposit;
    vm.selectDeposit = selectDeposit;
    vm.deleteDeposit = deleteDeposit;
    vm.cancelChanges = cancelChanges;
    vm.addInvoiceToDeposit = addInvoiceToDeposit;
    vm.removeInvoiceFromDeposit = removeInvoiceFromDeposit;
    vm.createTransactionEntry = createTransactionEntry;
    vm.addTransactionEntry = addTransactionEntry;
    vm.saveDeposit = saveDeposit;

    // Date-Picker Methods
    vm.openDepositDate = openDepositDate;

    activate();

    /********************************************
     * Implement Methods
     ********************************************/

    function activate() {
      depositService.getDeposits().then(function(response) {
        if (responseSuccessful(response)) {
          vm.deposits = response.data;
        }
      });

      codeSetService.getCodeValuesForCodeSet('TRANSACTION_ENTRY_TYPE').then(function(response) {
        if (responseSuccessful(response)) {
          vm.entryTypes = response.data;
        }
      });
    }

    function addDeposit() {
      depositService.getDeposit(0).then(function(response) {
        if (responseSuccessful(response)) {
          vm.deposit = response.data;
          vm.view = "add";
        }
      });

      transactionService.getUnpaidTransactions().then(function(response) {
        if (responseSuccessful(response)) {
          vm.unpaid = response.data;
        }
      });
    }

    function editDeposit(deposit) {
      $('#createdMessage').hide();
      $('#updatedMessage').hide();

      depositService.getDeposit(deposit.depositUID).then(function(reponse) {
        if (responseSuccessful(reponse)) {
          vm.deposit  = reponse.data;

          // need to add the paidAmount to the deposit transaction so when it is removed it can be removed from the total
          for (var idx = 0; idx < vm.deposit.transactions.length; idx++) {
            vm.deposit.transactions[idx].amountPaid = vm.deposit.transactions[idx].transactionAmount;
            vm.deposit.transactions[idx].amountRemaining = vm.deposit.transactions[idx].transactionAmount;
            vm.deposit.transactions[idx].transactionInDeposit = true;
          }

          vm.view = "edit";
        }
      });

      transactionService.getUnpaidTransactions().then(function(response) {
        if (responseSuccessful(response)) {
          vm.unpaid = response.data;

          // need to add the deposit transaction to the unpaid but mark it as included in deposit so if it is removed it will re-appear in the unpaid invoices section
          for (var idx = 0; idx < vm.deposit.transactions.length; idx++) {
            vm.unpaid.push(vm.deposit.transactions[idx]);
          }
        }
      });
    }

    function selectDeposit(deposit) {
      vm.deposit = deposit;
    }

    function deleteDeposit() {
    }

    function cancelChanges() {
      vm.view = "listing";
    }

    function addInvoiceToDeposit(transaction) {
      transaction.transactionInDeposit = true;

      var tx = angular.copy(transaction);
      tx.amountPaid = tx.amountRemaining;
      tx.amountRemaining = 0;

      // flip the polarity of the transaction entries
      for (var idx = 0; idx < tx.transactionEntries.length; idx++) {
        tx.transactionEntries[idx].transactionEntryAmount = Math.abs(tx.transactionEntries[idx].transactionEntryAmount);
      }

      vm.deposit.transactions.push(tx);
      vm.deposit.depositAmount += tx.amountPaid;
    }

    function removeInvoiceFromDeposit(transaction) {
      for (var idx1 = 0; idx1 < vm.unpaid.length; idx1++) {
        if (vm.unpaid[idx1].transactionUID === transaction.transactionUID) {
          vm.unpaid[idx1].transactionInDeposit = false;
          break;
        }
      }

      vm.deposit.depositAmount -= transaction.amountPaid;
      transaction.amountPaid = 0;

      if (transaction.depositTransactionUID) {
        transaction.active = false;
      } else {
        var idx2 = vm.deposit.transactions.indexOf(transaction);
        vm.deposit.transactions.splice(idx2, 1);
      }
    }

    function createTransactionEntry(transaction) {
      vm.selectedTransaction    = angular.copy(transaction);
      vm.selectedTransactionIdx = vm.deposit.transactions.indexOf(transaction);
      vm.newTransactionEntry    = {transactionEntryAmount: 0};

      $("#addEntry").modal('toggle');
    }

    function addTransactionEntry() {
      $('#addEntry').modal('hide');

      vm.selectedTransaction.transactionEntries.push(vm.newTransactionEntry);
      vm.selectedTransaction.amountPaid += vm.newTransactionEntry.transactionEntryAmount;
      vm.selectedTransaction.transactionAmount += vm.newTransactionEntry.transactionEntryAmount;

      vm.deposit.transactions[vm.selectedTransactionIdx] = angular.copy(vm.selectedTransaction);
      vm.deposit.depositAmount += vm.newTransactionEntry.transactionEntryAmount;
    }

    function saveDeposit() {
      depositService.saveDeposit(vm.deposit).then(function(response) {
        if (responseSuccessful(response)) {
          if (response.status === 201) {
            $('#createdMessage').show();
          } else if (response.status === 200) {
            $('#updatedMessage').show();
          }

          vm.view     = "listing";
          vm.deposit  = response.data;

          depositService.getDeposits().then(function(response) {
            if (responseSuccessful(response)) {
              vm.deposits = response.data;
            }
          });
        }
      });
    }

    function openDepositDate($event) {
      $event.preventDefault();
      $event.stopPropagation();

      vm.isOpen.depositDate = true;
    }

  }
})(window, window.angular, window.jQuery);

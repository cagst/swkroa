/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 *
 * Version: 1.0.0
 */

var swkroaApp = angular.module('swkroaApp', ['ui.bootstrap', 'ui.utils'])
                       .controller('swkroaController', ['$scope', '$http', function($scope, $http) {

//	$http.get('../svc/codeset/TRANSACTION_TYPE/').success(function(data) {
//		$scope.transactionTypes = data;
//	});

	$scope.queryKeydown = function($event) {
		if ($event.keyCode == 13) {
			$scope.getMemberships();
		}
	};

	$scope.getMemberships = function() {
		$scope.membership = null;

		var url = "../svc/memberships?q=";
		if ($scope.query && $scope.query.length > 0) {
			url = url + $scope.query;
		}

		$http.get(url).success(function(data) {
			$scope.memberships = data;
		});
	};
	
	$scope.getMembership = function(membershipUID) {
		$http.get('../svc/membership/' + membershipUID).success(function(data) {
			$scope.membership = data;
		});
	};
	
	$scope.dateOptions = {
		'year-format': "'yy'",
		'starting-day': 1,
		'show-weeks': false
	};

	$scope.removeMembership = function() {
		$scope.membership.active = false;
		$http.put("../svc/membership", $scope.membership).success(function(data) {
			$scope.membership = null;
		})
		.error(function(data) {
			// if we failed to save (remove) the membership
			// set it back to active
			// TODO: Need to add a message for the user
			$scope.membership.active = true;
		});
		$('#deleteMembership').modal('hide');
	};

	$scope.selectComment = function(comment) {
		$scope.commentIdx = $scope.membership.comments.indexOf(comment);
		$scope.comment = angular.copy(comment);
	};

	$scope.addComment = function() {
		$scope.commentIdx = -1;
		$scope.comment = {active: true, commentDate: new Date()};
	};

	$scope.saveComment = function() {
		$scope.comment.parentEntityName = "MEMBERSHIP";
		$scope.comment.parentEntityUID  = $scope.membership.membershipUID;
		
		$http.put("../svc/comments", $scope.comment).success(function(data) {
			if ($scope.commentIdx == -1) {
				$scope.membership.comments.push(data);
			} else {
				$scope.membership.comments[$scope.commentIdx] = data;
			}
		});
		$scope.comment = null;
		$('#modifyComment').modal('hide');
	};

	$scope.removeComment = function() {
		$scope.comment.active = false;
		$http.put("../svc/comments", $scope.comment).success(function(data) {
			$scope.membership.comments.splice($scope.commentIdx, 1);
		})
		.error(function(data) {
			// TODO: Need to add a message for the user
			$scope.comment.active = true;
			$scope.membership.comments[$scope.commentIdx] = $scope.comment;
		});
		$scope.comment = null;
		$('#deleteComment').modal('hide');
	};
	
	$scope.runningBalance = function(index) {
		var balance = 0;
		var selectedTransactions = $scope.membership.transactions.slice(0, index + 1);
		angular.forEach(selectedTransactions, function(transaction, idx) {
			balance += (transaction.transactionAmount - transaction.unrelatedAmount);
		});

		return balance;
	}
	
	$scope.deleteTransaction = function(transaction) {
		$scope.transactionIdx = $scope.membership.transactions.indexOf(transaction);
		$scope.transaction = angular.copy(transaction);
	};
	
	$scope.editTransaction = function(transaction) {
		$scope.transactionIdx = $scope.membership.transactions.indexOf(transaction);
		$scope.transaction = angular.copy(transaction);
		
//		syncTransactionType($scope);
//		syncMember($scope);
		
		$("#modifyTransaction").modal('toggle');
		
		if ($scope.transaction.transactionAmount < 0) {
			// it is a debit (-) for the Member
			$("#debitAmount").val(Math.abs($scope.transaction.transactionAmount));
			$("#creditAmount").val(null);
		} else {
			// it is a credit (+) / payment for the Member
			$("#debitAmount").val(null);
			$("#creditAmount").val(Math.abs($scope.transaction.transactionAmount));
		}
	};
	
	$scope.changedDebitAmount = function() {
		var debitAmt  = Math.abs($("#debitAmount").val());
		
		if (debitAmt  > 0) {
			$("#creditAmount").val(null)
		}
	}
	
	$scope.changedCreditAmount = function() {
		var creditAmt = Math.abs($("#creditAmount").val());
		
		if (creditAmt > 0) {
			$("#debitAmount").val(null);
		}
	}

	$scope.addTransaction = function() {
		$scope.transactionIdx = -1;
		$scope.transaction = {active: true, transactionDate: new Date()};
	};

	$scope.saveTransaction = function() {
		var debitAmt  = Math.abs($("#debitAmount").val());
		var creditAmt = Math.abs($("#creditAmount").val());
		
		$scope.transaction.membershipUID  = $scope.membership.membershipUID;
		if (debitAmt > 0) {
			$scope.transaction.transactionAmount = debitAmt * -1;
		} else if (creditAmt > 0) {
			$scope.transaction.transactionAmount = creditAmt;
		}
		
		$http.put("../svc/transaction", $scope.transaction).success(function(data) {
			if ($scope.transactionIdx == -1) {
				$scope.membership.transactions.push(data);
			} else {
				$scope.membership.transactions[$scope.transactionIdx] = data;
			}
		});
		$scope.transaction = null;
		$('#modifyTransaction').modal('hide');
	};

	$scope.removeTransaction = function() {
		$scope.transaction.active = false;
		$http.put("../svc/transaction", $scope.transaction).success(function(data) {
			$scope.membership.transactions.splice($scope.transactionIdx, 1);
		})
		.error(function(data) {
			// TODO: Need to add a message for the user
			$scope.transaction.active = true;
			$scope.membership.transactions[$scope.transactionIdx] = $scope.transaction;
		});
		$scope.transaction = null;
		$('#deleteTransaction').modal('hide');
	};
}]);

//var syncTransactionType = function(scope) {
//	for (var idx1 = 0; idx1 < scope.transactionTypes.length; idx1++) {
//		if (scope.transaction.transactionType.codeValueUID == scope.transactionTypes[idx1].codeValueUID) {
//			scope.transaction.transactionType = scope.transactionTypes[idx1];
//			break;
//		}
//	}
//}

//var syncMember = function(scope) {
//	for (var idx1 = 0; idx1 < scope.membership.allMembers.length; idx1++) {
//		if (scope.transaction.member.memberUID == scope.membership.allMembers[idx1].memberUID) {
//			scope.transaction.member = scope.membership.allMembers[idx1];
//		}
//	}
//}

var toggleTransactionDetails = function(transaction) {
  var img       = $(transaction).children()[0];
  var collapsed = $(img).hasClass("fa-plus");

  var parentDiv = $(transaction).parent();
  var parentCol = $(parentDiv).parent();
  var parentRow = $(parentCol).parent();

  if (collapsed) {
    $(parentRow).siblings().removeClass("hide");
    $(img).removeClass("fa-plus");
    $(img).addClass("fa-minus");
  } else {
    $(parentRow).siblings().addClass("hide");
    $(img).addClass("fa-plus");
    $(img).removeClass("fa-minus");
  }
};

$(document).on('shown.bs.modal', function (event) {
	$('[autofocus]', this).focus();
});

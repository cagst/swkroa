/**
 * (c) 2016 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for the Member pages.
 *
 * Author:  Craig Gaskill
 */
var toggleTransactionDetails = function(transaction) {
  var img       = $(transaction).children()[0];
  var collapsed = $(img).hasClass("fa-caret-right");

  var parentDiv = $(transaction).parent();
  var parentCol = $(parentDiv).parent();
  var parentRow = $(parentCol).parent();

  if (collapsed) {
    $(parentRow).siblings().removeClass("hide");
    $(img).removeClass("fa-caret-right");
    $(img).addClass("fa-caret-down");
  } else {
    $(parentRow).siblings().addClass("hide");
    $(img).addClass("fa-caret-right");
    $(img).removeClass("fa-caret-down");
  }
};

/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for submitting reports.
 *
 * Author:  Craig Gaskill
 */

function submitReportForm(reportType, altAction) {
  $("#reportType").val(reportType);

  if (altAction && altAction !== 'undefined') {
    document.reportForm.action = document.reportForm.getAttribute(altAction);
  }

  document.reportForm.submit();
}

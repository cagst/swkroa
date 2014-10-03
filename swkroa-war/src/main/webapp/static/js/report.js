/**
 * (c) 2014 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Provides functionality for submitting reports.
 *
 * Author:  Craig Gaskill
 * Version: 1.0.0
 */

function submitReportForm(reportType) {
  $("#reportType").val(reportType);
  document.reportForm.submit();
}

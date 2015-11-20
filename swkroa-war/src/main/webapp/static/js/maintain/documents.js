/**
 * (c) 2015 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 */

enableUploadDocument = function() {
  var description  = $('#documentDesc').val();
  var documentType = $('#documentTypeUID').val();
  var beginDate    = $('#beginDate').val();
  var uploadFile   = $("#uploadFile").val();

  var disabled = !(description.length > 0 && documentType.length > 0 && beginDate.length > 0 && uploadFile.length > 0);

  $('#uploadDocumentButton').prop('disabled', disabled);
};
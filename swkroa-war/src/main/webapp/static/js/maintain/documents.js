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

displayEditDialog = function(el) {
  var parentRow = $(el).closest("tr");
  var fields    = $(parentRow).find("td");
  var idx       = 0;

  $.each(fields, function() {
    var value = $(this).text();

    if (idx === 0) {
      $('#documentUID').val($(this).attr('id'));
      $('#editDocumentDesc').val(value);
    } else if (idx === 1) {
      $('#editDocumentTypeUID').val($(this).attr('id'));
    } else if (idx === 3) {
      value = $(this).attr('name');
      $('#editBeginDate').val(value);
    } else if (idx === 4) {
      value = $(this).attr('name');
      $('#editEndDate').val(value);
    }

    idx++;
  });

};

enableSaveDocument = function() {
  var description  = $('#editDocumentDesc').val();
  var documentType = $('#editDocumentTypeUID').val();
  var beginDate    = $('#editBeginDate').val();

  var disabled = !(description.length > 0 && documentType.length > 0 && beginDate.length > 0);

  $('#saveDocumentButton').prop('disabled', disabled);
};

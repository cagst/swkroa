/**
 * (c) 2015 CAGST Solutions: http://www.cagst.com/solutions
 *
 * Author: Craig Gaskill
 */

enableUploadDocument = function() {
  const description  = $('#documentDesc').val();
  const documentType = $('#documentTypeUID').val();
  const beginDate    = $('#beginDate').val();
  const uploadFile   = $("#uploadFile").val();

  const disabled = !(description.length > 0 && documentType.length > 0 && beginDate.length > 0 && uploadFile.length > 0);

  $('#uploadDocumentButton').prop('disabled', disabled);
};

displayEditDialog = function(el) {
  const parentRow = $(el).closest("tr");
  const fields    = $(parentRow).find("td");
  let   idx       = 0;

  $.each(fields, function() {
    let value = $(this).text();

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
  const description  = $('#editDocumentDesc').val();
  const documentType = $('#editDocumentTypeUID').val();
  const beginDate    = $('#editBeginDate').val();

  const disabled = !(description.length > 0 && documentType.length > 0 && beginDate.length > 0);

  $('#saveDocumentButton').prop('disabled', disabled);
};

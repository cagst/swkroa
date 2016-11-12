package com.cagst.swkroa.controller.web;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cagst.swkroa.codevalue.CodeSetType;
import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.document.Document;
import com.cagst.swkroa.document.DocumentRepository;
import com.cagst.swkroa.exception.ResourceNotFoundException;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles and retrieves the CodeSet pages depending on the URI template.
 *
 * @author Craig Gaskill
 */
@Controller
@RequestMapping(value = "/maintain")
@RolesAllowed("ROLE_ADMIN")
public class MaintenanceController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MaintenanceController.class);

  private DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

  private final DocumentRepository documentRepository;
  private final CodeValueRepository codeValueRepository;

  /**
   * Primary Constructor used to create an instance of <i>MaintenanceController</i>.
   *
   * @param documentRepository
   *    The {@link DocumentRepository} to use to retrieve / persist Documents.
   * @param codeValueRepository
   *    The {@link CodeValueRepository} to use to retrieve codified values.
   */
  @Inject
  public MaintenanceController(DocumentRepository documentRepository, CodeValueRepository codeValueRepository) {
    this.documentRepository = documentRepository;
    this.codeValueRepository = codeValueRepository;
  }

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(Date.class, new CustomDateEditor(DATE_FORMAT, true));
  }

  /**
   * Handles and retrieves the CodeSet page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "codeset", method = RequestMethod.GET)
  public String getMaintainCodeSetsPage() {
    LOGGER.info("Received request to show codesets page.");

    return "maintain/codeset";
  }

  /**
   * Handles and retrieves the Documents page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "documents", method = RequestMethod.GET)
  public ModelAndView getMaintainDocumentsPage() {
    LOGGER.info("Received request to show the documents page.");

    ModelAndView mav = new ModelAndView("maintain/documents");
    mav.addObject("documents", documentRepository.getGlobalDocuments());
    mav.addObject("documentTypes", codeValueRepository.getCodeValuesForCodeSetByType(CodeSetType.DOCUMENT_TYPE));

    return mav;
  }

  /**
   * Handles and retrieve a Single Document to view.
   *
   * @param documentId
   *    A {@link long} that uniquely identifies the Document to retrieve.
   *
   * @return A {@link ResponseEntity} that contains the document for display.
   */
  @RequestMapping(value = "documents/{documentId}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> viewDocument(@PathVariable("documentId") long documentId) {
    LOGGER.info("Received request to view the document [{}]", documentId);

    Document document = documentRepository.getDocumentByUID(documentId);
    if (document == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    MediaType mediaType;
    try {
      mediaType = MediaType.parseMediaType(document.getDocumentFormat());
    } catch (InvalidMediaTypeException ex) {
      LOGGER.warn("Unable to parse document format [{}] as media type.", document.getDocumentFormat());

      mediaType = MediaType.ALL;
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(mediaType);

    return new ResponseEntity<>(document.getDocumentContents(), headers, HttpStatus.OK);
  }

  /**
   * Handles and retrieves the file to upload as a Document.
   *
   * @param uploadFile
   *    The {@link MultipartFile File} to upload.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "documents/upload", method = RequestMethod.POST)
  public ModelAndView uploadDocument(
      @RequestParam("uploadFile") MultipartFile uploadFile,
      @RequestParam("documentDesc") String documentDescription,
      @RequestParam("documentTypeUID") long documentTypeUID,
      @RequestParam("beginDate") Date beginDate,
      @RequestParam("endDate") Date endDate) {

    LOGGER.info("Received request to upload the file.");

    CodeValue documentType = codeValueRepository.getCodeValueByUID(documentTypeUID);

    boolean uploadSucceeded = true;
    try {
      Document uploadDocument = new Document();
      uploadDocument.setDocumentDescription(documentDescription);
      uploadDocument.setDocumentType(documentType);
      uploadDocument.setDocumentName(uploadFile.getOriginalFilename());
      uploadDocument.setDocumentFormat(uploadFile.getContentType());
      uploadDocument.setDocumentContents(uploadFile.getBytes());
      uploadDocument.setBeginEffectiveDate(new DateTime(beginDate));
      uploadDocument.setEndEffectiveDate(endDate != null ? new DateTime(endDate) : null);

      documentRepository.saveDocument(uploadDocument, WebAppUtils.getUser());
    } catch (IOException ex) {
      LOGGER.error("Unable to upload file [{}]", uploadFile.getName());
      uploadSucceeded = false;
    }

    ModelAndView mav = new ModelAndView("maintain/documents");
    mav.addObject("documents", documentRepository.getGlobalDocuments());
    mav.addObject("uploadSucceeded", uploadSucceeded);

    return mav;
  }

  @RequestMapping(value = "documents", method = RequestMethod.POST)
  public ModelAndView saveDocument(
      @RequestParam("documentUID") long documentId,
      @RequestParam("editDocumentDesc") String documentDescription,
      @RequestParam("editDocumentTypeUID") long documentTypeUID,
      @RequestParam("editBeginDate") Date beginDate,
      @RequestParam("editEndDate") Date endDate) {

    LOGGER.info("Received request to save document [{}]", documentId);
    Document document = documentRepository.getDocumentByUID(documentId);
    if (document == null) {
      throw new ResourceNotFoundException("Unable to find document [" + documentId + "]");
    }

    CodeValue documentType = codeValueRepository.getCodeValueByUID(documentTypeUID);

    document.setDocumentDescription(documentDescription);
    document.setDocumentType(documentType);
    document.setBeginEffectiveDate(new DateTime(beginDate));
    document.setEndEffectiveDate(endDate != null ? new DateTime(endDate) : null);

    documentRepository.saveDocument(document, WebAppUtils.getUser());

    ModelAndView mav = new ModelAndView("maintain/documents");
    mav.addObject("documents", documentRepository.getGlobalDocuments());

    return mav;
  }

  /**
   * Handles and retrieves the Member Types page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "membertype", method = RequestMethod.GET)
  public String getMaintainMemberTypesPage() {
    LOGGER.info("Received request to show member types page.");

    return "maintain/membertype";
  }

  /**
   * Handles and retrieves the User Home / Listing page.
   *
   * @return The location and name of the page template.
   */
  @RequestMapping(value = "users", method = RequestMethod.GET)
  public String getUsersPage() {
    LOGGER.info("Received request to show users listing page");

    return "maintain/user";
  }
}

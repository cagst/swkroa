package com.cagst.swkroa.controller.web;

import com.cagst.swkroa.document.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;

/**
 * Handles and retrieves the CodeSet pages depending on the URI template.
 *
 * @author Craig Gaskill
 */
@Controller
@RequestMapping(value = "/maintain")
public final class MaintenanceController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MaintenanceController.class);

  private final DocumentRepository documentRepository;

  /**
   * Primary Constructor used to create an instance of <i>MaintenanceController</i>.
   *
   * @param documentRepository
   *    The {@link DocumentRepository} to use to retrieve / persist Documents.
   */
  @Inject
  public MaintenanceController(final DocumentRepository documentRepository) {
    this.documentRepository = documentRepository;
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

package com.cagst.swkroa.controller.api;

import com.cagst.swkroa.county.County;
import com.cagst.swkroa.county.CountyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

/**
 * Handles and retrieves {@link Country} objects depending on the URI template.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Controller
public final class CountyApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(CountyApiController.class);

  @Autowired
  private CountyRepository countyRepo;

  /**
   * Handles the request and retrieves the active Counties within the system.
   *
   * @return A JSON representation of the active Counties within the system.
   */
  @RequestMapping(value = {"/api/counties"}, method = RequestMethod.GET)
  @ResponseBody
  public List<County> getActiveCounties() {
    LOGGER.info("Received request to retrieve active counties.");

    List<County> counties = (List<County>) countyRepo.getActiveCounties();
    Collections.sort(counties);

    return counties;
  }
}

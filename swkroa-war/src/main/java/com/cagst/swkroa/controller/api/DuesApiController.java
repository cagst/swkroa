package com.cagst.swkroa.controller.api;

import javax.inject.Inject;

import java.util.List;

import com.cagst.swkroa.dues.DuesRepository;
import com.cagst.swkroa.dues.DuesRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles and retrieves {@link DuesRun} objects depending on the URI template.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping("/api/dues")
public class DuesApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(DuesApiController.class);

  private final DuesRepository duesRepo;

  @Inject
  public DuesApiController(final DuesRepository duesRepo) {
    this.duesRepo = duesRepo;
  }

  /**
   * Handles the request and retrieves the Dues Runs within the system.
   *
   * @return A JSON representation of the Dues Runs within the system.
   */
  @RequestMapping(method = RequestMethod.GET)
  public List<DuesRun> getBillingRuns() {
    return duesRepo.getDuesRuns();
  }
}

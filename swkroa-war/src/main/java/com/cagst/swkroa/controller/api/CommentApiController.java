package com.cagst.swkroa.controller.api;

import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.comment.CommentRepository;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles and retrieves {@link Comment} objects depending on the URI template.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Controller
public final class CommentApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommentApiController.class);

  @Autowired
  private CommentRepository commentRepo;

  /**
   * Handles the request and persists the {@link Comment} to persistent storage.
   *
   * @param comment
   *     The {@link Comment} to persist.
   *
   * @return The {@link Comment} after it has been persisted.
   */
  @RequestMapping(value = "/api/comments", method = RequestMethod.PUT)
  @ResponseBody
  public Comment saveComment(final @RequestBody Comment comment) {
    LOGGER.info("Received request to save comment.");

    return commentRepo.saveComment(comment, WebAppUtils.getUser());
  }
}

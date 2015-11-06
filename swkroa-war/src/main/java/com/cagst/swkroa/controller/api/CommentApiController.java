package com.cagst.swkroa.controller.api;

import javax.inject.Inject;

import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.comment.CommentRepository;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles and retrieves {@link Comment} objects depending on the URI template.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping(value = "/api/comments")
public final class CommentApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommentApiController.class);

  private final CommentRepository commentRepo;

  @Inject
  public CommentApiController(final CommentRepository commentRepo) {
    this.commentRepo = commentRepo;
  }

  /**
   * Handles the request and persists the {@link Comment} to persistent storage.
   *
   * @param comment
   *     The {@link Comment} to persist.
   *
   * @return The {@link Comment} after it has been persisted.
   */
  @RequestMapping(method = RequestMethod.PUT)
  public Comment saveComment(final @RequestBody Comment comment) {
    LOGGER.info("Received request to save comment.");

    return commentRepo.saveComment(comment, WebAppUtils.getUser());
  }
}

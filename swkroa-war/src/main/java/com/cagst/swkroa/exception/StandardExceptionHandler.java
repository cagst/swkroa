package com.cagst.swkroa.exception;

import com.cagst.swkroa.model.ErrorEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * An exception handler class that will generate common error messages to be sent back for {@link RestController}.
 *
 * @author Craig Gaskill
 */
@ControllerAdvice(annotations = RestController.class)
public class StandardExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(StandardExceptionHandler.class);

  @ExceptionHandler(BadRequestException.class)
  protected ResponseEntity<ErrorEntity> handleBadRequest(final RuntimeException ex, final WebRequest request) {
    ErrorEntity error = new ErrorEntity(HttpStatus.BAD_REQUEST.toString(), ex.getLocalizedMessage());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(OptimisticLockingFailureException.class)
  protected ResponseEntity<ErrorEntity> handleOptimisticLockingFailure(final RuntimeException ex, final WebRequest request) {
    ErrorEntity error = new ErrorEntity(HttpStatus.CONFLICT.toString(), ex.getLocalizedMessage());

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorEntity> handleInternalException(final RuntimeException ex, final WebRequest request) {
    ErrorEntity error = new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getLocalizedMessage());

    LOGGER.error(ex.getMessage(), ex);

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

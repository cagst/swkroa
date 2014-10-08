package com.cagst.swkroa.exception;

import com.cagst.swkroa.model.ErrorEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * An exception handler class that will generate a common error message to be sent back.
 *
 * @author Craig Gaskill
 */
@ControllerAdvice
public class StandardExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(BadRequestException.class)
  protected ResponseEntity<ErrorEntity> handleBadRequest(final RuntimeException ex, final WebRequest request) {
    ErrorEntity error = new ErrorEntity(HttpStatus.BAD_REQUEST.toString(), ex.getLocalizedMessage());

    return new ResponseEntity<ErrorEntity>(error, HttpStatus.BAD_REQUEST);
  }
}

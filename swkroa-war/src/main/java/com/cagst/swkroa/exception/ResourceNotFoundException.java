package com.cagst.swkroa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception to be thrown when the requested resource would not be located.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException() {
    super();
  }

  public ResourceNotFoundException(java.lang.String msg) {
    super(msg);
  }

  public ResourceNotFoundException(java.lang.String msg, java.lang.Throwable throwable) {
    super(msg, throwable);
  }

  public ResourceNotFoundException(java.lang.Throwable throwable) {
    super(throwable);
  }
}

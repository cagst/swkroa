package com.cagst.swkroa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception to be thrown when the requested resource could not be found.
 *
 * @author Craig Gaskill
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

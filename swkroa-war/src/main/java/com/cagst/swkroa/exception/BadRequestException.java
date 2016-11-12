package com.cagst.swkroa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception to be thrown when the request was invalid.
 *
 * @author Craig Gaskill
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
  public BadRequestException() {
    super();
  }

  public BadRequestException(java.lang.String msg) {
    super(msg);
  }

  public BadRequestException(java.lang.String msg, java.lang.Throwable throwable) {
    super(msg, throwable);
  }

  public BadRequestException(java.lang.Throwable throwable) {
    super(throwable);
  }
}

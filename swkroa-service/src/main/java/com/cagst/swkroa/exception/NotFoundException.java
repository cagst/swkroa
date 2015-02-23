package com.cagst.swkroa.exception;

/**
 * An exception to be thrown when a requested object could not be found.
 */
public class NotFoundException extends RuntimeException {
  public NotFoundException() {
    super();
  }

  public NotFoundException(java.lang.String msg) {
    super(msg);
  }

  public NotFoundException(java.lang.String msg, java.lang.Throwable throwable) {
    super(msg, throwable);
  }

  public NotFoundException(java.lang.Throwable throwable) {
    super(throwable);
  }
}

package com.cagst.swkroa.model;

/**
 * Defines the standard error response that is sent back as the response body.
 */
public class ErrorEntity {
  private final String code;
  private final String msg;

  public ErrorEntity(final String code, final String msg) {
    this.code = code;
    this.msg = msg;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return msg;
  }
}

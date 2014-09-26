package com.cagst.swkroa.audit.annotation;

import com.cagst.swkroa.audit.AuditEventType;

import java.lang.annotation.*;

/**
 * This class annotates a method for auditing.
 *
 * @author Craig Gaskill.
 * @version 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Auditable {
  public static final String ACTION_SIGNIN_SUCCESSFUL = "SIGNIN_SUCCESSFUL";
  public static final String ACTION_SIGNIN_FAILURE    = "SIGNIN_FAILURE";
  public static final String ACTION_PASSWORD_CHANGED  = "PASSWORD_CHANGED";
  public static final String ACTION_PASSWORD_RESET    = "PASSWORD_RESET";
  public static final String ACTION_ACCOUNT_LOCKED    = "ACCOUNT_LOCKED";
  public static final String ACTION_ACCOUNT_UNLOCKED  = "ACCOUNT_UNLOCKED";
  public static final String ACTION_SIGNED_OUT        = "SIGNED_OUT";
  public static final String ACTION_TIMED_OUT         = "TIMED_OUT";
  public static final String ACTION_ACCOUNT_ENABLED   = "ACCOUNT_ENABLED";
  public static final String ACTION_ACCOUNT_DISABLED  = "ACCOUNT_DISABLED";

  AuditEventType eventType();

  String action();

  String message() default "";
}

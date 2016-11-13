package com.cagst.swkroa.audit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cagst.swkroa.audit.AuditEventType;

/**
 * This class annotates a method for auditing.
 *
 * @author Craig Gaskill.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Auditable {
  String ACTION_SIGNIN_SUCCESSFUL = "SIGNIN_SUCCESSFUL";
  String ACTION_SIGNIN_FAILURE = "SIGNIN_FAILURE";
  String ACTION_PASSWORD_CHANGED = "PASSWORD_CHANGED";
  String ACTION_PASSWORD_RESET = "PASSWORD_RESET";
  String ACTION_ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
  String ACTION_ACCOUNT_UNLOCKED = "ACCOUNT_UNLOCKED";
  String ACTION_SIGNED_OUT = "SIGNED_OUT";
  String ACTION_TIMED_OUT = "TIMED_OUT";
  String ACTION_ACCOUNT_ENABLED = "ACCOUNT_ENABLED";
  String ACTION_ACCOUNT_DISABLED = "ACCOUNT_DISABLED";

  AuditEventType eventType();

  String action();

  String message() default "";
}

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
 * 
 * @version 1.0.0
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Auditable {
	public static final String ACTION_SIGNIN_ATTEMP = "SIGNIN_ATTEMPT";
	public static final String ACTION_SIGNIN_SUCCESSFUL = "SIGNIN_SUCCESSFUL";
	public static final String ACTION_SIGNIN_FAILURE = "SIGNIN_FAILURE";
	public static final String ACTION_PASSWORD_CHANGED = "PASSWORD_CHANGED";
	public static final String ACTION_ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
	public static final String ACTION_ACCOUNT_UNLOCKED = "ACCOUNT_UNLOCKED";
	public static final String ACTION_SIGNED_OUT = "SIGNED_OUT";
	public static final String ACTION_TIMED_OUT = "TIMED_OUT";

	AuditEventType eventType();

	String action();

	String message() default "";
}

package com.cagst.swkroa.aop;

import com.cagst.swkroa.audit.annotation.Auditable;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * This class contains all AOP {@link Pointcut Pointcuts} defines used throughout the system.
 *
 * @author Craig Gaskill
 */
@Aspect
public final class SystemPointcuts {
  @Pointcut("@annotation(auditable)")
  public void auditableMethod(final Auditable auditable) {
  }
}

package com.cagst.swkroa.aop;

import com.cagst.swkroa.audit.annotation.Auditable;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains all AOP {@link Pointcut Pointcuts} defines used throughout the system.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Aspect
public final class SystemPointcuts {
  private static final Logger logger = LoggerFactory.getLogger(SystemPointcuts.class);

  @Pointcut("@annotation(auditable)")
  public void auditableMethod(final Auditable auditable) {
    logger.debug("Calling auditableMethod.");
  }
}

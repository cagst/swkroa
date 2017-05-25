package com.cagst.swkroa.config;

import javax.inject.Inject;

import com.cagst.swkroa.aop.AuditableAnnotationAspect;
import com.cagst.swkroa.audit.AuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration class for the system's AOP framework.
 *
 * @author Craig Gaskill
 */
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
  @Bean
  @Inject
  public AuditableAnnotationAspect getAuditableAspect(AuditEventRepository auditRepository) {
    return new AuditableAnnotationAspect(auditRepository);
  }
}

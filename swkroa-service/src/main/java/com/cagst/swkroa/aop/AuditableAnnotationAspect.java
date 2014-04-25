package com.cagst.swkroa.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.intercept.Joinpoint;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cagst.swkroa.audit.AuditEvent;
import com.cagst.swkroa.audit.AuditEventRepository;
import com.cagst.swkroa.audit.AuditEventType;
import com.cagst.swkroa.audit.annotation.AuditInstigator;
import com.cagst.swkroa.audit.annotation.AuditMessage;
import com.cagst.swkroa.audit.annotation.Auditable;
import com.cagst.swkroa.user.User;

/**
 * The AOP {@link Aspect} for the {@link Auditable} annotation.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
@Aspect
public final class AuditableAnnotationAspect {
	private static final Logger logger = LoggerFactory.getLogger(AuditableAnnotationAspect.class);

	private final AuditEventRepository auditRepo;

	/**
	 * Primary Constructor used to create an instance of <i>AuditEventRepository</i>.
	 * 
	 * @param auditRepo
	 *          The {@link AuditEventRepository} to use to log {@link AuditEvent AuditEvents}.
	 */
	public AuditableAnnotationAspect(final AuditEventRepository auditRepo) {
		this.auditRepo = auditRepo;
	}

	/**
	 * The {@link JoinPoint} for the {@link Auditable} annotation {@link Pointcut}. This method will
	 * log the actual {@link AuditEvent} for the parameters specified in the {@link Auditable}
	 * annotation.
	 * 
	 * @param joinPoint
	 *          The {@link Joinpoint} for the {@link Auditable} annotation.
	 * @param auditable
	 *          The {@link Auditable} annotation that caused the {@link Pointcut}.
	 */
	@Before("com.cagst.swkroa.aop.SystemPointcuts.auditableMethod(auditable)")
	public void logAuditEvent(final JoinPoint joinPoint, final Auditable auditable) {
		logger.info("Calling logAuditEvent.");

		AuditEventType eventType = auditable.eventType();
		String action = auditable.action();
		String message = auditable.message();
		String instigator = null;
		User user = null;

		if (joinPoint.getSignature() != null && (joinPoint.getSignature() instanceof MethodSignature)) {
			MethodSignature sig = (MethodSignature) joinPoint.getSignature();
			Method method = sig.getMethod();
			Object[] objs = joinPoint.getArgs();

			try {
				Annotation[][] annotations = joinPoint.getTarget().getClass()
						.getMethod(method.getName(), method.getParameterTypes()).getParameterAnnotations();
				for (int idx1 = 0; idx1 < annotations.length; idx1++) {
					Annotation[] paramAnnotations = annotations[idx1];

					for (int idx2 = 0; idx2 < paramAnnotations.length; idx2++) {
						Annotation paramAnnotation = paramAnnotations[idx2];

						if (paramAnnotation instanceof AuditInstigator) {
							if (objs[idx1] instanceof User) {
								user = (User) objs[idx1];
								instigator = user.getUsername();
							} else {
								instigator = objs[idx1].toString();
							}
						} else if (paramAnnotation instanceof AuditMessage) {
							message = objs[idx1].toString();
						}
					}
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}

		// retrieve the user that is currently logged in (if applicable).
		SecurityContext secCtx = SecurityContextHolder.getContext();
		if (secCtx != null && secCtx.getAuthentication() != null) {
			Authentication auth = secCtx.getAuthentication();
			if (auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof User) {
				user = (User) auth.getPrincipal();
			}
		}

		// if the instigator is empty but we have the user currently logged in
		if (StringUtils.isEmpty(instigator) && user != null) {
			// set the instigator to the user currently logged in
			instigator = user.getUsername();
		}

		// the instigator is required so if it doesn't exist throw an exception
		if (StringUtils.isEmpty(instigator)) {
			throw new IllegalArgumentException(
					"Instigator not found for AuditEvent. Use @AuditInstigator to annotate the instigator.");
		}

		auditRepo.save(new AuditEvent(eventType, action, instigator, StringUtils.isEmpty(message) ? null : message));
	}
}

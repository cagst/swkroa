package com.cagst.swkroa;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

/**
 * Main entry point for the configuration of the MyLife WebApp (replaces the web.xml).
 *
 * @author Craig Gaskill
 */
public class WebAppInitializer extends AbstractDispatcherServletInitializer {
  @Override
  protected WebApplicationContext createServletApplicationContext() {
    AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
    webContext.scan("com.cagst.swkroa");

    return webContext;
  }

  @Override
  protected String[] getServletMappings() {
    return new String[] {"/"};
  }

  @Override
  protected WebApplicationContext createRootApplicationContext() {
    AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
    rootContext.scan("com.cagst.swkroa");

    return rootContext;
  }
}

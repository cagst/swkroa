package com.cagst.swkroa.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * Configuration class for the Spring MVC Framework.
 *
 * @author Craig Gaskill
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.cagst.swkroa")
public class WebMvcConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**")
        .addResourceLocations("/static/", "classpath:/static/");

    //        .setCacheControl(CacheControl.maxAge(15, TimeUnit.DAYS).cachePublic());
  }

  @Bean(name = "messageSource")
  public MessageSource getMessageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasenames(
        "properties/i18n/common",
        "properties/i18n/auth",
        "properties/i18n/membership",
        "properties/i18n/report",
        "properties/i18n/maintenance",
        "properties/i18n/user",
        "properties/i18n/accounting",
        "properties/i18n/member"
    );
    messageSource.setDefaultEncoding("UTF-8");

    return messageSource;
  }

  @Bean(name = "multipartResolver")
  public MultipartResolver getMultipartResolver() {
    return new CommonsMultipartResolver();
  }

  @Bean
  public ViewResolver getViewResolver() {
    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(getTemplateEngine());
    viewResolver.setCharacterEncoding("UTF-8");
    viewResolver.setCache(false);

    return viewResolver;
  }

  @Bean
  public SpringTemplateEngine getTemplateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(getTemplateResolver());
    templateEngine.addDialect(new SpringSecurityDialect());
    templateEngine.setMessageSource(getMessageSource());

    return templateEngine;
  }

  private ITemplateResolver getTemplateResolver() {
    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setPrefix("/WEB-INF/templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCacheable(false);

    return templateResolver;
  }
}

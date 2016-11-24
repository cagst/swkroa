package com.cagst.swkroa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Configuration class for environment properties.
 *
 * @author Craig Gaskill
 */
@Configuration
@PropertySource("classpath:environment.properties")
public class PropertyConfig {
  @Bean
  public PropertySourcesPlaceholderConfigurer getPropertyConfiguration() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}

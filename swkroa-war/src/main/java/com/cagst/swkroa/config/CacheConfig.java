package com.cagst.swkroa.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the system's Caching framework.
 *
 * @author Craig Gaskill
 */
@Configuration
@EnableCaching
public class CacheConfig {
  @Bean(name = "cacheManager")
  public CacheManager getCacheManager() {
    return new JCacheCacheManager();
  }
}

package com.cagst.swkroa.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class for the system's Datasource.
 *
 * @author Craig Gaskill
 */
@Configuration
@EnableTransactionManagement
public class DatasourceConfig {
  @Bean(name = "datasource")
  public DataSource getDatasource() {
    JndiDataSourceLookup jndiDatasource = new JndiDataSourceLookup();
    jndiDatasource.setResourceRef(true);

    return jndiDatasource.getDataSource("java:comp/env/jdbc/SWKROADataSource");
  }

  @Bean(name = "transactionManager")
  public PlatformTransactionManager getTransactionManager() {
    DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
    transactionManager.setDataSource(getDatasource());

    return transactionManager;
  }
}

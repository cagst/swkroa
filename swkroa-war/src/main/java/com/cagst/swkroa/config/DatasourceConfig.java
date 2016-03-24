package com.cagst.swkroa.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

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

  @Bean(name = "flyway")
  public Flyway getFlyway() {
    Flyway flyway = new Flyway();
    flyway.setBaselineOnMigrate(true);
    flyway.setDataSource(getDatasource());
    flyway.setValidateOnMigrate(false);

    return flyway;
  }
}

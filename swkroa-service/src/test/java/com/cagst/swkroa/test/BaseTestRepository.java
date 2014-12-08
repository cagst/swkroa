package com.cagst.swkroa.test;

import javax.sql.DataSource;

import com.cagst.common.db.DataSourceFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Abstract class that contains some common methods for testing repositories.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public abstract class BaseTestRepository {
  protected DataSource createTestDataSource() {
    Resource schemaLocation = new ClassPathResource("/testDb/schema.sql");
    Resource testDataLocation = new ClassPathResource("/testDb/test_data.sql");

    DataSourceFactory dsFactory = new DataSourceFactory("savedb", schemaLocation, testDataLocation);
    return dsFactory.getDataSource();
  }
}

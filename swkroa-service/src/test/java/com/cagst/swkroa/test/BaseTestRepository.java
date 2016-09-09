package com.cagst.swkroa.test;

import com.cagst.common.db.DataSourceFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

/**
 * Base class for all repository test classes that creates the common test database that can be shared by all repositories.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public abstract class BaseTestRepository {
  /**
   * Helper method that can be used to create the common test database for all repositories to use.
   *
   * @return A {@link DataSource} that represents the common test database after it has been created and loaded with test data.
   */
  protected DataSource createTestDataSource() {
    Resource schemaLocation = new ClassPathResource("/testDb/schema.sql");
    Resource altSchemaLocation = new ClassPathResource("/testDb/views.sql");
    Resource testDataLocation = new ClassPathResource("/testDb/test_data.sql");

    DataSourceFactory dsFactory = new DataSourceFactory("swkroadb", new Resource[]{schemaLocation, altSchemaLocation, testDataLocation});
    return dsFactory.getDataSource();
  }
}

package com.cagst.swkroa.test;

import javax.sql.DataSource;

import com.cagst.swkroa.DataSourceFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Base class for all repository test classes that creates the common test database that can be shared by all repositories.
 *
 * @author Craig Gaskill
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

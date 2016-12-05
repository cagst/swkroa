package com.cagst.swkroa;

import javax.sql.DataSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * A factory that creates a data source fit for use in a system test environment. Creates a simple
 * data source that connects to an in-memory database pre-loaded with test data.
 * <p/>
 * This factory returns a fully-initialized DataSource implementation. When the DataSource is
 * returned, callers are guaranteed that the database schema and test data will have been loaded by
 * that time.
 * <p/>
 * Is a FactoryBean, for exposing the fully-initialized test DataSource as a Spring bean. See
 * {@link #getObject()}.
 */
public class DataSourceFactory implements FactoryBean<DataSource> {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceFactory.class);

  /**
   * The {@link DataSource} created by this factory.
   */
  private DataSource dataSource;

  private final String testDatabaseName;
  private final Resource resources[];

  /**
   * Creates a new TestDataSourceFactory fully-initialized with what it needs to work. Fully-formed
   * constructors are nice in a programmatic environment, as they result in more concise code and
   * allow for a class to enforce its required properties.
   *
   * @param testDatabaseName
   *     The name of the test database to create
   */
  public DataSourceFactory(final String testDatabaseName, final Resource resources[]) {
    this.testDatabaseName = testDatabaseName;
    this.resources = resources;
  }

  @Override
  public DataSource getObject() throws Exception {
    return getDataSource();
  }

  @Override
  public Class<?> getObjectType() {
    return DataSource.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  /**
   * @return The {@link DataSource} created by this factory.
   */
  public DataSource getDataSource() {
    if (null == dataSource) {
      initializeDataSource();
    }

    return dataSource;
  }

  /**
   * Encapsulates the steps involved in initializing the data source.
   */
  private void initializeDataSource() {
    // create the in-memory database source first
    this.dataSource = createDataSource();

    // now populate the data source
    populateDataSource();
  }

  private DataSource createDataSource() {
    LOGGER.debug("Creating Test Data Source...");

    DriverManagerDataSource dataSource = new DriverManagerDataSource();

    // use the HSQLDB JDBC driver
    dataSource.setDriverClassName("org.hsqldb.jdbcDriver");

    // have it create an in-memory database
    dataSource.setUrl("jdbc:hsqldb:mem:" + testDatabaseName);
    dataSource.setUsername("sa");
    dataSource.setPassword("");

    return dataSource;
  }

  private void populateDataSource() {
    LOGGER.debug("Populating Test Data Source...");

    TestDatabasePopulator populator = new TestDatabasePopulator(dataSource);
    populator.populate();
  }

  /**
   * Populates an in-memory data source with test data.
   *
   * @author Craig Gaskill
   */
  private final class TestDatabasePopulator {
    private final DataSource dataSource;

    /**
     * Primary Constructor
     *
     * @param dataSource
     *     {@link DataSource} to populate data with.
     */
    public TestDatabasePopulator(final DataSource dataSource) {
      this.dataSource = dataSource;
    }

    public void populate() {
      try (Connection conn = dataSource.getConnection()) {
        for (Resource resource : resources) {
          processResource(conn, resource);
        }
      } catch (SQLException ex) {
        throw new RuntimeException("SQLException occurred", ex);
      }
    }

    private void processResource(final Connection conn, final Resource resource) throws SQLException {
      LOGGER.debug("Processing resource [{}]", resource.getFilename());

      try {
        String sql = parseSqlIn(resource);
        executeSql(conn, sql);
      } catch (IOException ex) {
        throw new RuntimeException("IOException occurred processing [" + resource.getFilename() + "]", ex);
      }
    }

    private String parseSqlIn(final Resource resource) throws IOException {
      try (InputStream is = resource.getInputStream()) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringWriter sw = new StringWriter();
        BufferedWriter writer = new BufferedWriter(sw);

        String line;
        while ((line = reader.readLine()) != null) {
          writer.write(line);
        }
        writer.flush();

        return sw.toString();
      }
    }

    private void executeSql(final Connection conn, final String sql) throws SQLException {
      Statement stmt = conn.createStatement();
      stmt.execute(sql);
    }
  }
}

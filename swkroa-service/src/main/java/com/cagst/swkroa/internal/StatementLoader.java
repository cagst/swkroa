package com.cagst.swkroa.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a utility class that loads the contents of an externalized SQL statement defined by the
 * given name and located under the "sql" sub-package under the package of the given class.
 * <p/>
 * <p/>
 * All resources loaded by the StatementLoader are relative to the package of the <code>owner</code>
 * class. The following sub-package structure is used:
 * <p/>
 * <ul>
 *     <li>sql -- Contains SQL statements common to supported databases</li>
 *     <li>sql/<code>type</code> -- Contains statements specific to a given database type such as "oracle".</li>
 * </ul>
 * <p/>
 * <p/>
 * Below are examples of an SQL resource location for general use and one for Oracle, respectively.
 * <p/>
 * <ul>
 * <li>sql/load_people.sql</li>
 * <li>sql/oracle/update_people.sql</li>
 * </ul>
 * <p/>
 * <p/>
 * In this example, the load_people SQL is common to all, but the update_people SQL is Oracle
 * specific.
 *
 * @author Craig Gaskill
 */
public final class StatementLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementLoader.class);

    /**
     * Class owning the resources to be loaded.
     */
    private final Class owner;

    /**
     * Dialect of the database for the statements.
     */
    private final StatementDialect dialect;

    /**
     * Map associating logical names with the corresponding statements.
     */
    private final Map<String, String> statements = new ConcurrentHashMap<>();

    /**
     * Flag indicating whether the dialect supports inline comments.
     */
    private final boolean supportsInlineComments;

    /**
     * Map associating owner/dialect tuples with StatementLoader instances.
     */
    private static Map<LoaderKey, StatementLoader> loaders = new ConcurrentHashMap<>();

    /**
     * Constructs a <code>StatementLoader</code> owned by the given class using the given SQL dialect.
     *
     * @param owner
     *     The {@link Class} that owns the <code>StatementLoader</code>.
     * @param dialect
     *     The {@link StatementDialect} dialect of the database for the statements.
     */
    private StatementLoader(final Class owner, final StatementDialect dialect) {
        this.owner = owner;
        this.dialect = dialect;
        this.supportsInlineComments = (dialect == StatementDialect.ORACLE);
    }

    /**
     * Returns an instance of the StatementLoader which creates statements for the type of the given
     * dialect.
     *
     * @param owner
     *     The {@link Class} that owns the resources to be loaded.
     * @param dialect
     *     The {@link StatementDialect} of the database for the statements.
     *
     * @return A <code>StatementLoader</code> for the specified owner/dialect.
     */
    public static StatementLoader getLoader(final Class owner, final StatementDialect dialect) {
        LoaderKey key = new LoaderKey(owner, dialect);

        synchronized (loaders) {
            StatementLoader loader = loaders.get(key);
            if (null == loader) {
                loader = new StatementLoader(owner, dialect);

                loaders.put(key, loader);
            }

            return loader;
        }
    }

    /**
     * Loads the text of the given SQL statement. The statement must be included as a
     * <code>&lt;name&gt;.sql</code> file in the SQL sub-package as the owning class.
     *
     * @param name
     *     The logical name of the statement.
     *
     * @return The SQL statement in String form.
     *
     * @throws MissingResourceException if the resource can not be loaded.
     */
    public String load(final String name) {
        synchronized (statements) {
            // determine if the statement is already loaded
            String stmt = statements.get(name);

            // if the statement has not been loaded, do so now
            if (null == stmt) {
                try {
                    stmt = readStatement(name, owner);
                } catch (MissingResourceException ex) {
                    // if our parent class isn't Object (we have derived from another class)
                    // then try the parent class
                    if (owner.getSuperclass() != Object.class) {
                        stmt = readStatement(name, owner.getSuperclass());
                    } else {
                        throw ex;
                    }
                }
                statements.put(name, stmt);
            }

            LOGGER.debug("Loaded statement [{}]\n{}", name, stmt);

            return stmt;
        }
    }

    /**
     * Returns the statement string with the given name.
     *
     * @param name
     *     The logical name of the statement.
     *
     * @return The SQL statement in String form.
     *
     * @throws MissingResourceException if the resource can not be loaded.
     */
    private String readStatement(final String name, final Class owner) {
        StringBuffer resourceName = new StringBuffer(32);
        resourceName.append("sql/").append(dialect.name().toLowerCase()).append("/").append(name).append(".sql");

        LOGGER.info("Loading statement from [{}]", resourceName);

        // First attempt to load from the dialect-specific location.
        InputStream stream = owner.getResourceAsStream(resourceName.toString());

        // If there was no .sql at that location, attempt to use the dialect-independent SQL package.
        if (null == stream) {
            resourceName = new StringBuffer(32);
            resourceName.append("sql/").append(name).append(".sql");

            LOGGER.info("Loading statement from [{}]", resourceName.toString());

            // Attempt to load the resource of the given name.
            stream = owner.getResourceAsStream(resourceName.toString());

            // If the resource still could not be found, throw an exception
            if (null == stream) {
                throw new MissingResourceException("Unable to load SQL resource [" + name + "]", owner.getName(), name);
            }
        }

        // The SQL stream was found, read in the content
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer(128);

        // If inline comments are supported, insert comment for instrumentation.
        if (supportsInlineComments) {
            // This comment structure is intended for simple use with existing SQL monitoring utilities.
            buffer.append("/*+ JDBC<");
            buffer.append(owner.getName().toUpperCase(Locale.US));
            buffer.append(">*/ ");
        }

        // Read the .sql file.
        try {
            String line = reader.readLine();
            while (null != line) {
                buffer.append(line);

                // preserve the new line in the externalized SQL
                buffer.append("\n");

                line = reader.readLine();
            }
        } catch (IOException ex) {
            // No IOException should occur since we are reading an internal resource, but propagate the
            // exception anyway.
            throw new RuntimeException("Unable to load SQL resources" + owner.getResource(name), ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                // Ignore any IOException as we are closing the reader.
            }
        }

        return buffer.toString();
    }

    /**
     * Simple object used as a key to look up existing StatementLoaders.
     *
     * @author Craig Gaskill
     */
    private static class LoaderKey {
        /**
         * Class owning the resources to be loaded.
         */
        private final Class owner;

        /**
         * Dialect of the database for the statements.
         */
        private final StatementDialect dialect;

        private LoaderKey(final Class owner, final StatementDialect dialect) {
            this.owner = owner;
            this.dialect = dialect;
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof LoaderKey)) {
                return false;
            }

            LoaderKey key = (LoaderKey) obj;

            return owner.equals(key.owner) && dialect.equals(key.dialect);
        }

        @Override
        public int hashCode() {
            return 23 * owner.hashCode() * dialect.hashCode();
        }
    }
}

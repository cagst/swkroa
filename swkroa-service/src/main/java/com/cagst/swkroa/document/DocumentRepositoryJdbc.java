package com.cagst.swkroa.document;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.filesystem.FileDTO;
import com.cagst.swkroa.filesystem.FileSystem;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.user.User;
import net.sf.ehcache.transaction.xa.OptimisticLockFailureException;
import org.apache.commons.vfs2.FileSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A JDBC Implementation of the {@link DocumentRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("documentRepo")
/* package */ class DocumentRepositoryJdbc extends BaseRepositoryJdbc implements DocumentRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentRepositoryJdbc.class);

  private static final long MAX_DB_SIZE = 1000000;

  private static final String GET_DOCUMENT_BY_UID      = "GET_DOCUMENT_BY_UID";
  private static final String GET_DOCUMENTS_FOR_ENTITY = "GET_DOCUMENTS_FOR_ENTITY";
  private static final String GET_GLOBAL_DOCUMENTS     = "GET_GLOBAL_DOCUMENTS";

  private static final String INSERT_DOCUMENT = "INSERT_DOCUMENT";
  private static final String UPDATE_DOCUMENT = "UPDATE_DOCUMENT";

  private final FileSystem fileSystem;
  private final CodeValueRepository codeValueRepo;

  /**
   * Primary Constructor used to create an instance of the DocumentRepositoryJdbc.
   *
   * @param dataSource
   *    The {@link DataSource} used to retrieve / persist data objects.
   * @param fileSystem
   *    The {@link FileSystem} used to persist the document externally.
   * @param codeValueRepo
   *     The {@link CodeValueRepository} to use to retrieve additional attributes.
   */
  @Inject
  public DocumentRepositoryJdbc(final DataSource dataSource,
                                final FileSystem fileSystem,
                                final CodeValueRepository codeValueRepo) {
    super(dataSource);

    this.fileSystem = fileSystem;
    this.codeValueRepo = codeValueRepo;
  }

  @Override
  public Document getDocumentByUID(final long uid) {
    LOGGER.info("Calling getDocumentByUID for [{}]", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<>(1);
    params.put("document_id", uid);

    List<Document> documents = getJdbcTemplate().query(stmtLoader.load(GET_DOCUMENT_BY_UID), params, new DocumentMapper(codeValueRepo));
    if (documents.size() == 1) {
      return documents.get(0);
    } else if (documents.size() == 0) {
      LOGGER.warn("Document with UID of [{}] was not found.", uid);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.warn("More than one Document with UID of [{}] was found.", uid);
      throw new IncorrectResultSizeDataAccessException(1, documents.size());
    }
  }

  @Override
  public List<Document> getDocumentsForMembership(final Membership membership) {
    Assert.notNull(membership, "Assertion Failed - argument [membership] cannot be null");

    LOGGER.info("Calling getDocumentsForMembership [{}]", membership.getMemberUID());

    return getDocumentsForEntity(Document.MEMBERSHIP, membership.getMembershipUID());
  }

  @Override
  public List<Document> getGlobalDocuments() {
    LOGGER.info("Calling getGlobalDocuments [{}]");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(stmtLoader.load(GET_GLOBAL_DOCUMENTS), new DocumentMapper(codeValueRepo));
  }

  @Override
  public Document saveDocument(final Document document, final User user)
      throws OptimisticLockFailureException, IncorrectResultSizeDataAccessException, DataAccessException {

    Assert.notNull(document, "Assertion Failed - argument [document] cannot be null");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Saving Document for [{}, {}]", document.getParentEntityName(), document.getParentEntityUID());

    if (document.getDocumentContents() != null && document.getDocumentContents().length > MAX_DB_SIZE) {
      try {
        // Document exceeds the size we want to store in the DB
        // save to our external persistent store
        Optional<FileDTO> file = fileSystem.saveFileForEntity(
            document.getParentEntityName(),
            document.getParentEntityUID(),
            document.getDocumentName(),
            document.getDocumentType().getMeaning(),
            document.getDocumentFormat(),
            document.getDocumentContents());

        // If we were successful, we need to set the location of the file and clear the contents (so we don't save it twice)
        if (file.isPresent()) {
          document.setDocumentLocation(file.get().getFileId());
          document.setDocumentContents(null);
        }
      } catch (FileSystemException | FileAlreadyExistsException ex) {
        LOGGER.error("Unable to save file to external persistence store", ex);
      }
    }

    if (document.getDocumentUID() == 0L) {
      return insertDocument(document, user);
    } else {
      return updateDocument(document, user);
    }
  }

  /**
   * Helper method to retrieve the {@link List} of {@link Document Documents} by EntityName / EntityID.
   *
   * @param entityName
   *    The <i>name</i> of the entity the document is associated with.
   * @param entityID
   *    The unique identifier of the entity the document is associated with.
   *
   * @return A {@link List} of {@link Document Documents} associated with the specified EntityName / EntityID.
   */
  private List<Document> getDocumentsForEntity(final String entityName, final long entityID) {
    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    Map<String, Object> params = new HashMap<>();
    params.put("parent_entity_name", entityName);
    params.put("parent_entity_id", entityID);

    return getJdbcTemplate().query(stmtLoader.load(GET_DOCUMENTS_FOR_ENTITY), params, new DocumentMapper(codeValueRepo));
  }

  private Document insertDocument(final Document document, final User user) {
    LOGGER.info("Inserting new Document for [{}]", document.getParentEntityName());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_DOCUMENT), DocumentMapper.mapInsertStatement(document, user), keyHolder);
    if (cnt == 1) {
      document.setDocumentUID(keyHolder.getKey().longValue());
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return document;
  }

  private Document updateDocument(final Document document, final User user) {
    LOGGER.info("Updating Document for [{}]", document.getDocumentUID());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_DOCUMENT), DocumentMapper.mapUpdateStatement(document, user));
    if (cnt == 1) {
      document.setDocumentUpdateCount(document.getDocumentUpdateCount() + 1);
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt
          + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return document;
  }
}

package com.cagst.swkroa.document;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.filesystem.FileSystem;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.user.User;
import net.sf.ehcache.transaction.xa.OptimisticLockFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A JDBC Implementation of the {@link DocumentRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("documentRepo")
/* package */ class DocumentRepositoryJdbc extends BaseRepositoryJdbc implements DocumentRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentRepositoryJdbc.class);

  private static final String GET_DOCUMENT_BY_UID      = "GET_DOCUMENT_BY_UID";
  private static final String GET_DOCUMENTS_FOR_ENTITY = "GET_DOCUMENTS_FOR_ENTITY";

  private final FileSystem fileSystem;

  /**
   * Primary Constructor used to create an instance of the DocumentRepositoryJdbc.
   *
   * @param dataSource
   *    The {@link DataSource} used to retrieve / persist data objects.
   * @param fileSystem
   *    The {@link FileSystem} used to persist the document externally.
   */
  @Inject
  public DocumentRepositoryJdbc(final DataSource dataSource, final FileSystem fileSystem) {
    super(dataSource);

    this.fileSystem = fileSystem;
  }

  @Override
  public Document getDocumentByUID(final long uid) {
    LOGGER.info("Calling getDocumentByUID for [{}]", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<>(1);
    params.put("document_id", uid);

    List<Document> documents = getJdbcTemplate().query(stmtLoader.load(GET_DOCUMENT_BY_UID), params, new DocumentMapper());
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
  public Document saveDocument(final Document document, final User user)
      throws OptimisticLockFailureException, IncorrectResultSizeDataAccessException, DataAccessException {

    Assert.notNull(document, "Assertion Failed - argument [document] cannot be null");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Saving Document for [{}, {}]", document.getParentEntityName(), document.getParentEntityUID());

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

    return getJdbcTemplate().query(stmtLoader.load(GET_DOCUMENTS_FOR_ENTITY), params, new DocumentMapper());
  }

  private Document insertDocument(final Document document, final User user) {
    return null;
  }

  private Document updateDocument(final Document document, final User user) {
    return null;
  }
}

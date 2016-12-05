package com.cagst.swkroa.document;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.internal.BaseRepositoryJdbc;
import com.cagst.swkroa.internal.StatementLoader;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

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
  private static final String GET_GLOBAL_DOCUMENTS     = "GET_GLOBAL_DOCUMENTS";

  private static final String INSERT_DOCUMENT = "INSERT_DOCUMENT";
  private static final String UPDATE_DOCUMENT = "UPDATE_DOCUMENT";

  private final CodeValueRepository codeValueRepo;

  /**
   * Primary Constructor used to create an instance of DocumentRepositoryJdbc.
   *
   * @param dataSource
   *    The {@link DataSource} used to retrieve / persist data objects.
   * @param codeValueRepo
   *     The {@link CodeValueRepository} to use to retrieve additional attributes.
   */
  @Inject
  public DocumentRepositoryJdbc(DataSource dataSource,
                                CodeValueRepository codeValueRepo) {
    super(dataSource);

    this.codeValueRepo = codeValueRepo;
  }

  @Override
  public Document getDocumentByUID(long uid) {
    LOGGER.info("Calling getDocumentByUID for [{}]", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<Document> documents = getJdbcTemplate().query(
        stmtLoader.load(GET_DOCUMENT_BY_UID),
        new MapSqlParameterSource("document_id", uid),
        new DocumentMapper(codeValueRepo, true));

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
  public List<Document> getDocumentsForMembership(Membership membership) {
    Assert.notNull(membership, "Argument [membership] cannot be null");

    LOGGER.info("Calling getDocumentsForMembership [{}]", membership.getMemberUID());

    return getDocumentsForEntity(Document.MEMBERSHIP, membership.getMembershipUID());
  }

  @Override
  public List<Document> getGlobalDocuments() {
    LOGGER.info("Calling getGlobalDocuments [{}]");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(stmtLoader.load(GET_GLOBAL_DOCUMENTS), new DocumentMapper(codeValueRepo, false));
  }

  @Override
  public Document saveDocument(Document document, User user)
      throws DataAccessException {

    Assert.notNull(document, "Argument [document] cannot be null");
    Assert.notNull(user, "Argument [user] cannot be null");

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
  private List<Document> getDocumentsForEntity(String entityName, long entityID) {
    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("parent_entity_name", entityName);
    params.addValue("parent_entity_id", entityID);

    return getJdbcTemplate().query(stmtLoader.load(GET_DOCUMENTS_FOR_ENTITY), params, new DocumentMapper(codeValueRepo, false));
  }

  private Document insertDocument(Document document, User user) {
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

  private Document updateDocument(Document document, User user) {
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

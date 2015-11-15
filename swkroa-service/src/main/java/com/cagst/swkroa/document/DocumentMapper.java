package com.cagst.swkroa.document;

import com.cagst.common.util.CGTDateTimeUtils;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.user.User;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Maps a row in the resultset into a {@link Document} object. Used to marshal / unmarshal a {@link Document} to / from
 * the database.
 *
 * @author Craig Gaskill
 */
/* package */ final class DocumentMapper implements RowMapper<Document> {
  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentMapper.class);

  private static final String DOCUMENT_ID          = "document_id";
  private static final String PARENT_ENTITY_ID     = "parent_entity_id";
  private static final String PARENT_ENTITY_NAME   = "parent_entity_name";
  private static final String DOCUMENT_TYPE_CD     = "document_type_cd";
  private static final String DOCUMENT_NAME        = "document_name";
  private static final String DOCUMENT_FORMAT      = "document_format";
  private static final String DOCUMENT_LOCATION    = "document_location";
  private static final String DOCUMENT_CONTENT     = "document_content";
  private static final String DOCUMENT_DESCRIPTION = "document_desc";
  private static final String BEG_EFF_DT           = "beg_eff_dt";
  private static final String END_EFF_DT           = "end_eff_dt";

  // meta-data
  private static final String ACTIVE_IND = "active_ind";
  private static final String CREATE_ID  = "create_id";
  private static final String UPDT_ID    = "updt_id";
  private static final String DOCUMENT_UPDT_CNT = "document_updt_cnt";

  private final CodeValueRepository codeValueRepo;

  /**
   * Primary Constructor used to create an instance of <i>DocumentMapper</i>
   *
   * @param codeValueRepo
   *     The {@link CodeValueRepository} to use to retrieve codified values associated with the document.
   */
  public DocumentMapper(final CodeValueRepository codeValueRepo) {
    this.codeValueRepo = codeValueRepo;
  }

  @Override
  public Document mapRow(ResultSet rs, int rowNum) throws SQLException {
    Document document = new Document();
    document.setDocumentUID(rs.getLong(DOCUMENT_ID));
    document.setParentEntityUID(rs.getLong(PARENT_ENTITY_ID));
    document.setParentEntityName(rs.getString(PARENT_ENTITY_NAME));
    document.setDocumentType(codeValueRepo.getCodeValueByUID(rs.getLong(DOCUMENT_TYPE_CD)));
    document.setDocumentName(rs.getString(DOCUMENT_NAME));
    document.setDocumentFormat(rs.getString(DOCUMENT_FORMAT));
    document.setDocumentLocation(rs.getString(DOCUMENT_LOCATION));
    document.setDocumentDescription(rs.getString(DOCUMENT_DESCRIPTION));
    document.setBeginEffectiveDate(CGTDateTimeUtils.getDateTime(rs, BEG_EFF_DT));
    document.setEndEffectiveDate(CGTDateTimeUtils.getDateTime(rs, END_EFF_DT));

    Blob content = rs.getBlob(DOCUMENT_CONTENT);
    if (content != null) {
      InputStream in = content.getBinaryStream();
      try {
        document.setDocumentContents(IOUtils.toByteArray(in));
      } catch (IOException ex) {
        LOGGER.error("Error retrieving document contents [" + ex.getLocalizedMessage() + "]", ex);
      } finally {
        IOUtils.closeQuietly(in);
      }
    }

    document.setActive(rs.getBoolean(ACTIVE_IND));
    document.setDocumentUpdateCount(rs.getLong(DOCUMENT_UPDT_CNT));

    return document;
  }

  /**
   * Will marshal a {@link Document} into a {@link MapSqlParameterSource} for inserting into the database.
   *
   * @param document
   *     The {@link Document} to map into an insert statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final Document document, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(PARENT_ENTITY_ID, document.getParentEntityUID() != 0L ? document.getParentEntityUID() : null);
    params.addValue(PARENT_ENTITY_NAME, document.getParentEntityName());
    params.addValue(DOCUMENT_DESCRIPTION, document.getDocumentDescription());
    params.addValue(DOCUMENT_TYPE_CD, document.getDocumentType().getCodeValueUID());
    params.addValue(DOCUMENT_NAME, document.getDocumentName());
    params.addValue(DOCUMENT_FORMAT,document.getDocumentFormat());
    params.addValue(DOCUMENT_LOCATION, document.getDocumentLocation());
    params.addValue(DOCUMENT_CONTENT, document.getDocumentContents() != null ? new SqlLobValue(document.getDocumentContents()) : null, Types.BLOB);
    params.addValue(BEG_EFF_DT, document.getBeginEffectiveDate().toDate());
    params.addValue(END_EFF_DT, document.getEndEffectiveDate() != null ? document.getEndEffectiveDate().toDate() : null);

    params.addValue(ACTIVE_IND, document.isActive());
    params.addValue(CREATE_ID, user.getUserUID());
    params.addValue(UPDT_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link Document} into a {@link MapSqlParameterSource} for updating into the database.
   *
   * @param document
   *     The {@link Document} to map into an update statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final Document document, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(DOCUMENT_DESCRIPTION, document.getDocumentDescription());
    params.addValue(DOCUMENT_TYPE_CD, document.getDocumentType().getCodeValueUID());
    params.addValue(DOCUMENT_NAME, document.getDocumentName());
    params.addValue(DOCUMENT_FORMAT,document.getDocumentFormat());
    params.addValue(DOCUMENT_LOCATION, document.getDocumentLocation());
    params.addValue(DOCUMENT_CONTENT, document.getDocumentContents() != null ? new SqlLobValue(document.getDocumentContents()) : null, Types.BLOB);
    params.addValue(BEG_EFF_DT, document.getBeginEffectiveDate().toDate());
    params.addValue(END_EFF_DT, document.getEndEffectiveDate() != null ? document.getEndEffectiveDate().toDate() : null);
    params.addValue(ACTIVE_IND, document.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    params.addValue(DOCUMENT_ID, document.getDocumentUID());
    params.addValue(DOCUMENT_UPDT_CNT, document.getDocumentUpdateCount());

    return params;
  }
}

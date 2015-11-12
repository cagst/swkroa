package com.cagst.swkroa.document;

import com.cagst.common.util.CGTDateTimeUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a row in the resultset into a {@link Document} object. Used to marshal / unmarshal a {@link Document} to / from
 * the database.
 *
 * @author Craig Gaskill
 */
/* package */ final class DocumentMapper implements RowMapper<Document> {
  private static final String DOCUMENT_ID = "document_id";
  private static final String PARENT_ENTITY_ID = "parent_entity_id";
  private static final String PARENT_ENTITY_NAME = "parent_entity_name";
  private static final String DOCUMENT_TYPE = "document_type";
  private static final String DOCUMENT_FORMAT = "document_format";
  private static final String DOCUMENT_LOCATION = "document_location";
  private static final String DOCUMENT_CONTENT = "document_content";
  private static final String DOCUMENT_DESCRIPTION = "document_desc";
  private static final String BEG_EFF_DT = "beg_eff_dt";
  private static final String END_EFF_DT = "end_eff_dt";

  // meta-data
  private static final String ACTIVE_IND = "active_ind";
  private static final String CREATE_ID  = "create_id";
  private static final String UPDT_ID    = "updt_id";
  private static final String DOCUMENT_UPDT_CNT = "document_updt_cnt";

  @Override
  public Document mapRow(ResultSet rs, int rowNum) throws SQLException {
    Document document = new Document();
    document.setDocumentUID(rs.getLong(DOCUMENT_ID));
    document.setParentEntityUID(rs.getLong(PARENT_ENTITY_ID));
    document.setParentEntityName(rs.getString(PARENT_ENTITY_NAME));
    document.setDocumentType(rs.getString(DOCUMENT_TYPE));
    document.setDocumentFormat(rs.getString(DOCUMENT_FORMAT));
    document.setDocumentLocation(rs.getString(DOCUMENT_LOCATION));
    document.setDocumentDescription(rs.getString(DOCUMENT_DESCRIPTION));
    document.setBeginEffectiveDate(CGTDateTimeUtils.getDateTime(rs, BEG_EFF_DT));
    document.setEndEffectiveDate(CGTDateTimeUtils.getDateTime(rs, END_EFF_DT));

    Blob content = rs.getBlob(DOCUMENT_CONTENT);
    if (content != null) {
    }

    document.setActive(rs.getBoolean(ACTIVE_IND));
    document.setDocumentUpdateCount(rs.getLong(DOCUMENT_UPDT_CNT));

    return document;
  }
}

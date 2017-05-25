package com.cagst.swkroa.document;

import java.io.Serializable;

import com.cagst.swkroa.codevalue.CodeValue;
import org.joda.time.DateTime;

/**
 * Represents a Document stored in the system.
 *
 * @author Craig Gaskill
 */
public final class Document implements Serializable {
  public static final String MEMBERSHIP = "MEMBERSHIP";
  public static final String TRANSACTION = "TRANSACTION";

  private long document_id;
  private long parent_entity_id;
  private String parent_entity_name;
  private CodeValue document_type;
  private String document_name;
  private String document_format;
  private String document_location;
  private byte[] document_content;
  private String document_desc;
  private DateTime beg_eff_dt;
  private DateTime end_eff_dt;

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  public long getDocumentUID() {
    return document_id;
  }

  public void setDocumentUID(final long uid) {
    this.document_id = uid;
  }

  public long getParentEntityUID() {
    return parent_entity_id;
  }

  public void setParentEntityUID(final long uid) {
    this.parent_entity_id = uid;
  }

  public String getParentEntityName() {
    return parent_entity_name;
  }

  public void setParentEntityName(final String name) {
    this.parent_entity_name = name;
  }

  public CodeValue getDocumentType() {
    return document_type;
  }

  public void setDocumentType(final CodeValue type) {
    this.document_type = type;
  }

  public String getDocumentName() {
    return document_name;
  }

  public void setDocumentName(final String name) {
    this.document_name = name;
  }

  public String getDocumentFormat() {
    return document_format;
  }

  public void setDocumentFormat(final String format) {
    this.document_format = format;
  }

  public String getDocumentLocation() {
    return document_location;
  }

  public void setDocumentLocation(final String location) {
    this.document_location = location;
  }

  public byte[] getDocumentContents() {
    return document_content;
  }

  public void setDocumentContents(final byte[] content) {
    this.document_content = content;
  }

  public String getDocumentDescription() {
    return document_desc;
  }

  public void setDocumentDescription(final String description) {
    this.document_desc = description;
  }

  public DateTime getBeginEffectiveDate() {
    return beg_eff_dt;
  }

  public void setBeginEffectiveDate(final DateTime effectiveDate) {
    this.beg_eff_dt = effectiveDate;
  }

  public DateTime getEndEffectiveDate() {
    return end_eff_dt;
  }

  public void setEndEffectiveDate(final DateTime effectiveDate) {
    this.end_eff_dt = effectiveDate;
  }

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(final boolean active) {
    this.active_ind = active;
  }

  public long getDocumentUpdateCount() {
    return updt_cnt;
  }

  /* package */ void setDocumentUpdateCount(final long count) {
    this.updt_cnt = count;
  }
}

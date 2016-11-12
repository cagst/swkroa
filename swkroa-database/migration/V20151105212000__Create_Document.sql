INSERT INTO codeset (codeset_id, codeset_display, codeset_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (7, 'Document Type', 'DOCUMENT_TYPE', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO codevalue (codeset_id, codevalue_display, codevalue_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (7, 'Newsletter', 'DOCUMENT_NEWSLETTER', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO codevalue (codeset_id, codevalue_display, codevalue_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (7, 'Renewal Notice', 'DOCUMENT_RENEWAL', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

CREATE TABLE document (
  document_id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  parent_entity_id       BIGINT UNSIGNED NULL,
  parent_entity_name     VARCHAR(25) NULL,
  document_type_cd       BIGINT NOT NULL COMMENT 'The type of document: Invoice, Newsletter, etc.',
  document_name          VARCHAR(100) NOT NULL COMMENT 'The name (filename) for the document',
  document_format        VARCHAR(50) NOT NULL COMMENT 'The format of the content: XML, application/pdf, etc.',
  document_location      VARCHAR(255) NULL,
  document_content       MEDIUMBLOB NULL,
  document_desc          VARCHAR(150) NULL,
  beg_eff_dt             DATE NOT NULL,
  end_eff_dt             DATE NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT document_pk PRIMARY KEY (document_id),
  INDEX document_idx1 (beg_eff_dt, document_type_cd),
  INDEX document_idx2 (parent_entity_name, parent_entity_id),
  INDEX document_idx3 (document_location)
) ENGINE = InnoDB;

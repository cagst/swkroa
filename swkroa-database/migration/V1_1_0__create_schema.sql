CREATE TABLE deposit_transaction (
  deposit_transaction_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  deposit_id             BIGINT UNSIGNED NOT NULL,
  transaction_id         BIGINT UNSIGNED NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT deposit_transaction_pk  PRIMARY KEY (deposit_transaction_id),
  CONSTRAINT deposit_transaction_fk1 FOREIGN KEY (deposit_id) REFERENCES deposit (deposit_id),
  CONSTRAINT deposit_transaction_fk2 FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
) ENGINE = InnoDB;

ALTER TABLE membership
  ADD COLUMN close_reason_id  BIGINT UNSIGNED NULL,
  ADD COLUMN close_reason_txt VARCHAR(100) NULL,
  ADD COLUMN close_dt_tm      DATETIME NULL;

ALTER TABLE member
  ADD COLUMN close_reason_id  BIGINT UNSIGNED NULL,
  ADD COLUMN close_reason_txt VARCHAR(100) NULL,
  ADD COLUMN close_dt_tm      DATETIME NULL;

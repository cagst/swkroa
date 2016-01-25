CREATE TABLE job (
  job_id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  job_name          VARCHAR(100) NOT NULL,
  job_type          VARCHAR(25) NOT NULL,
  job_status        VARCHAR(25) NOT NULL,
  job_detail        VARCHAR(5000) NULL,
  create_dt_tm      DATETIME NOT NULL,
  create_id         BIGINT UNSIGNED NOT NULL,
  updt_dt_tm        DATETIME NOT NULL,
  updt_id           BIGINT UNSIGNED NOT NULL,
  updt_cnt          INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT job_pk PRIMARY KEY (job_id),
  INDEX job_idx1 (job_type, job_status)
) ENGINE = InnoDB;

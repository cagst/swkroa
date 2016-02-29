CREATE TABLE job (
  job_id                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  job_name               VARCHAR(100) NOT NULL,
  job_type               VARCHAR(25) NOT NULL,
  job_status             VARCHAR(25) NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT NOT NULL,
  updt_cnt               INT DEFAULT 0 NOT NULL,
  CONSTRAINT job_pk PRIMARY KEY (job_id)
);

CREATE TABLE job_detail (
  job_detail_id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  job_id                 BIGINT UNSIGNED NOT NULL,
  job_status             VARCHAR(25) NOT NULL,
  parent_entity_id       BIGINT UNSIGNED NULL,
  parent_entity_name     VARCHAR(25) NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT NOT NULL,
  updt_cnt               INT DEFAULT 0 NOT NULL,
  CONSTRAINT job_detail_pk PRIMARY KEY (job_detail_id),
  CONSTRAINT job_detail_fk1 FOREIGN KEY (job_id) REFERENCES job (job_id)
);

CREATE TABLE role (
  role_id                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  role_name              VARCHAR(50) NOT NULL,
  role_key               VARCHAR(25) NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT NOT NULL,
  updt_cnt               INT DEFAULT 0 NOT NULL,
  CONSTRAINT role_pk PRIMARY KEY (role_id)
) ENGINE = InnoDB;

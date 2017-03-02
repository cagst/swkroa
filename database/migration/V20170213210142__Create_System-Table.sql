CREATE TABLE system (
  system_key             VARCHAR(25) NOT NULL,
  system_name            VARCHAR(50) NOT NULL,
  system_value           VARCHAR(50) NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT NOT NULL,
  updt_cnt               INT DEFAULT 0 NOT NULL,
  CONSTRAINT system_pk PRIMARY KEY (system_key)
) ENGINE = InnoDb;

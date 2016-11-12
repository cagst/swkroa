CREATE TABLE user_role (
  user_id                BIGINT UNSIGNED NOT NULL,
  role_id                BIGINT UNSIGNED NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT NOT NULL,
  updt_cnt               INT DEFAULT 0 NOT NULL,
  CONSTRAINT user_role_pk PRIMARY KEY (user_id, role_id),
  CONSTRAINT user_role_fk1 FOREIGN KEY (role_id) REFERENCES role (role_id),
  CONSTRAINT user_role_fk2 FOREIGN KEY (user_id) REFERENCES user (user_id)
) ENGINE = InnoDb;

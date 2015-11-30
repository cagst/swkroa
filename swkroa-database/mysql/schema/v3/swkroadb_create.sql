CREATE TABLE privilege (
  privilege_id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  privilege_key          VARCHAR(25) NOT NULL,
  privilege_name         VARCHAR(50) NOT NULL,
  privilege_desc         VARCHAR(500) NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT privilege_pk PRIMARY KEY (privilege_id)
) ENGINE = InnoDB;

CREATE TABLE role (
  role_id                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  role_key               VARCHAR(25) NOT NULL,
  role_name              VARCHAR(50) NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT role_pk PRIMARY KEY (role_id)
) ENGINE = InnoDB;

CREATE TABLE role_privilege (
  role_privilege_id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  role_id                BIGINT UNSIGNED NOT NULL,
  privilege_id           BIGINT UNSIGNED NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT role_privilege_pk PRIMARY KEY (role_privilege_id),
  CONSTRAINT role_privilege_fk1 FOREIGN KEY (role_id) REFERENCES role (role_id),
  CONSTRAINT role_privilege_fk2 FOREIGN KEY (privilege_id) REFERENCES privilege (privilege_id)
) ENGINE = InnoDB;

CREATE TABLE user_role (
  user_role_id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id                BIGINT UNSIGNED NOT NULL,
  role_id                BIGINT UNSIGNED NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT user_role_pk PRIMARY KEY (user_role_id),
  CONSTRAINT user_role_fk1 FOREIGN KEY (user_id) REFERENCES user (user_id),
  CONSTRAINT user_role_fk2 FOREIGN KEY (role_id) REFERENCES role (role_id)
) ENGINE = InnoDB;


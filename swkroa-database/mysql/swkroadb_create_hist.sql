CREATE TABLE person_hist (
  person_hist_id        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  person_id              BIGINT UNSIGNED NOT NULL,
  name_last              VARCHAR(50) NOT NULL,
  name_last_key          VARCHAR(50) NOT NULL,
  name_first            VARCHAR(50) NOT NULL,
  name_first_key        VARCHAR(50) NOT NULL,
  name_middle            VARCHAR(50) NULL,
  dob_dt_tm              TIMESTAMP NULL,
  gender_cd              BIGINT UNSIGNED NULL,
  active_ind            BOOLEAN NOT NULL DEFAULT 1,
  create_dt_tm          TIMESTAMP NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm            TIMESTAMP NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt              INT UNSIGNED NOT NULL DEFAULT 0,
  CONSTRAINT person_hist_pk PRIMARY KEY (person_hist_id),
  CONSTRAINT person_hist_fk1 FOREIGN KEY (person_id) REFERENCES person (person_id)
) ENGINE = InnoDB;

CREATE TABLE user_hist (
  user_hist_id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id                  BIGINT UNSIGNED NOT NULL,
  person_id                BIGINT UNSIGNED NOT NULL,
  username                VARCHAR(50) NOT NULL,
  password                CHAR(64) NOT NULL,
  password_key            CHAR(64) NOT NULL,
  last_signin_dt_tm        DATETIME NULL,
  last_signin_ip          VARCHAR(25) NULL,
  signin_attempts          INT UNSIGNED NOT NULL DEFAULT 0,
  change_password_ind      BOOLEAN NOT NULL DEFAULT 1,
  account_locked_dt_tm    DATETIME NULL,
  account_expired_dt_tm    DATETIME NULL,
  password_changed_dt_tm  DATETIME NULL,
  active_ind              BOOLEAN NOT NULL DEFAULT 1,
  create_dt_tm            TIMESTAMP NOT NULL,
  create_id                BIGINT UNSIGNED NOT NULL,
  updt_dt_tm              TIMESTAMP NOT NULL,
  updt_id                  BIGINT UNSIGNED NOT NULL,
  updt_cnt                INT UNSIGNED NOT NULL DEFAULT 0,
  CONSTRAINT user_hist_pk PRIMARY KEY (user_hist_id),
  CONSTRAINT user_hist_fk1 FOREIGN KEY (user_id) REFERENCES user (user_id)
) ENGINE = InnoDB;

CREATE TABLE codeset_hist (
  codeset_hist_id        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  codeset_id            BIGINT UNSIGNED NOT NULL,
  codeset_display        VARCHAR(50) NOT NULL,
  codeset_meaning        VARCHAR(25) NULL,
  active_ind            BOOLEAN NOT NULL DEFAULT 1,
  create_dt_tm          TIMESTAMP NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm            TIMESTAMP NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt              INT UNSIGNED NOT NULL DEFAULT 0,
  CONSTRAINT codeset_hist_pk PRIMARY KEY (codeset_hist_id),
  CONSTRAINT codeset_hist_fk1 FOREIGN KEY (codeset_id) REFERENCES codeset (codeset_id)
) ENGINE = InnoDB;

CREATE TABLE codevalue_hist (
  codevalue_hist_id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  codevalue_id          BIGINT UNSIGNED NOT NULL,
  codeset_id            BIGINT UNSIGNED NOT NULL,
  codevalue_display      VARCHAR(50) NOT NULL,
  codevalue_meaning      VARCHAR(25) NULL,
  active_ind            BOOLEAN NOT NULL DEFAULT 1,
  create_dt_tm          TIMESTAMP NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm            TIMESTAMP NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt              INT UNSIGNED NOT NULL DEFAULT 0,
  CONSTRAINT codevalue_hist_pk PRIMARY KEY (codevalue_hist_id),
  CONSTRAINT codevalue_hist_fk1 FOREIGN KEY (codevalue_id) REFERENCES codevalue (codevalue_id)
) ENGINE = InnoDB;

CREATE TABLE member_hist (
  member_hist_id    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  member_id          BIGINT UNSIGNED NOT NULL,
  person_id          BIGINT UNSIGNED NOT NULL,
  owner_ident        VARCHAR(15) NOT NULL,
  company_name      VARCHAR(250),
  dues_amount        NUMERIC(10,2) DEFAULT 0.0 NOT NULL,
  due_on            DATE NOT NULL,
  start_dt_tm       DATE NOT NULL,
  end_dt_tm         DATE NULL,
  member_type_cv    BIGINT NOT NULL,
  active_ind        BOOLEAN NOT NULL DEFAULT 1,
  create_dt_tm      TIMESTAMP NOT NULL,
  create_id          BIGINT UNSIGNED NOT NULL,
  updt_dt_tm        TIMESTAMP NOT NULL,
  updt_id            BIGINT UNSIGNED NOT NULL,
  updt_cnt          INT UNSIGNED NOT NULL DEFAULT 0,
  CONSTRAINT member_hist_pk PRIMARY KEY (member_hist_id),
  CONSTRAINT member_hist_fk1 FOREIGN KEY (member_id) REFERENCES member (member_id)
) ENGINE = InnoDB;

delimiter |

CREATE TRIGGER person_history AFTER UPDATE ON person
  FOR EACH ROW BEGIN
    INSERT INTO person_hist (person_id, name_last, name_last_key, name_middle, name_first, name_first_key, dob_dt_tm, gender_cd, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.person_id, old.name_last, old.name_last_key, old.name_middle, old.name_first, old.name_first_key, old.dob_dt_tm, old.gender_cd, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER user_history AFTER UPDATE ON user
  FOR EACH ROW BEGIN
    IF old.username != new.username OR
       old.password != new.password OR
       old.change_password_ind != new.change_password_ind OR
       old.account_locked_dt_tm != new.account_locked_dt_tm OR
       old.active_ind != new.active_ind THEN
      INSERT INTO user_hist (user_id, person_id, username, password, password_key, last_signin_dt_tm, last_signin_ip, signin_attempts, change_password_ind, account_locked_dt_tm, account_expired_dt_tm, password_changed_dt_tm, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
      VALUES (old.user_id, old.person_id, old.username, old.password, old.password_key, old.last_signin_dt_tm, old.last_signin_ip, old.signin_attempts, old.change_password_ind, old.account_locked_dt_tm, old.account_expired_dt_tm, old.password_changed_dt_tm, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
    END IF;
  END;
|

CREATE TRIGGER codeset_history AFTER UPDATE ON codeset
  FOR EACH ROW BEGIN
    INSERT INTO codeset_hist (codeset_id, codeset_display, codeset_meaning, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.codeset_id, old.codeset_display, old.codeset_meaning, old.active_ind, old.updt_id, old.create_id, old.create_dt_tm, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER codevalue_history AFTER UPDATE ON codevalue
  FOR EACH ROW BEGIN
    INSERT INTO codevalue_hist (codevalue_id, codeset_id, codevalue_display, codevalue_meaning, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.codevalue_id, old.codeset_id, old.codevalue_display, old.codevalue_meaning, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER member_history AFTER UPDATE ON member
  FOR EACH ROW BEGIN
    INSERT INTO member_hist (member_id, person_id, owner_ident, company_name, dues_amount, due_on, start_dt_tm, end_dt_tm, member_type_cv, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.member_id, old.person_id, old.owner_ident, old.company_name, old.dues_amount, old.due_on, old.start_dt_tm, old.end_dt_tm, old.member_type_cv, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TABLE county_hist (
  county_hist_id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  county_id              BIGINT UNSIGNED NOT NULL,
  state_code             CHAR(2) NOT NULL,
  county_code            CHAR(2) NOT NULL,
  county_name            VARCHAR(50) NOT NULL,
  swkroa_county_ind      BOOLEAN DEFAULT 0 NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT county_hist_pk PRIMARY KEY (county_hist_id)
) ENGINE = InnoDB;

CREATE TABLE codeset_hist (
  codeset_hist_id        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  codeset_id             BIGINT UNSIGNED NOT NULL,
  codeset_display        VARCHAR(50) NOT NULL,
  codeset_meaning        VARCHAR(25) NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT codeset_hist_pk PRIMARY KEY (codeset_hist_id)
) ENGINE = InnoDB;

CREATE TABLE codevalue_hist (
  codevalue_hist_id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  codevalue_id           BIGINT UNSIGNED NOT NULL,
  codeset_id             BIGINT UNSIGNED NOT NULL,
  codevalue_display      VARCHAR(50) NOT NULL,
  codevalue_meaning      VARCHAR(25) NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT codevalue_hist_pk PRIMARY KEY (codevalue_hist_id)
) ENGINE = InnoDB;

CREATE TABLE person_hist (
  person_hist_id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  person_id              BIGINT UNSIGNED NOT NULL,
  title_cd               BIGINT NULL,
  name_last              VARCHAR(50) NOT NULL,
  name_last_key          VARCHAR(50) NOT NULL,
  name_first             VARCHAR(50) NOT NULL,
  name_first_key         VARCHAR(50) NOT NULL,
  name_middle            VARCHAR(50) NULL,
  locale_language        VARCHAR(10) NULL,
  locale_country         VARCHAR(10) NULL,
  time_zone              VARCHAR(50) NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT person_hist_pk PRIMARY KEY (person_hist)
) ENGINE = InnoDB;

CREATE TABLE address_hist (
  address_hist_id        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  address_id             BIGINT UNSIGNED NOT NULL,
  parent_entity_id       BIGINT UNSIGNED NOT NULL,
  parent_entity_name     VARCHAR(25) NOT NULL,
  address_type_cd        BIGINT UNSIGNED NOT NULL,
  address1               VARCHAR(50) NOT NULL,
  address2               VARCHAR(50) NULL,
  address3               VARCHAR(50) NULL,
  city                   VARCHAR(50) NOT NULL,
  state_code             CHAR(2) NOT NULL,
  country_code           CHAR(2) NOT NULL,
  postal_code            VARCHAR(15) NOT NULL,
  primary_ind            BOOLEAN DEFAULT 0 NOT NULL,
  verified_cd            BIGINT UNSIGNED NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT address_hist_pk PRIMARY KEY (address_hist_id)
) ENGINE = InnoDB;

CREATE TABLE phone_hist (
  phone_hist_id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  phone_id               BIGINT UNSIGNED NOT NULL,
  parent_entity_id       BIGINT UNSIGNED NOT NULL,
  parent_entity_name     VARCHAR(25) NOT NULL,
  phone_type_cd          BIGINT UNSIGNED NOT NULL,
  phone_number           VARCHAR(25) NOT NULL,
  phone_extension        VARCHAR(10) NULL,
  primary_ind            BOOLEAN DEFAULT 0 NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT phone_hist_pk PRIMARY KEY (phone_hist_id)
) ENGINE = InnoDB;

CREATE TABLE email_hist (
  email_hist_id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  email_id               BIGINT UNSIGNED NOT NULL,
  parent_entity_id       BIGINT UNSIGNED NOT NULL,
  parent_entity_name     VARCHAR(25) NOT NULL,
  email_type_cd          BIGINT UNSIGNED NOT NULL,
  email_address          VARCHAR(256) NOT NULL,
  primary_ind            BOOLEAN DEFAULT 0 NOT NULL,
  verified_cd            BIGINT UNSIGNED NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT email_hist_pk PRIMARY KEY (email_hist_id)
) ENGINE = InnoDB;

CREATE TABLE user_hist (
  user_hist_id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id                BIGINT UNSIGNED NOT NULL,
  person_id              BIGINT UNSIGNED NOT NULL,
  username               VARCHAR(50) NOT NULL,
  password               CHAR(60) NOT NULL,
  last_signin_dt_tm      DATETIME NULL,
  last_signin_ip         VARCHAR(50) NULL,
  signin_attempts        INT UNSIGNED DEFAULT 0 NOT NULL,
  temporary_pwd_ind      BOOLEAN DEFAULT 1 NOT NULL,
  account_locked_dt_tm   DATETIME NULL,
  account_expired_dt_tm  DATETIME NULL,
  password_changed_dt_tm DATETIME NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT user_hist_pk PRIMARY KEY (user_hist_id)
) ENGINE = InnoDB;

CREATE TABLE membership_hist (
  membership_hist_id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  membership_id          BIGINT UNSIGNED NOT NULL,
  entity_type_cd         BIGINT UNSIGNED NOT NULL,
  next_due_dt            DATE NOT NULL,
  dues_amount            NUMERIC(10,2) NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT membership_hist_pk PRIMARY KEY (membership_hist_id)
) ENGINE = InnoDB;

CREATE TABLE membership_county_hist (
  membership_county_hist_id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  membership_county_id        BIGINT UNSIGNED NOT NULL,
  membership_id               BIGINT UNSIGNED NOT NULL,
  county_id                   BIGINT UNSIGNED NOT NULL,
  net_mineral_acres           INT UNSIGNED NOT NULL,
  surface_acres               INT UNSIGNED NOT NULL,
  voting_ind                  BOOLEAN DEFAULT 0 NOT NULL,
  active_ind                  BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm                DATETIME NOT NULL,
  create_id                   BIGINT UNSIGNED NOT NULL,
  updt_dt_tm                  DATETIME NOT NULL,
  updt_id                     BIGINT UNSIGNED NOT NULL,
  updt_cnt                    INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT membership_county_hist_pk PRIMARY KEY (membership_county_hist_id)
) ENGINE = InnoDB;

CREATE TABLE member_type_hist (
  member_type_hist_id    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  member_type_id         BIGINT UNSIGNED NOT NULL,
  prev_member_type_id    BIGINT UNSIGNED DEFAULT 0 NOT NULL,
  member_type_display    VARCHAR(50) NOT NULL,
  member_type_meaning    VARCHAR(25) NOT NULL,
  dues_amount            NUMERIC(10,2) DEFAULT 0.0 NOT NULL,
  primary_ind            BOOLEAN DEFAULT 1 NOT NULL,
  allow_spouse_ind       BOOLEAN DEFAULT 1 NOT NULL,
  allow_member_ind       BOOLEAN DEFAULT 0 NOT NULL,
  beg_eff_dt_tm          DATETIME NOT NULL,
  end_eff_dt_tm          DATETIME NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT member_type_hist_pk PRIMARY KEY (member_type_hist_id)
) ENGINE = InnoDB;

CREATE TABLE member_hist (
  member_hist_id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  member_id              BIGINT UNSIGNED NOT NULL,
  membership_id          BIGINT UNSIGNED NOT NULL,
  person_id              BIGINT UNSIGNED NULL,
  company_name           VARCHAR(250) NULL,
  company_name_key       VARCHAR(250) NULL,
  owner_ident            VARCHAR(15) NOT NULL,
  member_type_id         BIGINT UNSIGNED NOT NULL,
  greeting               VARCHAR(25) NULL,
  in_care_of             VARCHAR(50) NULL,
  join_dt                DATE NULL,
  mail_newsletter_ind    BOOLEAN DEFAULT 1 NOT NULL,
  email_newsletter_ind   BOOLEAN DEFAULT 0 NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT member_hist_pk PRIMARY KEY (member_hist_id)
) ENGINE = InnoDB;

CREATE TABLE comment_hist (
  comment_hist_id        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  comment_id             BIGINT UNSIGNED NOT NULL,
  parent_entity_id       BIGINT UNSIGNED NOT NULL,
  parent_entity_name     VARCHAR(25) NOT NULL,
  comment_dt             DATE NOT NULL,
  comment_txt            VARCHAR(1000) NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT comment_hist_pk PRIMARY KEY (comment_hist_id)
) ENGINE = InnoDB;

CREATE TABLE transaction_hist (
  transaction_hist_id    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  transaction_id         BIGINT UNSIGNED NOT NULL,
  membership_id          BIGINT UNSIGNED NOT NULL,
  transaction_dt         DATE NOT NULL,
  transaction_type_flag  INT NOT NULL COMMENT '0 = Invoice, 1 = Payment',
  transaction_desc       VARCHAR(50) NULL,
  ref_num                VARCHAR(25) NULL,
  memo_txt               VARCHAR(250) NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT transaction_hist_pk PRIMARY KEY (transaction_hist_id)
) ENGINE = InnoDB;

CREATE TABLE transaction_entry_hist (
  transaction_entry_hist_id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  transaction_entry_id        BIGINT UNSIGNED NOT NULL,
  transaction_id              BIGINT UNSIGNED NOT NULL,
  related_transaction_id      BIGINT UNSIGNED NULL,
  member_id                   BIGINT UNSIGNED NULL,
  transaction_entry_amount    NUMERIC(10, 2) NOT NULL,
  transaction_entry_type_cd   BIGINT UNSIGNED NOT NULL,
  active_ind                  BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm                DATETIME NOT NULL,
  create_id                   BIGINT UNSIGNED NOT NULL,
  updt_dt_tm                  DATETIME NOT NULL,
  updt_id                     BIGINT UNSIGNED NOT NULL,
  updt_cnt                    INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT transaction_entry_hist_pk PRIMARY KEY (transaction_entry_hist_id)
) ENGINE = InnoDB;

CREATE TABLE deposit_hist (
  deposit_hist_id        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  deposit_id             BIGINT UNSIGNED NOT NULL,
  deposit_ref            VARCHAR(25) NOT NULL,
  deposit_dt             DATE NOT NULL,
  deposit_amount         NUMERIC(10, 2) NOT NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT deposit_hist_pk PRIMARY KEY (deposit_hist_id)
) ENGINE = InnoDB;

delimiter |

CREATE TRIGGER county_history AFTER UPDATE ON county
  FOR EACH ROW BEGIN
    INSERT INTO county_hist (county_id, state_code, county_code, county_name, swkroa_county_ind, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.county_id, old.state_code, old.county_code, old.county_name, old.swkroa_county_ind, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|



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

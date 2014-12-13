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
  CONSTRAINT county_hist_pk  PRIMARY KEY (county_hist_id),
  CONSTRAINT county_hist_fk1 FOREIGN KEY (county_id) REFERENCES county (county_id)
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
  CONSTRAINT codeset_hist_pk  PRIMARY KEY (codeset_hist_id),
  CONSTRAINT codeset_hist_fk1 FOREIGN KEY (codeset_id) REFERENCES codeset (codeset_id)
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
  CONSTRAINT codevalue_hist_pk  PRIMARY KEY (codevalue_hist_id),
  CONSTRAINT codevalue_hist_fk1 FOREIGN KEY (codevalue_id) REFERENCES codevalue (codevalue_id)
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
  CONSTRAINT person_hist_pk  PRIMARY KEY (person_hist_id),
  CONSTRAINT person_hist_fk1 FOREIGN KEY (person_id) REFERENCES person (person_id)
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
  CONSTRAINT address_hist_pk  PRIMARY KEY (address_hist_id),
  CONSTRAINT address_hist_fk1 FOREIGN KEY (address_id) REFERENCES address (address_id)
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
  CONSTRAINT phone_hist_pk  PRIMARY KEY (phone_hist_id),
  CONSTRAINT phone_hist_fk1 FOREIGN KEY (phone_id) REFERENCES phone (phone_id)
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
  CONSTRAINT email_hist_pk  PRIMARY KEY (email_hist_id),
  CONSTRAINT email_hist_fk1 FOREIGN KEY (email_id) REFERENCES email (email_id)
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
  CONSTRAINT user_hist_pk  PRIMARY KEY (user_hist_id),
  CONSTRAINT user_hist_fk1 FOREIGN KEY (user_id) REFERENCES user (user_id)
) ENGINE = InnoDB;

CREATE TABLE membership_hist (
  membership_hist_id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  membership_id          BIGINT UNSIGNED NOT NULL,
  entity_type_cd         BIGINT UNSIGNED NOT NULL,
  next_due_dt            DATE NOT NULL,
  dues_amount            NUMERIC(10,2) NULL,
  close_reason_id        BIGINT UNSIGNED NULL,
  close_reason_txt       VARCHAR(100) NULL,
  close_dt_tm            DATETIME NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT membership_hist_pk  PRIMARY KEY (membership_hist_id),
  CONSTRAINT membership_hist_fk1 FOREIGN KEY (membership_id) REFERENCES membership (membership_id)
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
  CONSTRAINT membership_county_hist_pk  PRIMARY KEY (membership_county_hist_id),
  CONSTRAINT membership_county_hist_fk1 FOREIGN KEY (membership_county_id) REFERENCES membership_county (membership_county_id)
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
  CONSTRAINT member_type_hist_pk  PRIMARY KEY (member_type_hist_id),
  CONSTRAINT member_type_hist_fk1 FOREIGN KEY (member_type_id) REFERENCES member_type (member_type_id)
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
  close_reason_id        BIGINT UNSIGNED NULL,
  close_reason_txt       VARCHAR(100) NULL,
  close_dt_tm            DATETIME NULL,
  active_ind             BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm           DATETIME NOT NULL,
  create_id              BIGINT UNSIGNED NOT NULL,
  updt_dt_tm             DATETIME NOT NULL,
  updt_id                BIGINT UNSIGNED NOT NULL,
  updt_cnt               INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT member_hist_pk  PRIMARY KEY (member_hist_id),
  CONSTRAINT member_hist_fk1 FOREIGN KEY (member_id) REFERENCES member (member_id)
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
  CONSTRAINT comment_hist_pk  PRIMARY KEY (comment_hist_id),
  CONSTRAINT comment_hist_fk1 FOREIGN KEY (comment_id) REFERENCES comment (comment_id)
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
  CONSTRAINT transaction_hist_pk  PRIMARY KEY (transaction_hist_id),
  CONSTRAINT transaction_hist_fk1 FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id)
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
  CONSTRAINT transaction_entry_hist_pk  PRIMARY KEY (transaction_entry_hist_id),
  CONSTRAINT transaction_entry_hist_fk1 FOREIGN KEY (transaction_entry_id) REFERENCES transaction_entry (transaction_entry_id)
) ENGINE = InnoDB;

CREATE TABLE deposit_hist (
  deposit_hist_id             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  deposit_id                  BIGINT UNSIGNED NOT NULL,
  deposit_ref                 VARCHAR(25) NOT NULL,
  deposit_dt                  DATE NOT NULL,
  deposit_amount              NUMERIC(10, 2) NOT NULL,
  active_ind                  BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm                DATETIME NOT NULL,
  create_id                   BIGINT UNSIGNED NOT NULL,
  updt_dt_tm                  DATETIME NOT NULL,
  updt_id                     BIGINT UNSIGNED NOT NULL,
  updt_cnt                    INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT deposit_hist_pk  PRIMARY KEY (deposit_hist_id),
  CONSTRAINT depoist_hist_fk1 FOREIGN KEY (deposit_id) REFERENCES deposit (deposit_id)
) ENGINE = InnoDB;

CREATE TABLE deposit_transaction_hist (
  deposit_transaction_hist_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  deposit_transaction_id      BIGINT UNSIGNED NOT NULL,
  deposit_id                  BIGINT UNSIGNED NOT NULL,
  transaction_id              BIGINT UNSIGNED NOT NULL,
  active_ind                  BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm                DATETIME NOT NULL,
  create_id                   BIGINT UNSIGNED NOT NULL,
  updt_dt_tm                  DATETIME NOT NULL,
  updt_id                     BIGINT UNSIGNED NOT NULL,
  updt_cnt                    INT UNSIGNED DEFAULT 0 NOT NULL,
  CONSTRAINT deposit_transaction_hist_pk  PRIMARY KEY (deposit_transaction_hist_id),
  CONSTRAINT depoist_transaction_hist_fk1 FOREIGN KEY (deposit_transaction_id) REFERENCES deposit_transaction (deposit_transaction_id)
) ENGINE = InnoDB;

delimiter |

CREATE TRIGGER county_history AFTER UPDATE ON county
  FOR EACH ROW BEGIN
    INSERT INTO county_hist (county_id, state_code, county_code, county_name, swkroa_county_ind, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.county_id, old.state_code, old.county_code, old.county_name, old.swkroa_county_ind, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER codeset_history AFTER UPDATE ON codeset
  FOR EACH ROW BEGIN
    INSERT INTO codeset_hist (codeset_id, codeset_display, codeset_meaning, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.codeset_id, old.codeset_display, old.codeset_meaning, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER codevalue_history AFTER UPDATE ON codevalue
  FOR EACH ROW BEGIN
    INSERT INTO codevalue_hist (codevalue_id, codeset_id, codevalue_display, codevalue_meaning, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.codevalue_id, old.codeset_id, old.codevalue_display, old.codevalue_meaning, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER person_history AFTER UPDATE ON person
  FOR EACH ROW BEGIN
    INSERT INTO person_hist (person_id, title_cd, name_last, name_last_key, name_middle, name_first, name_first_key, locale_language, locale_country, time_zone, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.person_id, old.title_cd, old.name_last, old.name_last_key, old.name_middle, old.name_first, old.name_first_key, old.locale_language, old.locale_country, old.time_zone, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER address_history AFTER UPDATE ON address
  FOR EACH ROW BEGIN
    INSERT INTO address_hist (address_id, parent_entity_id, parent_entity_name, address_type_cd, address1, address2, address3, city, state_code, country_code, postal_code, primary_ind, verified_cd, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.address_id, old.parent_entity_id, old.parent_entity_name, old.address_type_cd, old.address1, old.address2, old.address3, old.city, old.state_code, old.country_code, old.postal_code, old.primary_ind, old.verified_cd, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER phone_history AFTER UPDATE ON phone
  FOR EACH ROW BEGIN
    INSERT INTO phone_hist (phone_id, parent_entity_id, parent_entity_name, phone_type_cd, phone_number, phone_extension, primary_ind, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.phone_id, old.parent_entity_id, old.parent_entity_name, old.phone_type_cd, old.phone_number, old.phone_extension, old.primary_ind, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER email_history AFTER UPDATE ON email
  FOR EACH ROW BEGIN
    INSERT INTO email_hist (email_id, parent_entity_id, parent_entity_name, email_type_cd, email_address, primary_ind, verified_cd, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.email_id, old.parent_entity_id, old.parent_entity_name, old.email_type_cd, old.email_address, old.primary_ind, old.verified_cd, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER user_history AFTER UPDATE ON user
  FOR EACH ROW BEGIN
    IF old.username != new.username OR
       old.password != new.password OR
       old.temporary_pwd_ind != new.temporary_pwd_ind OR
       old.account_locked_dt_tm != new.account_locked_dt_tm OR
       old.active_ind != new.active_ind THEN
      INSERT INTO user_hist (user_id, person_id, username, password, last_signin_dt_tm, last_signin_ip, signin_attempts, temporary_pwd_ind, account_locked_dt_tm, account_expired_dt_tm, password_changed_dt_tm, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
      VALUES (old.user_id, old.person_id, old.username, old.password, old.last_signin_dt_tm, old.last_signin_ip, old.signin_attempts, old.temporary_pwd_ind, old.account_locked_dt_tm, old.account_expired_dt_tm, old.password_changed_dt_tm, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
    END IF;
  END;
|

CREATE TRIGGER membership_history AFTER UPDATE ON membership
  FOR EACH ROW BEGIN
    INSERT INTO membership_hist (membership_id, entity_type_cd, next_due_dt, dues_amount, active_ind, close_reason_id, close_reason_txt, close_dt_tm, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.membership_id, old.entity_type_cd, old.next_due_dt, old.dues_amount, old.active_ind, old.close_reason_id, old.close_reason_txt, old.close_dt_tm, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER membership_county_history AFTER UPDATE ON membership_county
  FOR EACH ROW BEGIN
    INSERT INTO membership_county_hist (membership_county_id, membership_id, county_id, net_mineral_acres, surface_acres, voting_ind, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.membership_county_id, old.membership_id, old.county_id, old.net_mineral_acres, old.surface_acres, old.voting_ind, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER member_type_history AFTER UPDATE ON member_type
  FOR EACH ROW BEGIN
    INSERT INTO member_type_hist (member_type_id, prev_member_type_id, member_type_display, member_type_meaning, dues_amount, primary_ind, allow_spouse_ind, allow_member_ind, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.member_type_id, old.prev_member_type_id, old.member_type_display, old.member_type_meaning, old.dues_amount, old.primary_ind, old.allow_spouse_ind, old.allow_member_ind, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER member_history AFTER UPDATE ON member
  FOR EACH ROW BEGIN
    INSERT INTO member_hist (member_id, membership_id, person_id, company_name, company_name_key, owner_ident, member_type_id, greeting, in_care_of, join_dt, mail_newsletter_ind, email_newsletter_ind, active_ind, close_reason_id, close_reason_txt, close_dt_tm, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.member_id, old.membership_id, old.person_id, old.company_name, old.company_name_key, old.owner_ident, old.member_type_id, old.greeting, old.in_care_of, old.join_dt, old.mail_newsletter_ind, old.email_newsletter_ind, old.active_ind, old.close_reason_id, old.close_reason_txt, old.close_dt_tm, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER comment_history AFTER UPDATE ON comment
  FOR EACH ROW BEGIN
    INSERT INTO comment_hist (comment_id, parent_entity_id, parent_entity_name, comment_dt, comment_txt, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.comment_id, old.parent_entity_id, old.parent_entity_name, old.comment_dt, old.comment_txt, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER transaction_history AFTER UPDATE ON transaction
  FOR EACH ROW BEGIN
    INSERT INTO transaction_hist (transaction_id, membership_id, transaction_dt, transaction_type_flag, transaction_desc, ref_num, memo_txt, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.transaction_id, old.membership_id, old.transaction_dt, old.transaction_type_flag, old.transaction_desc, old.ref_num, old.memo_txt, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER transaction_entry_history AFTER UPDATE ON transaction_entry
  FOR EACH ROW BEGIN
    INSERT INTO transaction_entry_hist (transaction_entry_id, transaction_id, related_transaction_id, member_id, transaction_entry_amount, transaction_entry_type_cd, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.transaction_entry_id, old.transaction_id, old.related_transaction_id, old.member_id, old.transaction_entry_amount, old.transaction_entry_type_cd, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER deposit_history AFTER UPDATE ON deposit
  FOR EACH ROW BEGIN
    INSERT INTO deposit_hist (deposit_id, deposit_ref, deposit_dt, deposit_amount, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.deposit_id, old.deposit_ref, old.deposit_dt, old.deposit_amount, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

CREATE TRIGGER deposit_transaction_history AFTER UPDATE ON deposit_transaction
  FOR EACH ROW BEGIN
    INSERT INTO deposit_transaction_hist (deposit_transaction_id, deposit_id, transaction_id, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.deposit_transaction_id, old.deposit_id, old.transaction_id, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

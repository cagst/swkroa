DROP TABLE IF EXISTS transaction_entry;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS member_type;
DROP TABLE IF EXISTS membership_county;
DROP TABLE IF EXISTS membership;
DROP TABLE IF EXISTS codevalue;
DROP TABLE IF EXISTS codeset;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS email;
DROP TABLE IF EXISTS phone;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS role_privilege;
DROP TABLE IF EXISTS privilege;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS county;
DROP TABLE IF EXISTS audit_log;
DROP TABLE IF EXISTS comment;

CREATE TABLE county (
  county_id                 BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  state_code                CHAR(2) NOT NULL,
  county_code               CHAR(2) NOT NULL,
  county_name               VARCHAR(50) NOT NULL,
  swkroa_county_ind         BOOLEAN DEFAULT 0 NOT NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              DATETIME NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                DATETIME NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL
);

CREATE TABLE audit_log (
  audit_log_id              BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  audit_event_type          INT NOT NULL,
  audit_action              VARCHAR(50) NOT NULL,
  audit_instigator          VARCHAR(50) NOT NULL,
  audit_message             VARCHAR(250) NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NULL,
);

CREATE TABLE privilege (
  privilege_id              BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  privilege_key             VARCHAR(25) NOT NULL,
  privilege_name            VARCHAR(50) NOT NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL
);

CREATE TABLE role (
  role_id                   BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  role_key                  VARCHAR(25) NOT NULL,
  role_name                 VARCHAR(50) NOT NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL
);

CREATE TABLE role_privilege (
  role_privilege_id         BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  role_id                   BIGINT NOT NULL,
  privilege_id              BIGINT NOT NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL,
  CONSTRAINT role_privilege_fk1 FOREIGN KEY (role_id) REFERENCES role (role_id),
  CONSTRAINT role_privilege_fk2 FOREIGN KEY (privilege_id) REFERENCES privilege (privilege_id)
);

CREATE TABLE person (
  person_id                 BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  title_cd                  BIGINT NULL,
  name_last                 VARCHAR(50) NOT NULL,
  name_last_key             VARCHAR(50) NOT NULL,
  name_first                VARCHAR(50) NOT NULL,
  name_first_key            VARCHAR(50) NOT NULL,
  name_middle               VARCHAR(50) NULL,
  locale_language           VARCHAR(10) NULL,
  locale_country            VARCHAR(10) NULL,
  time_zone                 VARCHAR(50) NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL
);

CREATE TABLE address (
  address_id                BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  parent_entity_id          BIGINT NOT NULL,
  parent_entity_name        VARCHAR(25) NOT NULL,
  address_type_cd           BIGINT NOT NULL,
  address1                  VARCHAR(50) NOT NULL,
  address2                  VARCHAR(50) NULL,
  address3                  VARCHAR(50) NULL,
  city                      VARCHAR(50) NOT NULL,
  state_code                CHAR(2) NOT NULL,
  country_code              CHAR(2) NOT NULL,
  postal_code               VARCHAR(15) NOT NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL
);

CREATE TABLE phone (
  phone_id                  BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  parent_entity_id          BIGINT NOT NULL,
  parent_entity_name        VARCHAR(25) NOT NULL,
  phone_type_cd             BIGINT NOT NULL,
  phone_number              VARCHAR(25) NOT NULL,
  phone_extension           VARCHAR(10) NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL
);

CREATE TABLE email (
  email_id                  BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  parent_entity_id          BIGINT NOT NULL,
  parent_entity_name        VARCHAR(25) NOT NULL,
  email_type_cd             BIGINT NOT NULL,
  email_address             VARCHAR(256) NOT NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL
);

CREATE TABLE user (
  user_id                   BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  person_id                 BIGINT NOT NULL,
  username                  VARCHAR(50) NOT NULL,
  password                  VARCHAR(60) NOT NULL,
  last_signin_dt_tm         DATETIME NULL,
  last_signin_ip            VARCHAR(25) NULL,
  signin_attempts           INT DEFAULT 0 NOT NULL,
  temporary_pwd_ind         BOOLEAN DEFAULT 1 NOT NULL,
  account_locked_dt_tm      DATETIME NULL,
  account_expired_dt_tm     DATETIME NULL,
  password_changed_dt_tm    DATETIME NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL,
  CONSTRAINT user_fk1 FOREIGN KEY (person_id) REFERENCES person (person_id)
);

CREATE TABLE user_role (
  user_role_id              BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  user_id                   BIGINT NOT NULL,
  role_id                   BIGINT NOT NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL,
  CONSTRAINT user_role_fk1 FOREIGN KEY (user_id) REFERENCES user (user_id),
  CONSTRAINT user_role_fk2 FOREIGN KEY (role_id) REFERENCES role (role_id)
);

CREATE TABLE codeset (
  codeset_id                BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  codeset_display           VARCHAR(50) NOT NULL,
  codeset_meaning           VARCHAR(25) NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL
);

CREATE TABLE codevalue (
  codevalue_id              BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  codeset_id                BIGINT NOT NULL,
  codevalue_display         VARCHAR(50) NOT NULL,
  codevalue_meaning         VARCHAR(25) NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL,
  CONSTRAINT codevalue_fk1 FOREIGN KEY (codeset_id) REFERENCES codeset (codeset_id)
);

CREATE TABLE membership (
  membership_id             BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  entity_type_cd            BIGINT NOT NULL,
  next_due_dt               DATE NOT NULL,
  dues_amount               NUMERIC(10,2) NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              DATETIME NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                DATETIME NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL
);

CREATE TABLE member_type (
  member_type_id            BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  prev_member_type_id       BIGINT DEFAULT 0 NOT NULL,
  member_type_display       VARCHAR(50) NOT NULL,
  member_type_meaning       VARCHAR(25) NOT NULL,
  dues_amount               NUMERIC(10,2) DEFAULT 0.0 NOT NULL,
  primary_ind               BOOLEAN DEFAULT 1 NOT NULL,
  allow_spouse_ind          BOOLEAN DEFAULT 1 NOT NULL,
  allow_member_ind          BOOLEAN DEFAULT 0 NOT NULL,
  beg_eff_dt_tm             TIMESTAMP NOT NULL,
  end_eff_dt_tm             TIMESTAMP NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL
);

CREATE TABLE member (
  member_id                 BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  membership_id             BIGINT NOT NULL,
  person_id                 BIGINT NULL,
  company_name              VARCHAR(250) NULL,
  company_name_key          VARCHAR(250) NULL,
  owner_ident               VARCHAR(15) NOT NULL,
  member_type_id            BIGINT NOT NULL,
  greeting                  VARCHAR(25) NULL,
  in_care_of                VARCHAR(25) NULL,
  join_dt                   DATE NULL,
  mail_newsletter_ind       BOOLEAN DEFAULT 1 NOT NULL,
  email_newsletter_ind      BOOLEAN DEFAULT 0 NOT NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              TIMESTAMP NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                TIMESTAMP NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL
);

CREATE TABLE membership_county (
  membership_county_id      BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  membership_id             BIGINT NOT NULL,
  county_id                 BIGINT NOT NULL,
  net_mineral_acres         INT NOT NULL,
  surface_acres             INT NOT NULL,
  voting_ind                BOOLEAN DEFAULT 0 NOT NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              DATETIME NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                DATETIME NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL,
  CONSTRAINT membership_county_fk1 FOREIGN KEY (membership_id) REFERENCES membership (membership_id),
  CONSTRAINT membership_county_fk2 FOREIGN KEY (county_id) REFERENCES county (county_id)
);

CREATE TABLE comment (
  comment_id                BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  parent_entity_id          BIGINT NOT NULL,
  parent_entity_name        VARCHAR(25) NOT NULL,
  comment_dt                DATE NOT NULL,
  comment_txt               VARCHAR(1000) NOT NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              DATETIME NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                DATETIME NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL
);

CREATE TABLE transaction (
  transaction_id            BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  membership_id             BIGINT NOT NULL,
  transaction_dt            DATE NOT NULL,
  transaction_type_flag     BIGINT NOT NULL,
  transaction_desc          VARCHAR(50) NULL,
  ref_num                   VARCHAR(25) NULL,
  memo_txt                  VARCHAR(250) NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              DATETIME NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                DATETIME NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL,
  CONSTRAINT transaction_fk1 FOREIGN KEY (membership_id) REFERENCES membership (membership_id)
);

CREATE TABLE transaction_entry (
  transaction_entry_id      BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY,
  transaction_id            BIGINT NOT NULL,
  related_transaction_id    BIGINT NULL,
  member_id                 BIGINT NULL,
  transaction_entry_amount  NUMERIC(10, 2) NOT NULL,
  transaction_entry_type_cd BIGINT NOT NULL,
  active_ind                BOOLEAN DEFAULT 1 NOT NULL,
  create_dt_tm              DATETIME NOT NULL,
  create_id                 BIGINT NOT NULL,
  updt_dt_tm                DATETIME NOT NULL,
  updt_id                   BIGINT NOT NULL,
  updt_cnt                  INT DEFAULT 0 NOT NULL,
  CONSTRAINT transaction_entry_fk1 FOREIGN KEY (transaction_id) REFERENCES transaction (transaction_id),
  CONSTRAINT transaction_entry_fk2 FOREIGN KEY (member_id) REFERENCES member (member_id),
  CONSTRAINT transaction_entry_fk3 FOREIGN KEY (related_transaction_id) REFERENCES transaction (transaction_id)
);

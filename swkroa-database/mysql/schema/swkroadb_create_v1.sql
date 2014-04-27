CREATE TABLE audit_log (
	audit_log_id					BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	audit_event_type			INT NOT NULL,
	audit_action					VARCHAR(50) NOT NULL,
	audit_instigator			VARCHAR(50) NOT NULL,
	audit_message					VARCHAR(250) NULL,
	create_dt_tm					DATETIME NOT NULL,
	CONSTRAINT audit_log_pk PRIMARY KEY (audit_log_id),
	INDEX audit_log_idx1 (create_dt_tm)
) ENGINE = InnoDB;

CREATE TABLE county (
	county_id							BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	state_code						CHAR(2) NOT NULL,
	county_code						CHAR(2) NOT NULL,
	county_name						VARCHAR(50) NOT NULL,
	swkroa_county_ind			BOOLEAN DEFAULT 0 NOT NULL,
	active_ind						BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm					DATETIME NOT NULL,
	create_id							BIGINT UNSIGNED NOT NULL,
	updt_dt_tm						DATETIME NOT NULL,
	updt_id								BIGINT UNSIGNED NOT NULL,
	updt_cnt							INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT county_pk PRIMARY KEY (county_id),
	INDEX county_idx1 (state_code, county_code)
) ENGINE = InnoDB;

CREATE TABLE codeset (
	codeset_id						BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	codeset_display				VARCHAR(50) NOT NULL,
	codeset_meaning				VARCHAR(25) NULL,
	active_ind						BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm					DATETIME NOT NULL,
	create_id							BIGINT UNSIGNED NOT NULL,
	updt_dt_tm						DATETIME NOT NULL,
	updt_id								BIGINT UNSIGNED NOT NULL,
	updt_cnt							INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT codeset_pk PRIMARY KEY (codeset_id),
	INDEX codeset_idx1 (codeset_meaning)
) ENGINE = InnoDB;

CREATE TABLE codevalue (
	codevalue_id					BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	codeset_id						BIGINT UNSIGNED NOT NULL,
	codevalue_display			VARCHAR(50) NOT NULL,
	codevalue_meaning			VARCHAR(25) NULL,
	active_ind						BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm					DATETIME NOT NULL,
	create_id							BIGINT UNSIGNED NOT NULL,
	updt_dt_tm						DATETIME NOT NULL,
	updt_id								BIGINT UNSIGNED NOT NULL,
	updt_cnt							INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT codevalue_pk PRIMARY KEY (codevalue_id),
	CONSTRAINT codevalue_fk1 FOREIGN KEY (codeset_id) REFERENCES codeset (codeset_id),
	INDEX codevalue_idx1 (codevalue_meaning)
) ENGINE = InnoDB;

CREATE TABLE person (
	person_id							BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	title_cd							BIGINT NULL,
	name_last							VARCHAR(50) NOT NULL,
	name_last_key					VARCHAR(50) NOT NULL,
	name_first						VARCHAR(50) NOT NULL,
	name_first_key				VARCHAR(50) NOT NULL,
	name_middle						VARCHAR(50) NULL,
	dob_dt								DATE NULL,
	gender_cd							BIGINT UNSIGNED NULL,
	locale_language				VARCHAR(10) NULL,
	locale_country				VARCHAR(10) NULL,
	time_zone							VARCHAR(50) NULL,
	active_ind						BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm					DATETIME NOT NULL,
	create_id							BIGINT UNSIGNED NOT NULL,
	updt_dt_tm						DATETIME NOT NULL,
	updt_id								BIGINT UNSIGNED NOT NULL,
	updt_cnt							INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT person_pk PRIMARY KEY (person_id),
	INDEX person_idx1 (name_last_key),
	INDEX person_idx2 (name_first_key)
) ENGINE = InnoDB;

CREATE TABLE address (
	address_id						BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	parent_entity_id			BIGINT UNSIGNED NOT NULL,
	parent_entity_name		VARCHAR(25) NOT NULL,
	address_type_cd				BIGINT UNSIGNED NOT NULL,
	address1							VARCHAR(50) NOT NULL,
	address2							VARCHAR(50) NULL,
	address3							VARCHAR(50) NULL,
	city									VARCHAR(50) NOT NULL,
	state_code						CHAR(2) NOT NULL,
	country_code					CHAR(2) NOT NULL,
	postal_code						VARCHAR(15) NOT NULL,
	primary_ind						BOOLEAN DEFAULT 0 NOT NULL,
	verified_cd						BIGINT UNSIGNED NULL,
	active_ind						BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm					DATETIME NOT NULL,
	create_id							BIGINT UNSIGNED NOT NULL,
	updt_dt_tm						DATETIME NOT NULL,
	updt_id								BIGINT UNSIGNED NOT NULL,
	updt_cnt							INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT address_pk PRIMARY KEY (address_id),
	INDEX address_idx1 (parent_entity_name, parent_entity_id)
) ENGINE = InnoDB;

CREATE TABLE phone (
	phone_id							BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	parent_entity_id			BIGINT UNSIGNED NOT NULL,
	parent_entity_name		VARCHAR(25) NOT NULL,
	phone_type_cd					BIGINT UNSIGNED NOT NULL,
	phone_number					VARCHAR(25) NOT NULL,
	phone_extension				VARCHAR(10) NULL,
	primary_ind						BOOLEAN DEFAULT 0 NOT NULL,
	active_ind						BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm					DATETIME NOT NULL,
	create_id							BIGINT UNSIGNED NOT NULL,
	updt_dt_tm						DATETIME NOT NULL,
	updt_id								BIGINT UNSIGNED NOT NULL,
	updt_cnt							INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT phone_pk PRIMARY KEY (phone_id),
	INDEX phone_idx1 (parent_entity_name, parent_entity_id)
) ENGINE = InnoDB;

CREATE TABLE email (
	email_id							BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	parent_entity_id			BIGINT UNSIGNED NOT NULL,
	parent_entity_name		VARCHAR(25) NOT NULL,
	email_type_cd					BIGINT UNSIGNED NOT NULL,
	email_address					VARCHAR(256) NOT NULL,
	primary_ind						BOOLEAN DEFAULT 0 NOT NULL,
	verified_cd						BIGINT UNSIGNED NULL,
	active_ind						BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm					DATETIME NOT NULL,
	create_id							BIGINT UNSIGNED NOT NULL,
	updt_dt_tm						DATETIME NOT NULL,
	updt_id								BIGINT UNSIGNED NOT NULL,
	updt_cnt							INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT email_pk PRIMARY KEY (email_id),
	INDEX email_idx1 (parent_entity_name, parent_entity_id)
) ENGINE = InnoDB;

CREATE TABLE user (
	user_id									BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	person_id								BIGINT UNSIGNED NOT NULL,
	username								VARCHAR(50) NOT NULL,
	password								CHAR(60) NOT NULL,
	last_signin_dt_tm				DATETIME NULL,
	last_signin_ip					VARCHAR(50) NULL,
	signin_attempts					INT UNSIGNED DEFAULT 0 NOT NULL,
	temporary_pwd_ind				BOOLEAN DEFAULT 1 NOT NULL,
	account_locked_dt_tm		DATETIME NULL,
	account_expired_dt_tm		DATETIME NULL,
	password_changed_dt_tm	DATETIME NULL,
	active_ind							BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm						DATETIME NOT NULL,
	create_id								BIGINT UNSIGNED NOT NULL,
	updt_dt_tm							DATETIME NOT NULL,
	updt_id									BIGINT UNSIGNED NOT NULL,
	updt_cnt								INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (user_id),
	CONSTRAINT user_fk1 FOREIGN KEY (person_id) REFERENCES person (person_id),
	INDEX user_idx1 (username)
) ENGINE = InnoDB;

CREATE TABLE membership (
	membership_id						BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	membership_type_cd			BIGINT UNSIGNED NOT NULL,
	entity_type_cd					BIGINT UNSIGNED NOT NULL,
	due_on_dt								DATE NOT NULL,
	dues_amount							NUMERIC(10,2) NULL,
	active_ind							BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm						DATETIME NOT NULL,
	create_id								BIGINT UNSIGNED NOT NULL,
	updt_dt_tm							DATETIME NOT NULL,
	updt_id									BIGINT UNSIGNED NOT NULL,
	updt_cnt								INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT membership_pk PRIMARY KEY (membership_id),
	INDEX membership_idx1 (membership_type_cd)
) ENGINE = InnoDB;

CREATE TABLE membership_county (
	membership_county_id		BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	membership_id						BIGINT UNSIGNED NOT NULL,
	county_id								BIGINT UNSIGNED NOT NULL,
	net_mineral_acres				INT UNSIGNED NOT NULL,
	surface_acres						INT UNSIGNED NOT NULL,
	voting_ind							BOOLEAN DEFAULT 0 NOT NULL,
	active_ind							BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm						DATETIME NOT NULL,
	create_id								BIGINT UNSIGNED NOT NULL,
	updt_dt_tm							DATETIME NOT NULL,
	updt_id									BIGINT UNSIGNED NOT NULL,
	updt_cnt								INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT membership_county_pk PRIMARY KEY (membership_county_id),
	CONSTRAINT membership_county_fk1 FOREIGN KEY (membership_id) REFERENCES membership (membership_id),
	CONSTRAINT membership_county_fk2 FOREIGN KEY (county_id) REFERENCES county (county_id)
) ENGINE = InnoDB;

CREATE TABLE member_type (
	member_type_id					BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	prev_member_type_id			BIGINT UNSIGNED DEFAULT 0 NOT NULL,
	member_type_display			VARCHAR(50) NOT NULL,
	member_type_meaning			VARCHAR(25) NOT NULL,
	dues_amount							NUMERIC(10,2) DEFAULT 0.0 NOT NULL,
	beg_eff_dt_tm						DATETIME NOT NULL,
	end_eff_dt_tm						DATETIME NULL,
	active_ind							BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm						DATETIME NOT NULL,
	create_id								BIGINT UNSIGNED NOT NULL,
	updt_dt_tm							DATETIME NOT NULL,
	updt_id									BIGINT UNSIGNED NOT NULL,
	updt_cnt								INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT member_type_pk PRIMARY KEY (member_type_id),
	INDEX member_type_idx1 (member_type_meaning)
) ENGINE = InnoDB;

CREATE TABLE member (
	member_id								BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	membership_id						BIGINT UNSIGNED NOT NULL,
	person_id								BIGINT UNSIGNED NULL,
	company_name						VARCHAR(250) NULL,
	company_name_key				VARCHAR(250) NULL,
	owner_ident							VARCHAR(15) NOT NULL,
	member_type_id					BIGINT UNSIGNED NOT NULL,
	greeting								VARCHAR(25) NULL,
	in_care_of							VARCHAR(50) NULL,
	join_dt									DATE NULL,
	mail_newsletter_ind			BOOLEAN DEFAULT 1 NOT NULL,
	email_newsletter_ind		BOOLEAN DEFAULT 0 NOT NULL,
	active_ind							BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm						DATETIME NOT NULL,
	create_id								BIGINT UNSIGNED NOT NULL,
	updt_dt_tm							DATETIME NOT NULL,
	updt_id									BIGINT UNSIGNED NOT NULL,
	updt_cnt								INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT member_pk PRIMARY KEY (member_id),
	CONSTRAINT member_fk1 FOREIGN KEY (membership_id) REFERENCES membership (membership_id),
	CONSTRAINT member_fk2 FOREIGN KEY (person_id) REFERENCES person (person_id),
	CONSTRAINT member_fk3 FOREIGN KEY (member_type_id) REFERENCES member_type (member_type_id),
	INDEX member_idx1 (owner_ident),
	INDEX member_idx2 (company_name_key)
) ENGINE = InnoDB;

CREATE TABLE comment (
	comment_id								BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	parent_entity_id					BIGINT UNSIGNED NOT NULL,
	parent_entity_name				VARCHAR(25) NOT NULL,
	comment_dt								DATE NOT NULL,
	comment_txt								VARCHAR(1000) NOT NULL,
	active_ind								BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm							DATETIME NOT NULL,
	create_id									BIGINT UNSIGNED NOT NULL,
	updt_dt_tm								DATETIME NOT NULL,
	updt_id										BIGINT UNSIGNED NOT NULL,
	updt_cnt									INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT comment_pk PRIMARY KEY (comment_id),
	INDEX comment_idx1 (parent_entity_name, parent_entity_id)
) ENGINE = InnoDB;

CREATE TABLE transaction (
	transaction_id					BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	membership_id						BIGINT UNSIGNED NOT NULL,
	member_id								BIGINT UNSIGNED NULL,
	transaction_dt					DATE NOT NULL,
	transaction_type_cd			BIGINT UNSIGNED NOT NULL,
	transaction_amount			NUMERIC(10, 2) NOT NULL,
	ref_num									VARCHAR(25) NULL,
	memo_txt								VARCHAR(250) NULL,
	active_ind							BOOLEAN DEFAULT 1 NOT NULL,
	create_dt_tm						DATETIME NOT NULL,
	create_id								BIGINT UNSIGNED NOT NULL,
	updt_dt_tm							DATETIME NOT NULL,
	updt_id									BIGINT UNSIGNED NOT NULL,
	updt_cnt								INT UNSIGNED DEFAULT 0 NOT NULL,
	CONSTRAINT transaction_pk PRIMARY KEY (transaction_id),
	CONSTRAINT transaction_fk1 FOREIGN KEY (membership_id) REFERENCES membership (membership_id),
	CONSTRAINT transaction_fk2 FOREIGN KEY (member_id) REFERENCES member (member_id),
	CONSTRAINT transaction_fk3 FOREIGN KEY (transaction_type_cd) REFERENCES codevalue (codevalue_id)
) ENGINE = InnoDB;
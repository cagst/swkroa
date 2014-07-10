INSERT INTO audit_log (audit_log_id, audit_event_type, audit_action, audit_instigator, create_dt_tm) VALUES (1, 0, 'SIGNIN_ATTEMP', 'cgaskill', CURRENT_TIMESTAMP);
INSERT INTO audit_log (audit_log_id, audit_event_type, audit_action, audit_instigator, create_dt_tm) VALUES (2, 0, 'SIGNIN_SUCCESSFUL', 'cgaskill', CURRENT_TIMESTAMP);

INSERT INTO county (state_code, county_code, county_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES ('KS', 'JO', 'Johnson', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO county (state_code, county_code, county_name, swkroa_county_ind, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES ('KS', 'SV', 'Stevents', 1, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO county (state_code, county_code, county_name, swkroa_county_ind, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES ('KS', 'GT', 'Grant', 1, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO county (state_code, county_code, county_name, swkroa_county_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, active_ind) VALUES ('KS', 'SW', 'Seward', 1, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 0);
INSERT INTO county (state_code, county_code, county_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES ('MO', 'JK', 'Jackson', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO county (state_code, county_code, county_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES ('MO', 'PL', 'Platte', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO county (state_code, county_code, county_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES ('MO', 'JO', 'Jackson', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO codeset (codeset_id, codeset_display, codeset_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 'Address Type', 'ADDRESS_TYPE', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO codeset (codeset_id, codeset_display, codeset_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 'Phone Type', 'PHONE_TYPE', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO codeset (codeset_id, codeset_display, codeset_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (3, 'Email Type', 'EMAIL_TYPE', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO codeset (codeset_id, codeset_display, codeset_meaning, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (4, 'Document Type', 'DOCUMENT_TYPE', 0, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO codevalue (codeset_id, codevalue_id, codevalue_display, codevalue_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 1, 'Home', 'HOME', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO codevalue (codeset_id, codevalue_id, codevalue_display, codevalue_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 2, 'Work', 'WORK', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO codevalue (codeset_id, codevalue_id, codevalue_display, codevalue_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 3, 'Home', 'HOME', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO codevalue (codeset_id, codevalue_id, codevalue_display, codevalue_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 4, 'Work', 'WORK', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO codevalue (codeset_id, codevalue_id, codevalue_display, codevalue_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 5, 'Fax', 'FAX', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO codevalue (codeset_id, codevalue_id, codevalue_display, codevalue_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (3, 6, 'Home', 'HOME', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO codevalue (codeset_id, codevalue_id, codevalue_display, codevalue_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (3, 7, 'Work', 'WORK', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO privilege (privilege_id, privilege_key, privilege_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 'PRIV1', 'Privilege 1', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO privilege (privilege_id, privilege_key, privilege_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 'PRIV2', 'Privilege 2', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO privilege (privilege_id, privilege_key, privilege_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (3, 'PRIV3', 'Privilege 3', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO privilege (privilege_id, privilege_key, privilege_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (4, 'PRIV4', 'Privilege 4', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO privilege (privilege_id, privilege_key, privilege_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (5, 'PRIV5', 'Privilege 5', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO privilege (privilege_id, privilege_key, privilege_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (6, 'PRIV6', 'Privilege 6', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO privilege (privilege_id, privilege_key, privilege_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (7, 'PRIV7', 'Privilege 7', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO role (role_id, role_key, role_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 'ROLE_1', 'Role 1', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role (role_id, role_key, role_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 'ROLE_2', 'Role 2', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role (role_id, role_key, role_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (3, 'ROLE_3', 'Role 3', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role (role_id, role_key, role_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (4, 'ROLE_4', 'Role 4', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 1, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 2, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 3, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 4, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 5, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 6, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 7, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 1, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 2, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 3, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 4, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (3, 4, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role_privilege (role_id, privilege_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (3, 5, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO person (person_id, name_last, name_last_key, name_first, name_first_key, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 'Gaskill', 'GASKILL', 'Craig', 'CRAIG', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO person (person_id, name_last, name_last_key, name_first, name_first_key, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 'Temp', 'TEMP', 'User', 'USER', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO person (person_id, name_last, name_last_key, name_first, name_first_key, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (3, 'Expired', 'EXPIRED', 'User', 'USER', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO person (person_id, name_last, name_last_key, name_first, name_first_key, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (4, 'Locked', 'LOCKED', 'User', 'USER', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO person (person_id, name_last, name_last_key, name_first, name_first_key, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (5, 'Locked', 'LOCKED', 'User', 'USER', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO user (user_id, person_id, username, password, temporary_pwd_ind, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (11, 1, 'cgaskill', 'password1', 0, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO user (user_id, person_id, username, password, temporary_pwd_ind, account_expired_dt_tm, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (12, 2, 'temp', 'password1', 1, '2014-12-25 15:00:00.0000', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO user (user_id, person_id, username, password, temporary_pwd_ind, account_expired_dt_tm, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (13, 3, 'expire', 'password1', 1, '2011-12-25 15:00:00.0000', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO user (user_id, person_id, username, password, temporary_pwd_ind, account_locked_dt_tm, signin_attempts, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (14, 4, 'locked', 'password1', 1, '2012-10-31 15:00:00.0000', 5, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO user (user_id, person_id, username, password, temporary_pwd_ind, account_locked_dt_tm, signin_attempts, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (15, 5, 'newlylocked', 'password1', 1, CURRENT_TIMESTAMP, 5, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO user_role (user_id, role_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (11, 1, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO user_role (user_id, role_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (12, 2, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO user_role (user_id, role_id, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (12, 3, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO address (parent_entity_id, parent_entity_name, address_type_cd, address1, city, state_code, postal_code, country_code, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 'MEMBER', 1, '23824 W 124th Court', 'Olathe', 'KS', '66061', 'US', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO address (parent_entity_id, parent_entity_name, address_type_cd, address1, address2, city, state_code, postal_code, country_code, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 'MEMBER', 2, '33017 Hwy 160', 'Cabin 3', 'South Fork', 'CO', '12345', 'US', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO address (parent_entity_id, parent_entity_name, address_type_cd, address1, city, state_code, postal_code, country_code, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 'MEMBER', 1, '107 S Washington', 'Hugoton', 'KS', '67951', 'US', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO phone (parent_entity_id, parent_entity_name, phone_type_cd, phone_number, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 'MEMBER', 1, '9137077438', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO phone (parent_entity_id, parent_entity_name, phone_type_cd, phone_number, phone_extension, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 'MEMBER', 2, '8162014449', '14449', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO phone (parent_entity_id, parent_entity_name, phone_type_cd, phone_number, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 'MEMBER', 1, '6205442087', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO email (parent_entity_id, parent_entity_name, email_type_cd, email_address, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 'MEMBER', 1, 'cgaskill23824@att.net', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO email (parent_entity_id, parent_entity_name, email_type_cd, email_address, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 'MEMBER', 2, 'cgaskill@cerner.com', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO email (parent_entity_id, parent_entity_name, email_type_cd, email_address, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 'MEMBER', 1, 'charper@pld.com', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO person (person_id, name_last, name_last_key, name_first, name_first_key, create_dt_tm, create_id, updt_dt_tm, updt_id, active_ind) VALUES (10, 'Patient', 'PATIENT', 'Test', 'TEST', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, 0);
INSERT INTO person (person_id, name_last, name_last_key, name_first, name_first_key, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (11, 'Doright', 'DORIGHT', 'Dudley', 'DUDLEY', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO person (person_id, name_last, name_last_key, name_first, name_first_key, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (12, 'Doright', 'DORIGHT', 'Doris', 'DORIS', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO person (person_id, name_last, name_last_key, name_first, name_first_key, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (13, 'Mouse', 'MOUSE', 'Dudley', 'DUDLEY', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO person (person_id, name_last, name_last_key, name_first, name_first_key, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (14, 'Mouse', 'MOUSE', 'Minni', 'MINNI', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO person (person_id, name_last, name_last_key, name_first, name_first_key, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (15, 'Member', 'MEMBER', 'Regular', 'REGULAR', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);

INSERT INTO membership (membership_id, entity_type_cd, next_due_dt, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (1, 1, '2011-01-23', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO membership (membership_id, entity_type_cd, next_due_dt, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (2, 1, '2012-01-23', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO membership (membership_id, entity_type_cd, next_due_dt, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (3, 1, '2013-10-23', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO membership (membership_id, entity_type_cd, next_due_dt, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (4, 1, '2013-04-19', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO membership (membership_id, entity_type_cd, next_due_dt, create_dt_tm, create_id, updt_dt_tm, updt_id, active_ind) VALUES (5, 1, '2010-01-23', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, 0);
INSERT INTO membership (membership_id, entity_type_cd, next_due_dt, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (6, 1, '2010-01-23', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);

INSERT INTO member_type (member_type_id, prev_member_type_id, member_type_display, member_type_meaning, dues_amount, beg_eff_dt_tm, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (1, 1, 'Regular Member', 'REGULAR', 60, '2000-01-01 00:00:00', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO member_type (member_type_id, prev_member_type_id, member_type_display, member_type_meaning, dues_amount, beg_eff_dt_tm, end_eff_dt_tm, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (2, 2, 'Family Head', 'FAMILY_HEAD', 70, '2000-01-01 00:00:00', '2012-12-31 00:00:00.000000', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO member_type (member_type_id, prev_member_type_id, member_type_display, member_type_meaning, dues_amount, beg_eff_dt_tm, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (3, 2, 'Family Head', 'FAMILY_HEAD', 85, '2013-01-01 00:00:00', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO member_type (member_type_id, prev_member_type_id, member_type_display, member_type_meaning, dues_amount, beg_eff_dt_tm, end_eff_dt_tm, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (4, 4, 'Associate Member', 'ASSOCIATE', 30, '2000-01-01 00:00:00', '2012-12-31 00:00:00.000000', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO member_type (member_type_id, prev_member_type_id, member_type_display, member_type_meaning, dues_amount, beg_eff_dt_tm, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (5, 5, 'Family Member', 'FAMILY_MEMBER', 25, '2000-01-01 00:00:00', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO member_type (member_type_id, prev_member_type_id, member_type_display, member_type_meaning, dues_amount, beg_eff_dt_tm, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (6, 6, 'Family Member 2', 'FAMILY_MEMBER', 25, '2000-01-01 00:00:00', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);

INSERT INTO member (member_id, person_id, membership_id, owner_ident, member_type_id, join_dt, create_dt_tm, create_id, updt_dt_tm, updt_id, active_ind) VALUES (1, 10, 1, 'PATTES0', 1, '2010-03-01', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, 0);
INSERT INTO member (member_id, person_id, membership_id, owner_ident, member_type_id, join_dt, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (2, 11, 2, 'DORDUD0', 1, '2011-03-01', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO member (member_id, person_id, membership_id, owner_ident, member_type_id, join_dt, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (3, 12, 2, 'DORDOR0', 1, '2011-03-01', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO member (member_id, person_id, membership_id, owner_ident, member_type_id, join_dt, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (4, 13, 3, 'MOUMIC0', 2, '2012-03-01', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO member (member_id, person_id, membership_id, owner_ident, member_type_id, join_dt, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (5, 14, 3, 'MOUMIN0', 5, '2012-06-15', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO member (member_id, person_id, membership_id, owner_ident, member_type_id, join_dt, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (6, 15, 4, 'MOUMIN0', 5, '2012-06-15', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);

INSERT INTO membership_county (membership_id, county_id, net_mineral_acres, surface_acres, voting_ind, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (1, 1, 320, 480, 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO membership_county (membership_id, county_id, net_mineral_acres, surface_acres, voting_ind, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (1, 2, 160, 160, 0, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO membership_county (membership_id, county_id, net_mineral_acres, surface_acres, voting_ind, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (2, 1, 480, 480, 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);

INSERT INTO comment (comment_id, comment_dt, comment_txt, parent_entity_id, parent_entity_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (1, '2012-06-15', 'TEST COMMENT 1', 1, 'MEMBERSHIP', CURRENT_TIMESTAMP, 11, CURRENT_TIMESTAMP, 11);
INSERT INTO comment (comment_id, comment_dt, comment_txt, parent_entity_id, parent_entity_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (2, CURRENT_TIMESTAMP, 'TEST COMMENT 2', 1, 'MEMBERSHIP', CURRENT_TIMESTAMP, 11, CURRENT_TIMESTAMP, 11);
INSERT INTO comment (comment_id, comment_dt, comment_txt, parent_entity_id, parent_entity_name, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (3, CURRENT_TIMESTAMP, 'TEST COMMENT 3', 1, 'TRANSACTION', CURRENT_TIMESTAMP, 11, CURRENT_TIMESTAMP, 11);

INSERT INTO transaction (transaction_id, membership_id, transaction_dt, transaction_type_flag, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (1, 2, '2014-03-01', 0, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO transaction_entry (transaction_id, member_id, transaction_entry_amount, transaction_entry_type_cd, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (1, 2, -70, 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO transaction_entry (transaction_id, member_id, transaction_entry_amount, transaction_entry_type_cd, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (1, 3, -20, 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);

INSERT INTO transaction (transaction_id, membership_id, transaction_dt, transaction_type_flag, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (2, 2, '2014-04-01', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO transaction_entry (transaction_id, related_transaction_id, member_id, transaction_entry_amount, transaction_entry_type_cd, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (2, 2, 1, 90, 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO transaction_entry (transaction_id, member_id, transaction_entry_amount, transaction_entry_type_cd, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (2, 2, 10, 3, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);

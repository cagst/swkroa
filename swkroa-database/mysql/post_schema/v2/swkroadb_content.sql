-- Insert roles
INSERT INTO role (role_id, role_key, role_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 'ROLE_SWKROA', 'SWKROA', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO role (role_id, role_key, role_name, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (2, 'ROLE_MEMBER', 'Member', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

-- Insert Verification Types
INSERT INTO codeset (codeset_id, codeset_display, codeset_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (8, 'Verification Type', 'VERIFICATION_TYPE', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO codevalue (codeset_id, codevalue_display, codevalue_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (8, 'Manual', 'VERIFICATION_MANUAL', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO codevalue (codeset_id, codevalue_display, codevalue_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (8, 'Automatic', 'VERIFICATION_AUTO', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

-- Insert Privileges
INSERT INTO privilege (privilege_key, privilege_name, privilege_desc, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES ('MANAGE_CODE_VALUES', 'Manage Coded Values', 'Manage the coded values defined within the system.', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO privilege (privilege_key, privilege_name, privilege_desc, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES ('MANAGE_USERS', 'Manage Users', 'Manage the users defined within the system.', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);
INSERT INTO privilege (privilege_key, privilege_name, privilege_desc, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES ('MANAGE_ROLES_PRIVS', 'Manage Roles/Privileges', 'Manage roles and privileges associated with those roles.', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

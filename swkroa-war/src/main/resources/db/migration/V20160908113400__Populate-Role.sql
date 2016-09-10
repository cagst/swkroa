INSERT INTO role (role_id, role_name, role_key, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (1, 'Administrator', 'ADMIN', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1);
INSERT INTO role (role_id, role_name, role_key, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (2, 'Staff', 'STAFF', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1);
INSERT INTO role (role_id, role_name, role_key, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (3, 'Member', 'MEMBER', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1);

INSERT INTO user_role (user_id, role_id, create_dt_tm, create_id, updt_dt_tm, updt_id)
SELECT user_id, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1
  FROM user;

INSERT INTO user_role (user_id, role_id, create_dt_tm, create_id, updt_dt_tm, updt_id)
SELECT user_id, 2, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, 1
  FROM user;

-- Insert Close Reasons
INSERT INTO codeset (codeset_id, codeset_display, codeset_meaning, create_id, create_dt_tm, updt_id, updt_dt_tm) VALUES (1, 'Close Reasons', 'CLOSE_REASONS', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

INSERT INTO codevalue (codeset_id, codevalue_display, codevalue_meaning, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (1, 'Deceased', 'CLOSE_DECEASED', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO codevalue (codeset_id, codevalue_display, codevalue_meaning, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (1, 'Delinquent', 'CLOSE_DELINQUENT', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
INSERT INTO codevalue (codeset_id, codevalue_display, codevalue_meaning, create_dt_tm, create_id, updt_dt_tm, updt_id) VALUES (1, 'Other', 'CLOSE_OTHER', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);

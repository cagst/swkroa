-- DROP triggers
DROP TRIGGER IF EXISTS codevalue_history;
DROP TRIGGER IF EXISTS codeset_history;
DROP TRIGGER IF EXISTS user_history;
DROP TRIGGER IF EXISTS person_history;
DROP TRIGGER IF EXISTS member_history;

-- DROP history tables
DROP TABLE IF EXISTS member_hist;
DROP TABLE IF EXISTS codevalue_hist;
DROP TABLE IF EXISTS codeset_hist;
DROP TABLE IF EXISTS user_hist;
DROP TABLE IF EXISTS person_hist;

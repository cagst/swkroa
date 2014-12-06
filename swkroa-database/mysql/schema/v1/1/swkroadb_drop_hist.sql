-- Drop History Triggers
DROP TRIGGER IF EXISTS county_history;
DROP TRIGGER IF EXISTS codeset_history;
DROP TRIGGER IF EXISTS codevalue_history;
DROP TRIGGER IF EXISTS person_history;
DROP TRIGGER IF EXISTS address_history;
DROP TRIGGER IF EXISTS phone_history;
DROP TRIGGER IF EXISTS email_history;
DROP TRIGGER IF EXISTS user_history;
DROP TRIGGER IF EXISTS membership_history;
DROP TRIGGER IF EXISTS membership_county_history;
DROP TRIGGER IF EXISTS member_type_history;
DROP TRIGGER IF EXISTS member_history;
DROP TRIGGER IF EXISTS comment_history;
DROP TRIGGER IF EXISTS transaction_history;
DROP TRIGGER IF EXISTS transaction_entry_history;
DROP TRIGGER IF EXISTS deposit_history;

-- Drop History Tables
DROP TABLE IF EXISTS deposit_hist;
DROP TABLE IF EXISTS transaction_entry_hist;
DROP TABLE IF EXISTS transaction_hist;
DROP TABLE IF EXISTS member_hist;
DROP TABLE IF EXISTS member_type_hist;
DROP TABLE IF EXISTS membership_county_hist;
DROP TABLE IF EXISTS membership_hist;
DROP TABLE IF EXISTS county_hist;
DROP TABLE IF EXISTS codevalue_hist;
DROP TABLE IF EXISTS codeset_hist;
DROP TABLE IF EXISTS user_hist;
DROP TABLE IF EXISTS address_hist;
DROP TABLE IF EXISTS phone_hist;
DROP TABLE IF EXISTS email_hist;
DROP TABLE IF EXISTS person_hist;
DROP TABLE IF EXISTS comment_hist;

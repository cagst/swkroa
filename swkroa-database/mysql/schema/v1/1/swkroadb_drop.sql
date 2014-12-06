ALTER TABLE membership
  DROP COLUMN inactive_reason_id,
  DROP COLUMN inactive_reason_txt;

ALTER TABLE member
  DROP COLUMN inactive_reason_id,
  DROP COLUMN inactive_reason_txt;

DROP TABLE IF EXISTS deposit_transaction;

ALTER TABLE membership
  DROP COLUMN close_reason_id,
  DROP COLUMN close_reason_txt;

ALTER TABLE member
  DROP COLUMN close_reason_id,
  DROP COLUMN close_reason_txt;

DROP TABLE IF EXISTS deposit_transaction;

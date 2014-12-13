ALTER TABLE membership
  DROP COLUMN close_reason_id,
  DROP COLUMN close_reason_txt,
  DROP COLUMN close_dt_tm;

ALTER TABLE member
  DROP COLUMN close_reason_id,
  DROP COLUMN close_reason_txt,
  DROP COLUMN close_dt_tm;

DROP TABLE IF EXISTS deposit_transaction;

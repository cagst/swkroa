ALTER TABLE membership
  ADD COLUMN incremental_dues          NUMERIC(10,2) NULL,
  CHANGE COLUMN dues_amount fixed_dues NUMERIC(10,2) NULL;

ALTER TABLE membership_hist
  ADD COLUMN incremental_dues          NUMERIC(10,2) NULL,
  CHANGE COLUMN dues_amount fixed_dues NUMERIC(10,2) NULL;

CREATE INDEX member_type_idx2 ON member_type (prev_member_type_id);
CREATE INDEX transaction_idx1 ON transaction (transaction_dt);

DROP TRIGGER membership_history;

delimiter |

CREATE TRIGGER membership_history AFTER UPDATE ON membership
  FOR EACH ROW BEGIN
    INSERT INTO membership_hist (membership_id, entity_type_cd, next_due_dt, fixed_dues, active_ind, close_reason_id, close_reason_txt, close_dt_tm, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt, incremental_dues)
    VALUES (old.membership_id, old.entity_type_cd, old.next_due_dt, old.fixed_dues, old.active_ind, old.close_reason_id, old.close_reason_txt, old.close_dt_tm, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt, old.incremental_dues);
  END;
|

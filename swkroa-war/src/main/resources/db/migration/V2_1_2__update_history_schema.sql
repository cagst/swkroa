DROP TRIGGER member_type_history;

delimiter |

CREATE TRIGGER member_type_history AFTER UPDATE ON member_type
  FOR EACH ROW BEGIN
    INSERT INTO member_type_hist (member_type_id, prev_member_type_id, member_type_display, member_type_meaning, dues_amount, primary_ind, allow_spouse_ind, allow_member_ind, beg_eff_dt_tm, end_eff_dt_tm, active_ind, create_id, create_dt_tm, updt_id, updt_dt_tm, updt_cnt)
    VALUES (old.member_type_id, old.prev_member_type_id, old.member_type_display, old.member_type_meaning, old.dues_amount, old.primary_ind, old.allow_spouse_ind, old.allow_member_ind, old.beg_eff_dt_tm, old.end_eff_dt_tm, old.active_ind, old.create_id, old.create_dt_tm, old.updt_id, old.updt_dt_tm, old.updt_cnt);
  END;
|

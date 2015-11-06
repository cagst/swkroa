DROP TRIGGER member_type_history;

ALTER TABLE member_type
        ADD beg_eff_dt DATE NOT NULL
       ,ADD end_eff_dt DATE NULL;

UPDATE member_type
   SET beg_eff_dt = CAST(beg_eff_dt_tm AS DATE)
 WHERE beg_eff_dt_tm IS NOT NULL;

UPDATE member_type
   SET end_eff_dt = CAST(end_eff_dt_tm AS DATE)
 WHERE end_eff_dt_tm IS NOT NULL;

ALTER TABLE member_type
       DROP beg_eff_dt_tm
      ,DROP end_eff_dt_tm;

SELECT member_type_id
      ,prev_member_type_id
      ,member_type_display
      ,member_type_meaning
      ,dues_amount
      ,primary_ind
      ,allow_spouse_ind
      ,allow_member_ind
      ,beg_eff_dt
      ,end_eff_dt
      ,active_ind
      ,updt_cnt AS member_type_updt_cnt
  FROM member_type
 WHERE beg_eff_dt <= :eff_dt
   AND (end_eff_dt IS NULL OR end_eff_dt >= :eff_dt)
   AND active_ind = 1

SELECT member_type_id
      ,prev_member_type_id
      ,member_type_display
      ,member_type_meaning
      ,dues_amount
      ,beg_eff_dt_tm
      ,end_eff_dt_tm
      ,active_ind
      ,updt_cnt AS member_type_updt_cnt
  FROM member_type
 WHERE member_type_meaning = :member_type_meaning
   AND beg_eff_dt_tm <= :eff_dt_tm
   AND (end_eff_dt_tm IS NULL OR end_eff_dt_tm >= :eff_dt_tm)
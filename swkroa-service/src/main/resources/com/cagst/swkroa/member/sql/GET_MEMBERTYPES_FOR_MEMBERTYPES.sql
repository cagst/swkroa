SELECT member_type_id
      ,prev_member_type_id
      ,member_type_display
      ,member_type_meaning
      ,dues_amount
      ,primary_ind
      ,allow_spouse_ind
      ,allow_member_ind
      ,beg_eff_dt_tm
      ,end_eff_dt_tm
      ,active_ind
      ,updt_cnt AS member_type_updt_cnt
  FROM member_type mt1
      ,member_type mt2
 WHERE mt1.member_type_id = :member_type_id
   AND mt1.active_ind = 1
   AND mt2.prev_member_type_id = mt1.prev_member_type_id
   AND mt2.active_ind = 1

SELECT mt2.member_type_id
      ,mt2.prev_member_type_id
      ,mt2.member_type_display
      ,mt2.member_type_meaning
      ,mt2.dues_amount
      ,mt2.primary_ind
      ,mt2.allow_spouse_ind
      ,mt2.allow_member_ind
      ,mt2.beg_eff_dt
      ,mt2.end_eff_dt
      ,mt2.active_ind
      ,mt2.updt_cnt AS member_type_updt_cnt
  FROM member_type mt1
      ,member_type mt2
 WHERE mt1.member_type_id = :member_type_id
   AND mt1.active_ind = 1
   AND mt2.prev_member_type_id = mt1.prev_member_type_id
   AND mt2.active_ind = 1

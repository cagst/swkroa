UPDATE member_type
   SET member_type_display = :member_type_display
      ,member_type_meaning = :member_type_meaning
      ,dues_amount         = :dues_amount
      ,primary_ind         = :primary_ind
      ,allow_spouse_ind    = :allow_spouse_ind
      ,allow_member_ind    = :allow_member_ind
      ,beg_eff_dt          = :beg_eff_dt
      ,end_eff_dt          = :end_eff_dt
      ,active_ind          = :active_ind
      ,updt_cnt            = updt_cnt + 1
      ,updt_id             = :updt_id
      ,updt_dt_tm          = CURRENT_TIMESTAMP
 WHERE member_type_id = :member_type_id
   AND updt_cnt       = :member_type_updt_cnt

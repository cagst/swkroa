UPDATE membership
   SET entity_type_cd     = :entity_type_cd
      ,next_due_dt        = :next_due_dt
      ,dues_amount        = :dues_amount
      ,active_ind         = :active_ind
      ,updt_id            = :updt_id
      ,updt_dt_tm         = CURRENT_TIMESTAMP
      ,updt_cnt           = updt_cnt + 1
 WHERE membership_id = :membership_id
   AND updt_cnt      = :membership_updt_cnt

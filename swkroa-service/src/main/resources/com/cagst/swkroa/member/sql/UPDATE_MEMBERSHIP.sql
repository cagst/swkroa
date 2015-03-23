UPDATE membership
   SET entity_type_cd     = :entity_type_cd
      ,next_due_dt        = :next_due_dt
      ,fixed_dues         = :fixed_dues
      ,incremental_dues   = :incremental_dues
      ,active_ind         = :active_ind
      ,close_reason_id    = :close_reason_id
      ,close_reason_txt   = :close_reason_txt
      ,close_dt_tm        = :close_dt_tm
      ,updt_id            = :updt_id
      ,updt_dt_tm         = CURRENT_TIMESTAMP
      ,updt_cnt           = updt_cnt + 1
 WHERE membership_id = :membership_id
   AND updt_cnt      = :membership_updt_cnt

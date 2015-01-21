UPDATE member
   SET active_ind = 0
      ,close_reason_id  = :close_reason_id
      ,close_reason_txt = :close_reason_txt
      ,close_dt_tm      = CURRENT_TIMESTAMP
      ,updt_id          = :updt_id
      ,updt_dt_tm       = CURRENT_TIMESTAMP
 WHERE member_id = :member_id

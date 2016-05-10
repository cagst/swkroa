UPDATE deposit
   SET deposit_dt     = :deposit_dt
      ,deposit_ref    = :deposit_ref
      ,deposit_amount = :deposit_amount
      ,active_ind     = :active_ind
      ,updt_dt_tm     = CURRENT_TIMESTAMP
      ,updt_cnt       = updt_cnt + 1
 WHERE deposit_id = :deposit_id
   AND updt_cnt   = :deposit_updt_cnt

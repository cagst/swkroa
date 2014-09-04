UPDATE user
   SET account_locked_dt_tm = CURRENT_TIMESTAMP
      ,updt_id              = :updt_id
 WHERE user_id = :user_id

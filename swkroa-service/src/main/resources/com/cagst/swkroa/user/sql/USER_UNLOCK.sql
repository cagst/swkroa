UPDATE user
   SET account_locked_dt_tm = NULL
      ,signin_attempts      = 0
      ,updt_id              = :updt_id
      ,updt_dt_tm           = CURRENT_TIMESTAMP
 WHERE user_id  = :user_id

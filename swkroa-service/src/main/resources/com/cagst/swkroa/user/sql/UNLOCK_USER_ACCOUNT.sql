UPDATE user
   SET account_locked_dt_tm = NULL
      ,signin_attempts      = 0
      ,updt_dt_tm           = CURRENT_TIMESTAMP
      ,updt_cnt             = updt_cnt + 1
      ,updt_id              = :updt_id
 WHERE user_id  = :user_id
   AND updt_cnt = :updt_cnt

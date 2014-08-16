UPDATE user
   SET last_signin_dt_tm = CURRENT_TIMESTAMP
      ,last_signin_ip    = :last_signin_ip
      ,signin_attempts   = 0
 WHERE user_id = :user_id

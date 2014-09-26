UPDATE user
   SET password          = :password
      ,temporary_pwd_ind = 1
      ,updt_id           = :updt_id
      ,updt_dt_tm        = CURRENT_TIMESTAMP
 WHERE user_id  = :user_id

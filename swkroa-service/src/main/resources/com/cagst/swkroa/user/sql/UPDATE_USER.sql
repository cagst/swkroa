UPDATE user
   SET username              = :username
      ,temporary_pwd_ind     = :temporary_pwd_ind
      ,account_locked_dt_tm  = :account_locked_dt_tm
      ,account_expired_dt_tm = :account_expired_dt_tm
      ,user_type             = :user_type
      ,active_ind            = :active_ind
      ,updt_id               = :updt_id
      ,updt_dt_tm            = CURRENT_TIMESTAMP
      ,updt_cnt              = updt_cnt + 1
 WHERE user_id  = :user_id
   AND updt_cnt = :user_updt_cnt

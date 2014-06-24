UPDATE user
   SET password               = :password
      ,password_changed_dt_tm = CURRENT_TIMESTAMP
      ,temporary_pwd_ind      = 0
 WHERE user_id  = :user_id

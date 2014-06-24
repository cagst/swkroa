UPDATE user
   SET account_locked_dt_tm = CURRENT_TIMESTAMP
 WHERE user_id = :user_id

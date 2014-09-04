UPDATE user
   SET active_ind = 1
      ,updt_dt_tm = CURRENT_TIMESTAMP
      ,updt_id    = :updt_id
 WHERE user_id  = :user_id

INSERT INTO user_role (user_id
                      ,role_id
                      ,active_ind
                      ,create_id
                      ,create_dt_tm
                      ,updt_id
                      ,updt_dt_tm)
             VALUES  (:user_id
                     ,:role_id
                     ,:active_ind
                     ,:create_id
                     ,CURRENT_TIMESTAMP
                     ,:updt_id
                     ,CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE
   active_ind = :active_ind
  ,updt_id    = :updt_id
  ,updt_dt_tm = CURRENT_TIMESTAMP
  ,updt_cnt   = updt_cnt + 1

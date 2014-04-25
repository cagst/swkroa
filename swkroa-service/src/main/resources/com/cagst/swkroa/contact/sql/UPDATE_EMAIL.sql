UPDATE email
   SET email_type_cd = :email_type_cd
      ,email_address = :email_address
      ,active_ind    = :active_ind
      ,updt_id       = :updt_id
      ,updt_dt_tm    = CURRENT_TIMESTAMP
      ,updt_cnt      = updt_cnt + 1
 WHERE email_id = :email_id
   AND updt_cnt = :updt_cnt

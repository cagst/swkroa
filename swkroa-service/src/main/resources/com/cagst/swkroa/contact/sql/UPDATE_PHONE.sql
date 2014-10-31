UPDATE phone  
   SET phone_type_cd   = :phone_type_cd
      ,phone_number    = :phone_number
      ,phone_extension = :phone_extension
      ,primary_ind     = :primary_ind
      ,active_ind      = :active_ind
      ,updt_id         = :updt_id
      ,updt_dt_tm      = CURRENT_TIMESTAMP
      ,updt_cnt        = updt_cnt + 1
 WHERE phone_id = :phone_id
   AND updt_cnt = :updt_cnt

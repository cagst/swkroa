UPDATE job_detail
   SET job_status    = :job_status
      ,active_ind    = :active_ind
      ,updt_id       = :updt_id
      ,updt_dt_tm    = CURRENT_TIMESTAMP
      ,updt_cnt      = updt_cnt + 1
 WHERE job_detail_id = :job_detail_id
   AND updt_cnt      = :job_detail_updt_cnt

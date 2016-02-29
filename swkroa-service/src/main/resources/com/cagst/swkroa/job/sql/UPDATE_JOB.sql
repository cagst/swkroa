UPDATE job
   SET job_name           = :job_name
      ,job_type           = :job_type
      ,job_status         = :job_status
      ,active_ind         = :active_ind
      ,updt_id            = :updt_id
      ,updt_dt_tm         = CURRENT_TIMESTAMP
      ,updt_cnt           = updt_cnt + 1
 WHERE job_id   = :job_id
   AND updt_cnt = :job_updt_cnt

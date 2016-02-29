SELECT j.job_id
      ,j.job_name
      ,j.job_type
      ,j.job_status
      ,j.active_ind
      ,j.updt_cnt AS job_updt_cnt
      ,j.create_id
  FROM job j
 WHERE j.job_id = :job_id

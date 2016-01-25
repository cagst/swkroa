SELECT job_id
      ,job_name
      ,job_type
      ,job_status
      ,job_detail
      ,active_ind
      ,updt_cnt AS job_updt_cnt
FROM job
WHERE job_type = :job_type

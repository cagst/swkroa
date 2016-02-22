SELECT j.job_id
      ,j.job_name
      ,j.job_type
      ,j.job_status
      ,j.parent_entity_id
      ,j.parent_entity_name
      ,j.active_ind
      ,j.updt_cnt AS job_updt_cnt
      ,j.create_id
FROM job j
WHERE j.job_type = :job_type

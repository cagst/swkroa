SELECT jd.job_detail_id
      ,jd.job_id
      ,jd.parent_entity_id
      ,jd.parent_entity_name
      ,jd.job_status
      ,jd.active_ind
      ,jd.updt_cnt AS job_detail_updt_cnt
      ,jd.create_id
FROM job_detail jd
WHERE jd.job_id = :job_id

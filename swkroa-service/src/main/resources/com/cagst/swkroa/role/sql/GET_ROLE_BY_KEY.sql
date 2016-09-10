SELECT r.role_id
      ,r.role_name
      ,r.role_key
      ,r.updt_cnt AS role_updt_cnt
      ,r.active_ind
  FROM role r
 WHERE r.role_key = :role_key
   AND r.active_ind = 1

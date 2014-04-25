SELECT r.role_id
      ,r.role_key
      ,r.role_name
      ,r.updt_cnt AS role_updt_cnt
      ,r.active_ind
  FROM role r
 WHERE r.active_ind = 1
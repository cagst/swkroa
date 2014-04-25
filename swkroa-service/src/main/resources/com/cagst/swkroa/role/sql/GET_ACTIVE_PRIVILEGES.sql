SELECT p.privilege_id
      ,p.privilege_key
      ,p.privilege_name
      ,p.updt_cnt AS privilege_updt_cnt
      ,p.active_ind
  FROM privilege p
 WHERE p.active_ind = 1
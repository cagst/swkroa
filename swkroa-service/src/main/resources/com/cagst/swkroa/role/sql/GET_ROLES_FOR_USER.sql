SELECT r.role_id
      ,r.role_key
      ,r.role_name
      ,r.updt_cnt AS role_updt_cnt
      ,r.active_ind
  FROM user_role ur
      ,role r
 WHERE ur.user_id    = :user_id
   AND ur.active_ind = 1
   AND r.role_id     = ur.role_id
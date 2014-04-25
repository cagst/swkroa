SELECT p.privilege_id
      ,p.privilege_key
      ,p.privilege_name
      ,p.updt_cnt AS privilege_updt_cnt
      ,p.active_ind
  FROM user_role ur
      ,role_privilege rp
      ,privilege p
 WHERE ur.user_id    = :user_id
   AND ur.active_ind = 1
   AND rp.role_id    = ur.role_id
   AND rp.active_ind = 1
   AND p.privilege_id = rp.privilege_id
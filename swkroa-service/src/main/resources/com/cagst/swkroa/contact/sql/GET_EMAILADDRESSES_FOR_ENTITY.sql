SELECT e.email_id
      ,e.parent_entity_id
      ,e.parent_entity_name
      ,e.email_type_cd
      ,e.email_address
      ,e.active_ind
      ,e.updt_cnt AS updt_cnt
  FROM email e
 WHERE e.parent_entity_id = :parent_entity_id
   AND e.parent_entity_name = :parent_entity_name
   AND e.active_ind = 1
SELECT p.phone_id
      ,p.parent_entity_id
      ,p.parent_entity_name
      ,p.phone_type_cd
      ,p.phone_number
      ,p.phone_extension
      ,p.primary_ind
      ,p.active_ind
      ,p.updt_cnt
  FROM phone p
 WHERE p.parent_entity_id = :parent_entity_id
   AND p.parent_entity_name = :parent_entity_name
   AND p.active_ind = 1
SELECT a.address_id
      ,a.parent_entity_id
      ,a.parent_entity_name
      ,a.address_type_cd
      ,a.address1
      ,a.address2
      ,a.address3
      ,a.city
      ,a.state_code
      ,a.postal_code
      ,a.country_code
      ,a.active_ind
      ,a.updt_cnt
  FROM address a
 WHERE a.parent_entity_id = :parent_entity_id
   AND a.parent_entity_name = :parent_entity_name
   AND a.active_ind = 1
SELECT d.document_id
      ,d.parent_entity_id
      ,d.parent_entity_name
      ,d.document_type
      ,d.document_name
      ,d.document_format
      ,d.document_location
      ,d.document_content
      ,d.document_desc
      ,d.beg_eff_dt
      ,d.end_eff_dt
      ,d.active_ind
      ,d.updt_cnt AS document_updt_cnt
  FROM document d
 WHERE d.beg_eff_dt <= CURRENT_DATE AND (d.end_eff_dt IS NULL OR d.end_eff_dt > CURRENT_DATE)
   AND ((d.parent_entity_id   = :parent_entity_id
   AND   d.parent_entity_name = :parent_entity_name)
    OR  (d.parent_entity_id IS NULL
   AND   d.parent_entity_name IS NULL))

SELECT d.document_id
      ,d.parent_entity_id
      ,d.parent_entity_name
      ,d.document_type
      ,d.document_format
      ,d.document_location
      ,d.document_content
      ,d.document_desc
      ,d.beg_eff_dt
      ,d.end_eff_dt
      ,d.active_ind
      ,d.updt_cnt AS document_updt_cnt
  FROM document d
 WHERE d.document_id = :document_id

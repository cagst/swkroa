UPDATE document
   SET document_type     = :document_type
      ,document_name     = :document_name
      ,document_format   = :document_format
      ,document_location = :document_location
      ,document_content  = :document_content
      ,document_desc     = :document_desc
      ,beg_eff_dt        = :beg_eff_dt
      ,end_eff_dt        = :end_eff_dt
      ,active_ind        = :active_ind
      ,updt_id           = :updt_id
      ,updt_dt_tm        = CURRENT_TIMESTAMP
      ,updt_cnt          = updt_cnt + 1
 WHERE document_id = :document_id
   AND updt_cnt    = :document_updt_cnt

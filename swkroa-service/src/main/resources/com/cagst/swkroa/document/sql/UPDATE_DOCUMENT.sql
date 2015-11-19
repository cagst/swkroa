UPDATE document
   SET document_desc     = :document_desc
      ,document_type_cd  = :document_type_cd
      ,beg_eff_dt        = :beg_eff_dt
      ,end_eff_dt        = :end_eff_dt
      ,active_ind        = :active_ind
      ,updt_id           = :updt_id
      ,updt_dt_tm        = CURRENT_TIMESTAMP
      ,updt_cnt          = updt_cnt + 1
 WHERE document_id = :document_id
   AND updt_cnt    = :document_updt_cnt

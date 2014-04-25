UPDATE codevalue
   SET codevalue_display = :codevalue_display
      ,codevalue_meaning = :codevalue_meaning
      ,active_ind        = :active_ind
      ,updt_id           = :updt_id
      ,updt_dt_tm        = CURRENT_TIMESTAMP
      ,updt_cnt          = updt_cnt + 1
 WHERE codevalue_id = :codevalue_id
   AND updt_cnt     = :codevalue_updt_cnt
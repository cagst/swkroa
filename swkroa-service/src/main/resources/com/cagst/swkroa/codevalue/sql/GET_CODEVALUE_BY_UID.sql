SELECT codevalue_id
      ,codeset_id
      ,codevalue_display
      ,codevalue_meaning
      ,active_ind
      ,updt_cnt AS codevalue_updt_cnt
  FROM codevalue
 WHERE codevalue_id = :codevalue_id

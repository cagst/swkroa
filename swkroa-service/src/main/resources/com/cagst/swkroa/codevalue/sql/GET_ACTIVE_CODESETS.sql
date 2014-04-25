SELECT codeset_id
      ,codeset_display
      ,codeset_meaning
      ,active_ind
      ,updt_cnt AS codeset_updt_cnt
  FROM codeset
 WHERE active_ind = 1

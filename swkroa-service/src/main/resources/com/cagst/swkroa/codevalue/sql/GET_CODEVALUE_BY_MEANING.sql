SELECT cv.codevalue_id
      ,cv.codeset_id
      ,cv.codevalue_display
      ,cv.codevalue_meaning
      ,cv.active_ind
      ,cv.updt_cnt AS codevalue_updt_cnt
  FROM codeset cs
      ,codevalue cv
 WHERE cs.codeset_meaning   = :codeset_meaning
   AND cv.codeset_id        = cs.codeset_id
   AND cv.codevalue_meaning = :meaning
   AND cv.active_ind        = 1

SELECT c.country_code
      ,c.country_name
      ,c.active_ind
      ,c.updt_cnt
  FROM country c
 WHERE c.active_ind = 1

SELECT s.country_code
      ,s.state_code
      ,s.state_fips
      ,s.state_name
      ,s.active_ind
      ,s.updt_cnt
  FROM state s
 WHERE s.country_code = :country_code
   AND s.active_ind = 1

SELECT c.county_id
      ,c.state_code
      ,c.county_code
      ,c.county_name
      ,c.swkroa_county_ind
      ,c.active_ind
      ,c.updt_cnt AS county_updt_cnt
  FROM county c
 WHERE c.county_id = :county_id
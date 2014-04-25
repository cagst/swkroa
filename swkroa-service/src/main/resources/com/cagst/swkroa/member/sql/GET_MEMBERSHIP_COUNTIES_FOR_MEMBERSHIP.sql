SELECT mc.membership_county_id
      ,mc.membership_id
      ,mc.county_id
      ,mc.net_mineral_acres
      ,mc.surface_acres
      ,mc.voting_ind
      ,mc.active_ind
      ,mc.updt_cnt AS membership_county_updt_cnt
  FROM membership_county mc
 WHERE mc.membership_id = :membership_id
   AND mc.active_ind = 1

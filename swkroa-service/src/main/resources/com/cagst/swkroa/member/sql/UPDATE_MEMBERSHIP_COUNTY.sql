UPDATE membership_county
   SET membership_id      = :membership_id
      ,county_id          = :county_id
      ,net_mineral_acres  = :net_mineral_acres
      ,surface_acres      = :surface_acres
      ,voting_ind         = :voting_ind
      ,active_ind         = :active_ind
      ,updt_id            = :updt_id
      ,updt_dt_tm         = CURRENT_TIMESTAMP
      ,updt_cnt           = updt_cnt + 1
 WHERE membership_county_id = :membership_county_id
   AND updt_cnt             = :membership_county_updt_cnt
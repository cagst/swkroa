INSERT INTO membership_county (membership_id
                              ,county_id
                              ,net_mineral_acres
                              ,surface_acres
                              ,voting_ind
                              ,active_ind
                              ,create_id
                              ,create_dt_tm
                              ,updt_id
                              ,updt_dt_tm)
                       VALUES (:membership_id
                              ,:county_id
                              ,:net_mineral_acres
                              ,:surface_acres
                              ,:voting_ind
                              ,:active_ind
                              ,:create_id
                              ,CURRENT_TIMESTAMP
                              ,:updt_id
                              ,CURRENT_TIMESTAMP)

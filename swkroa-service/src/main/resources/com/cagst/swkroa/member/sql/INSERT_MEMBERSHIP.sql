INSERT INTO membership (membership_type_cd
                       ,entity_type_cd
                       ,due_on_dt
                       ,dues_amount
                       ,active_ind
                       ,create_id
                       ,create_dt_tm
                       ,updt_id
                       ,updt_dt_tm)
                VALUES (:membership_type_cd
                       ,:entity_type_cd
                       ,:due_on_dt
                       ,:dues_amount
                       ,:active_ind
                       ,:create_id
                       ,CURRENT_TIMESTAMP
                       ,:updt_id
                       ,CURRENT_TIMESTAMP)

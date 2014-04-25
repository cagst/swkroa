INSERT INTO comment (parent_entity_id
                    ,parent_entity_name
                    ,comment_dt
                    ,comment_txt
                    ,active_ind
                    ,create_id
                    ,create_dt_tm
                    ,updt_id
                    ,updt_dt_tm)
            VALUES (:parent_entity_id
                   ,:parent_entity_name
                   ,:comment_dt
                   ,:comment_txt
                   ,:active_ind
                   ,:create_id
                   ,CURRENT_TIMESTAMP
                   ,:updt_id
                   ,CURRENT_TIMESTAMP)

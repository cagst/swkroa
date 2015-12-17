INSERT INTO membership (entity_type_cd
                       ,next_due_dt
                       ,incremental_dues
                       ,active_ind
                       ,close_reason_id
                       ,close_reason_txt
                       ,close_dt_tm
                       ,create_id
                       ,create_dt_tm
                       ,updt_id
                       ,updt_dt_tm)
                VALUES (:entity_type_cd
                       ,:next_due_dt
                       ,:incremental_dues
                       ,:active_ind
                       ,:close_reason_id
                       ,:close_reason_txt
                       ,:close_dt_tm
                       ,:create_id
                       ,CURRENT_TIMESTAMP
                       ,:updt_id
                       ,CURRENT_TIMESTAMP)

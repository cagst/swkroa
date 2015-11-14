INSERT INTO document (parent_entity_id
                     ,parent_entity_name
                     ,document_type
                     ,document_format
                     ,document_location
                     ,document_content
                     ,document_desc
                     ,beg_eff_dt
                     ,end_eff_dt
                     ,active_ind
                     ,create_id
                     ,create_dt_tm
                     ,updt_id
                     ,updt_dt_tm)
             VALUES (:parent_entity_id
                    ,:parent_entity_name
                    ,:document_type
                    ,:document_format
                    ,:document_location
                    ,:document_content
                    ,:document_desc
                    ,:beg_eff_dt
                    ,:end_eff_dt
                    ,:active_ind
                    ,:create_id
                    ,CURRENT_TIMESTAMP
                    ,:updt_id
                    ,CURRENT_TIMESTAMP)

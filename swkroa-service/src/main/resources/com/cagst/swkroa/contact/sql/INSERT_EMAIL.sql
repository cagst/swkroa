INSERT INTO email (parent_entity_id
                  ,parent_entity_name
                  ,email_type_cd
                  ,email_address
                  ,active_ind
                  ,create_id
                  ,create_dt_tm
                  ,updt_id
                  ,updt_dt_tm)
          VALUES (:parent_entity_id
                 ,:parent_entity_name
                 ,:email_type_cd
                 ,:email_address
                 ,:active_ind
                 ,:create_id
                 ,CURRENT_TIMESTAMP
                 ,:updt_id
                 ,CURRENT_TIMESTAMP)
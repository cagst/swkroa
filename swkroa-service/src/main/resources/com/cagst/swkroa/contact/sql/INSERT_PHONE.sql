INSERT INTO phone (parent_entity_id
                  ,parent_entity_name
                  ,phone_type_cd
                  ,phone_number
                  ,phone_extension
                  ,primary_ind
                  ,active_ind
                  ,create_id
                  ,create_dt_tm
                  ,updt_id
                  ,updt_dt_tm)
          VALUES (:parent_entity_id
                 ,:parent_entity_name
                 ,:phone_type_cd
                 ,:phone_number
                 ,:phone_extension
                 ,:primary_ind
                 ,:active_ind
                 ,:create_id
                 ,CURRENT_TIMESTAMP
                 ,:updt_id
                 ,CURRENT_TIMESTAMP)

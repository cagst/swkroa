INSERT INTO address (parent_entity_id
                    ,parent_entity_name
                    ,address_type_cd
                    ,address1
                    ,address2
                    ,address3
                    ,city
                    ,state_code
                    ,postal_code
                    ,country_code
                    ,create_id
                    ,create_dt_tm
                    ,updt_id
                    ,updt_dt_tm)
            VALUES (:parent_entity_id
                   ,:parent_entity_name
                   ,:address_type_cd
                   ,:address1
                   ,:address2
                   ,:address3
                   ,:city
                   ,:state_code
                   ,:postal_code
                   ,:country_code
                   ,:create_id
                   ,CURRENT_TIMESTAMP
                   ,:updt_id
                   ,CURRENT_TIMESTAMP)

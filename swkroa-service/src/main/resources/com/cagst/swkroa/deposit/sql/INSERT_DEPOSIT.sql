INSERT INTO deposit (deposit_dt
                    ,deposit_ref
                    ,deposit_amount
                    ,active_ind
                    ,create_id
                    ,create_dt_tm
                    ,updt_id
                    ,updt_dt_tm)
            VALUES (:deposit_dt
                  ,:deposit_ref
                  ,:deposit_amount
                  ,:active_ind
                  ,:create_id
                  ,CURRENT_TIMESTAMP
                  ,:updt_id
                  ,CURRENT_TIMESTAMP)

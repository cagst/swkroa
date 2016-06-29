INSERT INTO deposit_transaction (deposit_id
                                ,transaction_id
                                ,active_ind
                                ,create_id
                                ,create_dt_tm
                                ,updt_id
                                ,updt_dt_tm)
                        VALUES (:deposit_id
                               ,:transaction_id
                               ,:active_ind
                               ,:create_id
                               ,CURRENT_TIMESTAMP
                               ,:updt_id
                               ,CURRENT_TIMESTAMP)

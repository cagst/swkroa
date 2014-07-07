INSERT INTO transaction_entry (transaction_id
                              ,member_id
                              ,transaction_entry_type_cd
                              ,transaction_entry_amount
                              ,transaction_entry_desc
                              ,related_transaction_id
                              ,active_ind
                              ,create_id
                              ,create_dt_tm
                              ,updt_id
                              ,updt_dt_tm)
                      VALUES (:transaction_id
                             ,:member_id
                             ,:transaction_entry_type_cd
                             ,:transaction_entry_amount
                             ,:transaction_entry_desc
                             ,:related_transaction_id
                             ,:active_ind
                             ,:create_id
                             ,CURRENT_TIMESTAMP
                             ,:updt_id
                             ,CURRENT_TIMESTAMP)
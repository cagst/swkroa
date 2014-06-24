INSERT INTO transaction (membership_id
                        ,transaction_dt
                        ,transaction_type_cd
                        ,transaction_amount
                        ,transaction_desc
                        ,remaining_amount
                        ,ref_num
                        ,memo_txt
                        ,active_ind
                        ,create_id
                        ,create_dt_tm
                        ,updt_id
                        ,updt_dt_tm)
                VALUES (:membership_id
                       ,:transaction_dt
                       ,:transaction_type_cd
                       ,:transaction_amount
                       ,:transaction_desc
                       ,:remaining_amount
                       ,:ref_num
                       ,:memo_txt
                       ,:active_ind
                       ,:create_id
                       ,CURRENT_TIMESTAMP
                       ,:updt_id
                       ,CURRENT_TIMESTAMP)
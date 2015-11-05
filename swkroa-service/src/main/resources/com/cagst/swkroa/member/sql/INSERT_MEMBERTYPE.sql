INSERT INTO member_type (prev_member_type_id
                        ,member_type_display
                        ,member_type_meaning
                        ,dues_amount
                        ,primary_ind
                        ,allow_spouse_ind
                        ,allow_member_ind
                        ,beg_eff_dt
                        ,end_eff_dt
                        ,active_ind
                        ,create_id
                        ,create_dt_tm
                        ,updt_id
                        ,updt_dt_tm)
                 VALUES (:prev_member_type_id
                        ,:member_type_display
                        ,:member_type_meaning
                        ,:dues_amount
                        ,:primary_ind
                        ,:allow_spouse_ind
                        ,:allow_member_ind
                        ,:beg_eff_dt
                        ,:end_eff_dt
                        ,:active_ind
                        ,:create_id
                        ,CURRENT_TIMESTAMP
                        ,:updt_id
                        ,CURRENT_TIMESTAMP)

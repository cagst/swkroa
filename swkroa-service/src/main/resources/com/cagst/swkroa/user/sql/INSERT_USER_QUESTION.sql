INSERT INTO user_question (user_id
                          ,question_cd
                          ,answer
                          ,active_ind
                          ,create_id
                          ,create_dt_tm
                          ,updt_id
                          ,updt_dt_tm)
                   VALUES (:user_id
                          ,:question_cd
                          ,:answer
                          ,:active_ind
                          ,:create_id
                          ,CURRENT_TIMESTAMP
                          ,:updt_id
                          ,CURRENT_TIMESTAMP)

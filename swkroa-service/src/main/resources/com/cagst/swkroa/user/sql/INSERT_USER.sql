INSERT INTO user (person_id
                 ,username
                 ,password
                 ,password_changed_dt_tm
                 ,temporary_pwd_ind
                 ,account_locked_dt_tm
                 ,account_expired_dt_tm
                 ,user_type
                 ,active_ind
                 ,create_id
                 ,create_dt_tm
                 ,updt_id
                 ,updt_dt_tm)
         VALUES (:person_id
                ,:username
                ,:password
                ,:password_changed_dt_tm
                ,:temporary_pwd_ind
                ,:account_locked_dt_tm
                ,:account_expired_dt_tm
                ,:user_type
                ,:active_ind
                ,:create_id
                ,CURRENT_TIMESTAMP
                ,:updt_id
                ,CURRENT_TIMESTAMP)

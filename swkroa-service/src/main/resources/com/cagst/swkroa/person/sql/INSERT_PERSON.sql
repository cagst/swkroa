INSERT INTO person (title_cd
                   ,name_last
                   ,name_last_key
                   ,name_first
                   ,name_first_key
                   ,name_middle
                   ,locale_language
                   ,locale_country
                   ,time_zone
                   ,active_ind
                   ,create_id
                   ,create_dt_tm
                   ,updt_id
                   ,updt_dt_tm)
            VALUES (:title_cd
                   ,:name_last
                   ,:name_last_key
                   ,:name_first
                   ,:name_first_key
                   ,:name_middle
                   ,:locale_language
                   ,:locale_country
                   ,:time_zone
                   ,:active_ind
                   ,:create_id
                   ,CURRENT_TIMESTAMP
                   ,:updt_id
                   ,CURRENT_TIMESTAMP)

INSERT INTO member (person_id
                   ,membership_id
                   ,company_name
                   ,company_name_key
                   ,owner_ident
                   ,member_type_id
                   ,greeting
                   ,in_care_of
                   ,join_dt
                   ,mail_newsletter_ind
                   ,email_newsletter_ind
                   ,active_ind
                   ,create_id
                   ,create_dt_tm
                   ,updt_id
                   ,updt_dt_tm)
            VALUES (:person_id
                   ,:membership_id
                   ,:company_name
                   ,:company_name_key
                   ,:owner_ident
                   ,:member_type_id
                   ,:greeting
                   ,:in_care_of
                   ,:join_dt
                   ,:mail_newsletter_ind
                   ,:email_newsletter_ind
                   ,:active_ind
                   ,:create_id
                   ,CURRENT_TIMESTAMP
                   ,:updt_id
                   ,CURRENT_TIMESTAMP)

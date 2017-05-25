UPDATE member
   SET membership_id        = :membership_id
      ,person_id            = :person_id
      ,company_name         = :company_name
      ,company_name_key     = :company_name_key
      ,owner_ident          = :owner_ident
      ,member_type_id       = :member_type_id
      ,greeting             = :greeting
      ,in_care_of           = :in_care_of
      ,join_dt              = :join_dt
      ,mail_newsletter_ind  = :mail_newsletter_ind
      ,email_newsletter_ind = :email_newsletter_ind
      ,active_ind           = :active_ind
      ,updt_id              = :updt_id
      ,updt_dt_tm           = CURRENT_TIMESTAMP
      ,updt_cnt             = updt_cnt + 1
 WHERE member_id = :member_id
   AND updt_cnt  = :member_updt_cnt
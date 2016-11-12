SELECT m.person_id
      ,m.member_id
      ,m.membership_id
      ,m.member_name
      ,m.company_name
      ,m.owner_ident
      ,m.member_type_id
      ,m.greeting
      ,m.in_care_of
      ,m.join_dt
      ,m.mail_newsletter_ind
      ,m.email_newsletter_ind
      ,m.close_reason_id
      ,m.close_reason_txt
      ,m.close_dt_tm
      ,m.member_updt_cnt
      ,m.active_ind
  FROM member_summary m
 WHERE m.person_id  = :person_id
   AND m.active_ind = 1

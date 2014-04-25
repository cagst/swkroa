SELECT m.person_id
      ,m.member_id
      ,m.company_name
      ,m.owner_ident
      ,m.member_type_id
      ,m.greeting
      ,m.in_care_of
      ,m.join_dt
      ,m.mail_newsletter_ind
      ,m.email_newsletter_ind
      ,m.updt_cnt AS member_updt_cnt
      ,m.active_ind
  FROM member m
 WHERE m.membership_id = :membership_id
   AND m.active_ind = 1

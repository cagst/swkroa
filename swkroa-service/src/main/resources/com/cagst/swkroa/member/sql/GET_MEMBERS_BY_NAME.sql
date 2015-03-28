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
      ,m.close_reason_id
      ,m.close_reason_txt
      ,m.close_dt_tm
      ,m.updt_cnt AS member_updt_cnt
      ,m.active_ind
  FROM member_summary m
      ,membership_summary ms
 WHERE m.member_id IN (SELECT m.member_id
                         FROM member m
                        WHERE m.company_name_key LIKE :name
                        UNION
                       SELECT m.member_id
                         FROM member m
                        WHERE m.owner_ident LIKE :name
                        UNION
                       SELECT m.member_id
                         FROM person p
                             ,member m
                        WHERE p.name_last_key LIKE :name
                          AND m.person_id  = p.person_id
                        UNION
                       SELECT m.member_id
                         FROM person p
                             ,member m
                        WHERE p.name_first_key LIKE :name
                          AND m.person_id  = p.person_id)
   AND ms.membership_id = m.membership_id
   AND ms.active_ind = m.active_ind
   AND (:status = 'ALL' OR
       (:status = 'ACTIVE' AND m.active_ind = 1) OR
       (:status = 'INACTIVE' AND m.active_ind = 0))
   AND (:balance = 'ALL' OR
       (:balance = 'DELINQUENT' AND ms.balance < 0.0) OR
       (:balance = 'PAID' AND ms.balance = 0.0) OR
       (:balance = 'CREDIT' AND ms.balance > 0.0))

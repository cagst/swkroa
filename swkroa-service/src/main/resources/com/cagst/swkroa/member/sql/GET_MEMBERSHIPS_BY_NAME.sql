SELECT membership_id
      ,membership_name
      ,entity_type_cd
      ,next_due_dt
      ,member_id
      ,join_dt
      ,member_type_id
      ,active_ind
      ,calculated_dues
      ,incremental_dues
      ,balance
      ,last_payment_dt
      ,close_reason_id
      ,close_reason_txt
      ,close_dt_tm
      ,membership_updt_cnt
  FROM membership_summary ms
 WHERE ms.membership_id IN (SELECT m.membership_id
                              FROM member m
                             WHERE m.company_name_key LIKE :name
                             UNION
                            SELECT m.membership_id
                              FROM member m
                             WHERE m.owner_ident LIKE :name
                             UNION
                            SELECT m.membership_id
                              FROM person p
                                  ,member m
                             WHERE p.name_last_key LIKE :name
                               AND m.person_id  = p.person_id
                             UNION
                            SELECT m.membership_id
                              FROM person p
                                  ,member m
                             WHERE p.name_first_key LIKE :name
                               AND m.person_id  = p.person_id)
   AND (:status = 'ALL' OR
       (:status = 'ACTIVE' AND ms.active_ind = 1) OR
       (:status = 'INACTIVE' AND ms.active_ind = 0))
   AND (:balance = 'ALL' OR
       (:balance = 'DELINQUENT' AND ms.balance < 0.0) OR
       (:balance = 'PAID' AND ms.balance = 0.0) OR
       (:balance = 'CREDIT' AND ms.balance > 0.0))

SELECT ms.membership_id
      ,ms.entity_type_cd
      ,ms.next_due_dt
      ,ms.member_id
      ,ms.company_name
      ,ms.owner_ident
      ,ms.join_dt
      ,ms.member_type_id
      ,ms.active_ind
      ,ms.name_last
      ,ms.name_middle
      ,ms.name_first
      ,ms.fixed_dues
      ,ms.calculated_dues
      ,ms.balance
      ,ms.last_payment_dt
      ,ms.close_reason_id
      ,ms.close_reason_txt
      ,ms.close_dt_tm
      ,ms.membership_updt_cnt
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

SELECT m.membership_id
      ,m.membership_type_cd
      ,m.entity_type_cd
      ,m.due_on_dt
      ,m.dues_amount
      ,SUM(t.transaction_amount) AS amount_due
      ,m.updt_cnt AS membership_updt_cnt
      ,m.active_ind
  FROM membership m LEFT OUTER JOIN transaction t ON
                    (t.membership_id = m.membership_id AND t.active_ind = 1)
 WHERE m.membership_id IN (SELECT m.membership_id
                             FROM member m
                            WHERE m.company_name_key LIKE :name
                              AND m.active_ind = 1
                            UNION
                           SELECT m.membership_id
                             FROM member m
                            WHERE m.owner_ident LIKE :name
                              AND m.active_ind = 1
                            UNION
                           SELECT m.membership_id
                             FROM person p
                                 ,member m
                            WHERE p.name_last_key LIKE :name
                              AND p.active_ind = 1
                              AND m.person_id  = p.person_id
                              AND m.active_ind = 1
                            UNION
                           SELECT m.membership_id
                             FROM person p
                                 ,member m
                            WHERE p.name_first_key LIKE :name
                              AND p.active_ind = 1
                              AND m.person_id  = p.person_id
                              AND m.active_ind = 1)
   AND m.active_ind = 1
GROUP BY m.membership_id
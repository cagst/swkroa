SELECT COUNT(distinct m.member_id)
  FROM member_summary m
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
                          AND m.person_id = p.person_id
                        UNION
                       SELECT m.member_id
                         FROM person p
                             ,member m
                        WHERE p.name_first_key LIKE :name
                          AND m.person_id = p.person_id)
   AND (:status = 'ALL' OR
       (:status = 'ACTIVE' AND m.active_ind = 1) OR
       (:status = 'INACTIVE' AND m.active_ind = 0))

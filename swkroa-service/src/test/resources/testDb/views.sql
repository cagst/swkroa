DROP VIEW IF EXISTS membership_summary;

CREATE VIEW membership_summary AS
     SELECT ms.membership_id
           ,COALESCE(m.company_name, CONCAT_WS(', ', p.name_last, p.name_first)) AS membership_name
           ,ms.entity_type_cd
           ,m.member_id
           ,mt.member_type_id
           ,mt.member_type_meaning
           ,m.company_name
           ,ms.next_due_dt
           ,m.owner_ident
           ,m.greeting
           ,m.in_care_of
           ,p.title_cd
           ,cv.codevalue_meaning AS title_meaning
           ,cv.codevalue_display AS title_display
           ,p.name_last
           ,p.name_middle
           ,p.name_first
           ,NULLIF(CONCAT_WS(' ', p.name_first, p.name_last), '') AS name_full
           ,m.join_dt
           ,ms.close_reason_id
           ,ms.close_reason_txt
           ,ms.close_dt_tm
           ,ms.active_ind
           ,ms.updt_cnt AS membership_updt_cnt
           ,ms.fixed_dues
           ,ms.incremental_dues
           ,(COALESCE(SUM(mt2.dues_amount), 0) + COALESCE(ms.incremental_dues, 0)) AS calculated_dues
           ,COALESCE(ms.fixed_dues, (COALESCE(SUM(mt2.dues_amount), 0) + COALESCE(ms.incremental_dues, 0))) as effective_dues
           ,(SELECT SUM(te.transaction_entry_amount) AS balance
               FROM transaction t
                   ,transaction_entry te
              WHERE t.membership_id = ms.membership_id
                AND t.active_ind = 1
                AND te.transaction_id = t.transaction_id
                AND te.transaction_entry_type_cd NOT IN (SELECT cv.codevalue_id FROM codevalue cv WHERE cv.codeset_id = 8 AND cv.codevalue_meaning = 'TRANS_SPECIAL_FUNDS')
                AND te.active_ind = 1) AS balance
           ,(SELECT MAX(t.transaction_dt) AS last_payment_dt
               FROM transaction t
              WHERE t.membership_id = ms.membership_id
                AND t.transaction_type_flag = 1
                AND t.active_ind = 1) AS last_payment_dt
       FROM membership ms
 INNER JOIN member m        ON (m.membership_id = ms.membership_id AND m.active_ind = 1)
 INNER JOIN member_type mt  ON (mt.prev_member_type_id = m.member_type_id
                            AND mt.primary_ind = 1 AND mt.active_ind = 1
                            AND mt.beg_eff_dt_tm < NOW() AND (mt.end_eff_dt_tm IS NULL OR mt.end_eff_dt_tm > NOW()))
 INNER JOIN member m2       ON (m2.membership_id = ms.membership_id AND m2.active_ind = 1)
 INNER JOIN member_type mt2 ON (mt2.prev_member_type_id = m2.member_type_id AND mt2.active_ind = 1
                            AND mt2.beg_eff_dt_tm < NOW() AND (mt2.end_eff_dt_tm IS NULL OR mt2.end_eff_dt_tm > NOW()))
 LEFT OUTER JOIN person p   ON (p.person_id = m.person_id AND p.active_ind = 1)
 LEFT OUTER JOIN codevalue cv ON (cv.codevalue_id = p.title_cd)
   GROUP BY ms.membership_id
           ,membership_name
           ,ms.entity_type_cd
           ,m.member_id
           ,mt.member_type_id
           ,mt.member_type_meaning
           ,m.company_name
           ,ms.next_due_dt
           ,m.owner_ident
           ,m.greeting
           ,m.in_care_of
           ,p.title_cd
           ,title_meaning
           ,title_display
           ,p.name_last
           ,p.name_middle
           ,p.name_first
           ,name_full
           ,m.join_dt
           ,ms.close_reason_id
           ,ms.close_reason_txt
           ,ms.close_dt_tm
           ,ms.active_ind
           ,membership_updt_cnt
           ,fixed_dues
           ,incremental_dues;

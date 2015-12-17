ALTER TABLE membership
       DROP COLUMN fixed_dues;

CREATE OR REPLACE VIEW _base_membership_summary AS
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
              ,ms.incremental_dues
              ,(COALESCE(SUM(mt2.dues_amount), 0) + COALESCE(ms.incremental_dues, 0)) AS calculated_dues
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
                                             AND mt.beg_eff_dt < NOW() AND (mt.end_eff_dt IS NULL OR mt.end_eff_dt > NOW()))
              INNER JOIN member m2       ON (m2.membership_id = ms.membership_id AND m2.active_ind = 1)
              INNER JOIN member_type mt2 ON (mt2.prev_member_type_id = m2.member_type_id AND mt2.active_ind = 1
                                             AND mt2.beg_eff_dt < NOW() AND (mt2.end_eff_dt IS NULL OR mt2.end_eff_dt > NOW()))
              LEFT OUTER JOIN person p   ON (p.person_id = m.person_id AND p.active_ind = 1)
              LEFT OUTER JOIN codevalue cv ON (cv.codevalue_id = p.title_cd)
       GROUP BY membership_id
              ,membership_name
              ,entity_type_cd
              ,member_id
              ,member_type_id
              ,company_name
              ,owner_ident
              ,greeting
              ,in_care_of
              ,title_cd
              ,name_last
              ,name_middle
              ,name_first
              ,next_due_dt
              ,join_dt
              ,close_reason_id
              ,close_reason_txt
              ,close_dt_tm
              ,active_ind
              ,incremental_dues
              ,membership_updt_cnt;

CREATE OR REPLACE VIEW membership_summary AS
       SELECT ms.membership_id
              ,ms.membership_name
              ,ms.entity_type_cd
              ,ms.member_id
              ,ms.member_type_id
              ,ms.member_type_meaning
              ,ms.company_name
              ,ms.next_due_dt
              ,ms.owner_ident
              ,ms.greeting
              ,ms.in_care_of
              ,ms.title_cd
              ,ms.title_meaning
              ,ms.title_display
              ,ms.name_last
              ,ms.name_middle
              ,ms.name_first
              ,ms.name_full
              ,ms.join_dt
              ,ms.close_reason_id
              ,ms.close_reason_txt
              ,ms.close_dt_tm
              ,ms.active_ind
              ,ms.membership_updt_cnt
              ,COALESCE(ms.incremental_dues, 0) AS incremental_dues
              ,ms.calculated_dues
              ,ms.balance
              ,ms.last_payment_dt
       FROM _base_membership_summary ms
       GROUP BY membership_id
              ,membership_name
              ,entity_type_cd
              ,member_id
              ,member_type_id
              ,company_name
              ,owner_ident
              ,greeting
              ,in_care_of
              ,title_cd
              ,name_last
              ,name_middle
              ,name_first
              ,next_due_dt
              ,join_dt
              ,close_reason_id
              ,close_reason_txt
              ,close_dt_tm
              ,active_ind
              ,incremental_dues
              ,membership_updt_cnt;

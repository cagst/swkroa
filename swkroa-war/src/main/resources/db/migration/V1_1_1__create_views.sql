CREATE OR REPLACE VIEW membership_summary AS
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
           ,ms.dues_amount AS fixed_dues
           ,ms.updt_cnt AS membership_updt_cnt
           ,COALESCE(SUM(mt2.dues_amount), 0) AS calculated_dues
           ,COALESCE(ms.dues_amount, COALESCE(SUM(mt2.dues_amount), 0)) AS effective_dues
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
           ,fixed_dues
           ,membership_updt_cnt;

CREATE OR REPLACE VIEW member_summary AS
     SELECT m.membership_id
           ,m.member_id
           ,NULLIF(COALESCE(m.company_name, CONCAT_WS(', ', p.name_last, p.name_first)), '') AS member_name
           ,mt.member_type_id
           ,mt.member_type_meaning
           ,mt.member_type_display
           ,m.company_name
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
           ,m.mail_newsletter_ind
           ,m.email_newsletter_ind
           ,m.close_reason_id
           ,m.close_reason_txt
           ,m.close_dt_tm
           ,m.active_ind
           ,mt.dues_amount
           ,m.updt_cnt AS member_updt_cnt
       FROM member m
 INNER JOIN member_type mt  ON (mt.prev_member_type_id = m.member_type_id AND mt.active_ind = 1
                            AND mt.beg_eff_dt_tm < NOW() AND (mt.end_eff_dt_tm IS NULL OR mt.end_eff_dt_tm > NOW()))
 LEFT OUTER JOIN person p   ON (p.person_id = m.person_id AND p.active_ind = 1)
 LEFT OUTER JOIN codevalue cv ON (cv.codevalue_id = p.title_cd);

CREATE OR REPLACE VIEW _base_primary_address AS
     SELECT a.parent_entity_id
           ,a.parent_entity_name
           ,MIN(a.address_id) AS primary_address_id
           ,null AS secondary_address_id
       FROM address a
      WHERE a.active_ind = 1 AND a.primary_ind = 1
   GROUP BY a.parent_entity_id, a.parent_entity_name
      UNION
     SELECT a.parent_entity_id
           ,a.parent_entity_name
           ,null AS primary_address_id
           ,MIN(a.address_id) AS secondary_address_id
       FROM address a
      WHERE a.active_ind = 1 AND a.primary_ind = 0
   GROUP BY a.parent_entity_id, a.parent_entity_name;

CREATE OR REPLACE VIEW _primary_address AS
     SELECT parent_entity_id
           ,parent_entity_name
           ,COALESCE(primary_address_id, secondary_address_id) AS address_id
       FROM _base_primary_address
   GROUP BY parent_entity_id, parent_entity_name;

CREATE OR REPLACE VIEW primary_address AS
     SELECT ba.parent_entity_id
           ,ba.parent_entity_name
           ,a.address_type_cd
           ,a.address1
           ,a.address2
           ,a.address3
           ,a.city
           ,a.state_code
           ,a.country_code
           ,a.postal_code
           ,IF(LENGTH(a.postal_code) > 5, CONCAT(LEFT(a.postal_code, 5), "-", MID(a.postal_code, 6)), a.postal_code) AS postal_code_formatted
       FROM _primary_address ba INNER JOIN address a USING (address_id);

CREATE OR REPLACE VIEW _base_primary_email AS
     SELECT e.parent_entity_id
           ,e.parent_entity_name
           ,MIN(e.email_id) AS primary_email_id
           ,null AS secondary_email_id
       FROM email e
      WHERE e.active_ind = 1 AND e.primary_ind = 1
   GROUP BY e.parent_entity_id, e.parent_entity_name
      UNION
     SELECT e.parent_entity_id
           ,e.parent_entity_name
           ,null AS primary_email_id
           ,MIN(e.email_id) AS secondary_email_id
       FROM email e
      WHERE e.active_ind = 1 AND e.primary_ind = 0
   GROUP BY e.parent_entity_id, e.parent_entity_name;

CREATE OR REPLACE VIEW _primary_email AS
     SELECT parent_entity_id
           ,parent_entity_name
           ,COALESCE(primary_email_id, secondary_email_id) AS email_id
       FROM _base_primary_email
   GROUP BY parent_entity_id, parent_entity_name;

CREATE OR REPLACE VIEW primary_email AS
     SELECT be.parent_entity_id
           ,be.parent_entity_name
           ,e.email_type_cd
           ,e.email_address
       FROM _primary_email be INNER JOIN email e USING (email_id);

CREATE OR REPLACE VIEW _base_primary_phone AS
     SELECT p.parent_entity_id
           ,p.parent_entity_name
           ,MIN(p.phone_id) AS primary_phone_id
           ,null AS secondary_phone_id
       FROM phone p
      WHERE p.active_ind = 1 AND p.primary_ind = 1
   GROUP BY p.parent_entity_id, p.parent_entity_name
      UNION
     SELECT p.parent_entity_id
           ,p.parent_entity_name
           ,null AS primary_phone_id
           ,MIN(p.phone_id) AS secondary_phone_id
       FROM phone p
      WHERE p.active_ind = 1 AND p.primary_ind = 0
   GROUP BY p.parent_entity_id, p.parent_entity_name;

CREATE OR REPLACE VIEW _primary_phone AS
     SELECT parent_entity_id
           ,parent_entity_name
           ,COALESCE(primary_phone_id, secondary_phone_id) AS phone_id
       FROM _base_primary_phone
   GROUP BY parent_entity_id, parent_entity_name;

CREATE OR REPLACE VIEW primary_phone AS
     SELECT bp.parent_entity_id
           ,bp.parent_entity_name
           ,p.phone_type_cd
           ,p.phone_number
           ,p.phone_extension
       FROM _primary_phone bp INNER JOIN phone p USING (phone_id);

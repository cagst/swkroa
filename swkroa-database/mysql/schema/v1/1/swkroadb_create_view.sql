CREATE VIEW membership_summary AS
     SELECT ms.membership_id
           ,ms.entity_type_cd
           ,m.member_id
           ,mt.member_type_id
           ,m.company_name
           ,m.owner_ident
           ,m.greeting
           ,m.in_care_of
           ,p.title_cd
           ,p.name_last
           ,p.name_middle
           ,p.name_first
           ,ms.next_due_dt
           ,m.join_dt
           ,ms.close_reason_id
           ,ms.close_reason_txt
           ,ms.close_dt_tm
           ,ms.active_ind
           ,ms.dues_amount AS fixed_dues
           ,ms.updt_cnt AS membership_updt_cnt
           ,COALESCE(SUM(mt2.dues_amount), 0) AS calculated_dues
           ,(SELECT SUM(te.transaction_entry_amount) AS balance
               FROM transaction t
                   ,transaction_entry te
              WHERE t.membership_id = ms.membership_id
                AND t.active_ind = 1
                AND te.transaction_id = t.transaction_id
                AND te.active_ind = 1) AS balance
           ,(SELECT MAX(t.transaction_dt) AS last_payment_dt
               FROM transaction t
              WHERE t.membership_id = ms.membership_id
                AND t.transaction_type_flag = 1
                AND t.active_ind = 1) AS last_payment_dt
       FROM membership ms
 INNER JOIN member m        ON (m.membership_id = ms.membership_id AND m.active_ind = ms.active_ind)
 INNER JOIN member_type mt  ON (mt.prev_member_type_id = m.member_type_id
                            AND mt.primary_ind = 1 AND mt.active_ind = 1
                            AND mt.beg_eff_dt_tm < NOW() AND (mt.end_eff_dt_tm IS NULL OR mt.end_eff_dt_tm > NOW()))
 INNER JOIN member m2       ON (m2.membership_id = ms.membership_id AND m2.active_ind = ms.active_ind)
 INNER JOIN member_type mt2 ON (mt2.prev_member_type_id = m2.member_type_id AND mt2.active_ind = 1
                            AND mt2.beg_eff_dt_tm < NOW() AND (mt2.end_eff_dt_tm IS NULL OR mt2.end_eff_dt_tm > NOW()))
 LEFT OUTER JOIN person p   ON (p.person_id = m.person_id AND p.active_ind = 1)
   GROUP BY membership_id
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

CREATE VIEW base_primary_addresses AS
     SELECT a.parent_entity_id
           ,a.parent_entity_name
           ,MIN(a.address_id) AS address_id
       FROM address a
      WHERE a.active_ind = 1 AND a.primary_ind = 1
   GROUP BY a.parent_entity_id, a.parent_entity_name
      UNION
     SELECT a.parent_entity_id
           ,a.parent_entity_name
           ,MIN(a.address_id) AS address_id
       FROM address a
      WHERE a.active_ind = 1 AND a.primary_ind = 0
   GROUP BY a.parent_entity_id, a.parent_entity_name;

CREATE VIEW primary_addresses AS
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
       FROM base_primary_addresses ba INNER JOIN address a USING (address_id);

CREATE VIEW base_primary_email AS
     SELECT e.parent_entity_id
           ,e.parent_entity_name
           ,MIN(e.email_id) AS email_id
       FROM email e
      WHERE e.active_ind = 1 AND e.primary_ind = 1
   GROUP BY e.parent_entity_id, e.parent_entity_name
      UNION
     SELECT e.parent_entity_id
           ,e.parent_entity_name
           ,MIN(e.email_id) AS email_id
       FROM email e
      WHERE e.active_ind = 1 AND e.primary_ind = 0
   GROUP BY e.parent_entity_id, e.parent_entity_name;

CREATE VIEW primary_email AS
     SELECT be.parent_entity_id
           ,be.parent_entity_name
           ,e.email_type_cd
           ,e.email_address
       FROM base_primary_email be INNER JOIN email e USING (email_id);

CREATE VIEW base_primary_phone AS
     SELECT p.parent_entity_id
           ,p.parent_entity_name
           ,MIN(p.phone_id) AS phone_id
       FROM phone p
      WHERE p.active_ind = 1 AND p.primary_ind = 1
   GROUP BY p.parent_entity_id, p.parent_entity_name
      UNION
     SELECT p.parent_entity_id
           ,p.parent_entity_name
           ,MIN(p.phone_id) AS phone_id
       FROM phone p
      WHERE p.active_ind = 1 AND p.primary_ind = 0
   GROUP BY p.parent_entity_id, p.parent_entity_name;

CREATE VIEW primary_phone AS
     SELECT bp.parent_entity_id
           ,bp.parent_entity_name
           ,p.phone_type_cd
           ,p.phone_number
           ,p.phone_extension
       FROM base_primary_phone bp INNER JOIN phone p USING (phone_id);

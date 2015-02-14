CREATE VIEW member_summary AS
     SELECT m.membership_id
           ,m.member_id
           ,COALESCE(m.company_name, CONCAT_WS(', ', p.name_last, p.name_first)) AS member_name
           ,mt.member_type_id
           ,mt.member_type_meaning
           ,mt.member_type_display
           ,m.company_name
           ,m.owner_ident
           ,m.greeting
           ,m.in_care_of
           ,p.title_cd
           ,p.name_last
           ,p.name_middle
           ,p.name_first
           ,m.join_dt
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

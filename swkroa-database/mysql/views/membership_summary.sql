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

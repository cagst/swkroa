SELECT membership_id
      ,entity_type_cd
      ,next_due_dt
      ,member_id
      ,company_name
      ,owner_ident
      ,join_dt
      ,member_type_id
      ,active_ind
      ,name_last
      ,name_middle
      ,name_first
      ,fixed_dues
      ,calculated_dues
      ,balance
      ,close_reason_id
      ,close_reason_txt
      ,membership_updt_cnt
  FROM membership_summary
 WHERE membership_id = :membership_id

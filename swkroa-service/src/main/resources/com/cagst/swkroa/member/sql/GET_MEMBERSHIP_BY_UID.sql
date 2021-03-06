SELECT membership_id
      ,membership_name
      ,entity_type_cd
      ,next_due_dt
      ,member_id
      ,join_dt
      ,member_type_id
      ,active_ind
      ,calculated_dues
      ,incremental_dues
      ,balance
      ,last_payment_dt
      ,close_reason_id
      ,close_reason_txt
      ,close_dt_tm
      ,membership_updt_cnt
  FROM membership_summary
 WHERE membership_id = :membership_id

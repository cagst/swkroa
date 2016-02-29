SELECT membership_id
      ,membership_name
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
      ,calculated_dues
      ,incremental_dues
      ,balance
      ,last_payment_dt
      ,close_reason_id
      ,close_reason_txt
      ,close_dt_tm
      ,membership_updt_cnt
  FROM membership_summary ms
 WHERE ms.active_ind = 1
   AND ms.calculated_dues > 0
   AND ms.next_due_dt <= :nextDueDate
 ORDER BY ms.next_due_dt ASC
         ,ms.membership_name

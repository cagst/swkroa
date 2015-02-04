SELECT ms.membership_id
      ,ms.entity_type_cd
      ,ms.next_due_dt
      ,ms.member_id
      ,ms.company_name
      ,ms.owner_ident
      ,ms.join_dt
      ,ms.member_type_id
      ,ms.active_ind
      ,ms.name_last
      ,ms.name_middle
      ,ms.name_first
      ,ms.fixed_dues
      ,ms.calculated_dues
      ,ms.balance
      ,ms.last_payment_dt
      ,ms.close_reason_id
      ,ms.close_reason_txt
      ,ms.close_dt_tm
      ,ms.membership_updt_cnt
  FROM membership_summary ms
 WHERE ms.active_ind = 1
   AND ms.next_due_dt <= :nextDueDate

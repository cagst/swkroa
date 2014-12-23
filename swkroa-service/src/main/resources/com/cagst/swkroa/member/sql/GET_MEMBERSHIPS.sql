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
      ,last_payment_dt
      ,close_reason_id
      ,close_reason_txt
      ,close_dt_tm
      ,membership_updt_cnt
  FROM membership_summary
 WHERE (:status = 'all' OR
       (:status = 'active' AND active_ind = 1) OR
       (:status = 'inactive' AND active_ind = 2))
   AND (:balance = 'all' OR
       (:balance = 'delinquent' AND balance < 0.0) OR
       (:balance = 'paid' AND balance = 0.0) OR
       (:balance = 'credit' AND balance > 0.0))
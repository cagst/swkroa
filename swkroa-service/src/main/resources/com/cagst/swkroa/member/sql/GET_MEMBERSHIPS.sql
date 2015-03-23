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
      ,incremental_dues
      ,balance
      ,last_payment_dt
      ,close_reason_id
      ,close_reason_txt
      ,close_dt_tm
      ,membership_updt_cnt
  FROM membership_summary
 WHERE (:status = 'ALL' OR
       (:status = 'ACTIVE' AND active_ind = 1) OR
       (:status = 'INACTIVE' AND active_ind = 0))
   AND (:balance = 'ALL' OR
       (:balance = 'DELINQUENT' AND balance < 0.0) OR
       (:balance = 'PAID' AND balance = 0.0) OR
       (:balance = 'CREDIT' AND balance > 0.0))

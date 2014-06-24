SELECT t.transaction_id
      ,t.membership_id
      ,t.transaction_dt
      ,t.transaction_type_flag
      ,t.transaction_amount
      ,t.transaction_desc
      ,t.remaining_amount
      ,t.ref_num
      ,t.memo_txt
      ,t.active_ind
      ,t.updt_cnt AS transaction_updt_cnt
  FROM transaction t
 WHERE t.membership_id = :membership_id
   AND t.active_ind = 1
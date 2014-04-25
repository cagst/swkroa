SELECT t.transaction_id
      ,t.membership_id
      ,t.member_id
      ,t.transaction_dt
      ,t.transaction_type_cd
      ,t.transaction_amount
      ,t.ref_num
      ,t.memo_txt
      ,t.active_ind
      ,t.updt_cnt AS transaction_updt_cnt
  FROM transaction t
 WHERE t.transaction_id = :transaction_id
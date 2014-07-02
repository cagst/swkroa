SELECT t.transaction_id
      ,t.membership_id
      ,t.transaction_dt
      ,t.transaction_type_flag
      ,t.transaction_amount
      ,t.transaction_desc
      ,t.ref_num
      ,t.memo_txt
      ,t.active_ind
      ,t.updt_cnt AS transaction_updt_cnt
      ,te.transaction_entry_id
      ,te.related_transaction_id
      ,te.member_id
      ,te.transaction_entry_amount
      ,te.transaction_entry_type_cd
      ,te.transaction_entry_desc
      ,te.updt_cnt AS transaction_entry_updt_cnt
  FROM transaction t
      ,transaction_entry te
 WHERE t.transaction_id  = :transaction_id
   AND te.transaction_id = t.transaction_id
   AND te.active_ind     = 1
ORDER BY te.transaction_id


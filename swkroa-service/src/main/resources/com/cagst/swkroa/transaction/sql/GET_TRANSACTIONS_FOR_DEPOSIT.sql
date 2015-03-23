SELECT t.transaction_id
      ,t.membership_id
      ,t.transaction_dt
      ,t.transaction_type_flag
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
      ,te.updt_cnt AS transaction_entry_updt_cnt
  FROM deposit_transaction dt
      ,transaction t
      ,transaction_entry te
 WHERE dt.deposit_id     = :deposit_id
   AND dt.active_ind     = 1
   AND t.transaction_id  = dt.transaction_id
   AND te.transaction_id = t.transaction_id
   AND te.active_ind     = 1
ORDER BY t.transaction_id

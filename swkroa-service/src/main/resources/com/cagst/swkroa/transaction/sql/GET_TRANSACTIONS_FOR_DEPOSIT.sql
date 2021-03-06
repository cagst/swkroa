SELECT dt.deposit_transaction_id
      ,dt.deposit_id
      ,dt.active_ind AS deposit_transaction_active_ind
      ,ms.membership_name
      ,ms.membership_id
      ,t.transaction_id
      ,t.transaction_dt
      ,t.transaction_type_flag
      ,t.transaction_desc
      ,t.ref_num
      ,t.memo_txt
      ,t.active_ind
      ,t.updt_cnt AS transaction_updt_cnt
      ,te.transaction_entry_id
      ,te.related_transaction_id
      ,te.transaction_entry_amount
      ,te.transaction_entry_type_cd
      ,te.updt_cnt AS transaction_entry_updt_cnt
  FROM deposit_transaction dt
      ,transaction t
      ,transaction_entry te
      ,membership_summary ms
 WHERE dt.deposit_id     = :deposit_id
   AND dt.active_ind     = 1
   AND t.transaction_id  = dt.transaction_id
   AND te.transaction_id = t.transaction_id
   AND te.active_ind     = 1
   AND ms.membership_id  = t.membership_id
ORDER BY t.transaction_id

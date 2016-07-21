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
      ,te.transaction_entry_amount
      ,te.transaction_entry_type_cd
      ,te.updt_cnt AS transaction_entry_updt_cnt
      ,(SELECT count(*)
          FROM deposit_transaction dt
         WHERE dt.transaction_id = t.transaction_id
           AND dt.active_ind = 1) AS deposit_ind
      ,(SELECT max(d.document_id)
          FROM document d
         WHERE d.parent_entity_name = 'TRANSACTION'
           AND d.parent_entity_id = t.transaction_id
           AND d.active_ind = 1) AS document_id
  FROM transaction t
      ,transaction_entry te
 WHERE t.transaction_id  = :transaction_id
   AND te.transaction_id = t.transaction_id
   AND te.active_ind     = 1
ORDER BY te.transaction_id


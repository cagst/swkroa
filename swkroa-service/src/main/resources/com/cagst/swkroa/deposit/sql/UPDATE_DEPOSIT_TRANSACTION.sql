UPDATE deposit_transaction
   SET active_ind = :active_ind
      ,updt_id    = :updt_id
      ,updt_dt_tm = CURRENT_TIMESTAMP
      ,updt_cnt   = updt_cnt + 1
 WHERE deposit_transaction_id = :deposit_transaction_id

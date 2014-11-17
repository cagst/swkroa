UPDATE transaction_entry
   SET transaction_id            = :transaction_id
      ,member_id                 = :member_id
      ,transaction_entry_type_cd = :transaction_entry_type_cd
      ,transaction_entry_amount  = :transaction_entry_amount
      ,related_transaction_id    = :related_transaction_id
      ,active_ind                = :active_ind
      ,updt_id                   = :updt_id
      ,updt_dt_tm                = CURRENT_TIMESTAMP
      ,updt_cnt                  = updt_cnt + 1
 WHERE transaction_entry_id = :transaction_entry_id
   AND updt_cnt             = :transaction_entry_updt_cnt
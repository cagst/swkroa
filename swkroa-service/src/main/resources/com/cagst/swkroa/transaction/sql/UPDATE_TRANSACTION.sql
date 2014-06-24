UPDATE transaction
   SET transaction_dt      = :transaction_dt
      ,transaction_type_cd = :transaction_type_cd
      ,transaction_amount  = :transaction_amount
      ,transaction_desc    = :transaction_desc
      ,remaining_amount    = :remaining_amount
      ,ref_num             = :ref_num
      ,memo_txt            = :memo_txt
      ,active_ind          = :active_ind
      ,updt_id             = :updt_id
      ,updt_dt_tm          = CURRENT_TIMESTAMP
      ,updt_cnt            = updt_cnt + 1
 WHERE transaction_id = :transaction_id
   AND updt_cnt       = :transaction_updt_cnt
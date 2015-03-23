SELECT d.deposit_id
      ,d.deposit_dt
      ,d.deposit_ref
      ,d.deposit_amount
      ,d.active_ind
      ,d.updt_cnt AS deposit_updt_cnt
  FROM deposit d
 WHERE d.active_ind = 1

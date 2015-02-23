SELECT t.transaction_dt
      ,t.transaction_desc
      ,COUNT(DISTINCT t.transaction_id) AS transaction_count
      ,SUM(te.transaction_entry_amount) AS transaction_total
  FROM transaction t
 INNER JOIN transaction_entry te ON (te.transaction_id = t.transaction_id AND te.active_ind = 1)
 WHERE t.transaction_type_flag = 0
   AND t.active_ind = 1
GROUP BY t.transaction_dt, t.transaction_desc

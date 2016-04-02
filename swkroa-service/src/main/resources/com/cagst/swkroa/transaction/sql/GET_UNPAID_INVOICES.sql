SELECT ms.membership_name
      ,t.membership_id
      ,t.transaction_id
      ,t.transaction_dt
      ,t.transaction_desc
      ,t.ref_num
      ,t.active_ind
      ,t.updt_cnt AS transaction_updt_cnt
      ,te.transaction_entry_id
      ,te.related_transaction_id
      ,te.member_id
      ,te.transaction_entry_amount
      ,te.transaction_entry_type_cd
      ,te.updt_cnt AS transaction_entry_updt_cnt
      ,iq.transaction_amount
      ,IFNULL(iq.paid_amount, 0) AS amount_paid
FROM (SELECT t.transaction_id
            ,(SELECT SUM(te.transaction_entry_amount)
                FROM transaction_entry te
               WHERE te.transaction_id = t.transaction_id
                 AND te.active_ind = 1) AS transaction_amount
            ,(SELECT SUM(te.transaction_entry_amount)
                FROM transaction_entry te
               WHERE te.related_transaction_id = t.transaction_id
                 AND te.active_ind = 1) AS paid_amount
        FROM transaction t
       WHERE t.membership_id IN (SELECT ms.membership_id
                                   FROM membership_summary ms
                                  WHERE ms.balance < 0
                                    AND ms.active_ind = 1)
         AND t.transaction_type_flag = 0
         AND t.active_ind = 1) iq
     ,transaction t
     ,transaction_entry te
     ,membership_summary ms
WHERE ABS(iq.transaction_amount) != ABS(IFNULL(iq.paid_amount, 0))
  AND t.transaction_id  = iq.transaction_id
  AND te.transaction_id = t.transaction_id
  AND te.active_ind     = 1
  AND ms.membership_id  = t.membership_id

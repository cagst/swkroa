SELECT iq.membership_id
      ,ms.membership_name
      ,iq.transaction_id
      ,iq.transaction_dt
      ,iq.transaction_desc
      ,iq.ref_num
      ,iq.transaction_amount
      ,IFNULL(iq.paid_amount, 0) AS amount_paid
FROM (SELECT t.transaction_id
            ,t.membership_id
            ,t.transaction_dt
            ,t.transaction_desc
            ,t.ref_num
            ,(SELECT SUM(te.transaction_entry_amount)
                FROM transaction_entry te
               WHERE te.transaction_id = t.transaction_id
                 AND te.active_ind = 1) AS transaction_amount
            ,(SELECT SUM(te.transaction_entry_amount)
                FROM transaction_entry te
               WHERE te.related_transaction_id = t.transaction_id
                 AND te.active_ind = 1) AS paid_amount
        FROM transaction t
       WHERE t.membership_id = :membership_id
         AND t.transaction_type_flag = 0
         AND t.active_ind = 1) iq
     ,membership_summary ms
WHERE ABS(iq.transaction_amount) != ABS(IFNULL(iq.paid_amount, 0))
  AND ms.membership_id = iq.membership_id

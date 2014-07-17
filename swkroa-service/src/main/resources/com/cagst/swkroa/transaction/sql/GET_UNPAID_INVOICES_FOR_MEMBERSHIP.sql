SELECT membership_id
      ,transaction_id
      ,transaction_dt
      ,transaction_type_flag
      ,transaction_desc
      ,ref_num
      ,memo_txt
      ,active_ind
      ,transaction_amount
      ,amount_paid
      ,amount_remaining
  FROM (
       -- Transactions with no payments
       SELECT t.membership_id
             ,t.transaction_id
             ,t.transaction_dt
             ,t.transaction_type_flag
             ,t.transaction_desc
             ,t.ref_num
             ,t.memo_txt
             ,t.active_ind
             ,sum(te.transaction_entry_amount) AS transaction_amount
             ,0.0 AS amount_paid
             ,sum(te.transaction_entry_amount) AS amount_remaining
         FROM transaction t
             ,transaction_entry te
        WHERE t.membership_id = :membership_id
          AND t.transaction_type_flag = 0
          AND t.active_ind = 1
          AND te.transaction_id = t.transaction_id
          AND te.active_ind = 1
          AND NOT EXISTS (SELECT *
                            FROM transaction_entry te
                           WHERE te.related_transaction_id = t.transaction_id
                             AND te.active_ind = 1)
        UNION
       -- Transactions with partial payments
       SELECT t.membership_id
             ,t.transaction_id
             ,t.transaction_dt
             ,t.transaction_type_flag
             ,t.transaction_desc
             ,t.ref_num
             ,t.memo_txt
             ,t.active_ind
             ,sum(te.transaction_entry_amount) AS transaction_amount
             ,iq.amount_paid
             ,sum(te.transaction_entry_amount) + iq.amount_paid AS amount_remaining
         FROM (SELECT t1.transaction_id
                     ,sum(te1.transaction_entry_amount) AS amount_paid
                 FROM transaction t1
                     ,transaction_entry te1
                WHERE t1.membership_id = :membership_id
                  AND t1.transaction_type_flag = 0
                  AND t1.active_ind = 1
                  AND te1.related_transaction_id = t1.transaction_id
                  AND te1.active_ind = 1
                GROUP BY t1.transaction_id
             ) iq
             ,transaction t
             ,transaction_entry te
        WHERE t.transaction_id = iq.transaction_id
          AND te.transaction_id = t.transaction_id
          AND te.active_ind = 1
       ) q
 WHERE q.amount_remaining IS NOT NULL
   AND q.amount_remaining != 0.0
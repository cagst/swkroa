SELECT m.membership_id
      ,m.entity_type_cd
      ,m.next_due_dt
      ,m.dues_amount
--      ,SUM(t.transaction_amount) AS amount_due
      ,0 AS amount_due
      ,m.updt_cnt AS membership_updt_cnt
      ,m.active_ind
  FROM membership m LEFT OUTER JOIN transaction t ON
                    (t.membership_id = m.membership_id AND t.active_ind = 1)
 WHERE m.membership_id = :membership_id
GROUP BY m.membership_id
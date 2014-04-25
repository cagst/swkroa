SELECT m.membership_id
      ,m.membership_type_cd
      ,m.entity_type_cd
      ,m.due_on_dt
      ,m.dues_amount
      ,SUM(t.transaction_amount) AS amount_due
      ,m.updt_cnt AS membership_updt_cnt
      ,m.active_ind
  FROM membership m LEFT OUTER JOIN transaction t ON
                    (t.membership_id = m.membership_id AND t.active_ind = 1)
 WHERE m.active_ind = 1
GROUP BY m.membership_id
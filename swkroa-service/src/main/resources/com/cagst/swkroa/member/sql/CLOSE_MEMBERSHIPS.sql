UPDATE membership
   SET active_ind = 0
      ,close_reason_id  = :close_reason_id
      ,close_reason_txt = :close_reason_txt
      ,close_dt_tm      = CURRENT_TIMESTAMP
 WHERE membership_id IN (:memberships)

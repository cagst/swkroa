UPDATE membership
   SET active_ind = 1
      ,close_reason_id  = :close_reason_id
      ,close_reason_txt = :close_reason_txt
 WHERE membership_id IN (:memberships)

UPDATE membership
   SET next_due_dt = DATE_ADD(next_due_dt, INTERVAL 1 YEAR)
      ,updt_id     = :updt_id
      ,updt_dt_tm  = CURRENT_TIMESTAMP
 WHERE membership_id = :membershipId

-- If the fixed_dues = the calculated_dues then we can clear out the
-- fixed_dues (set to NULL) since we are calculating the dues correctly.
UPDATE membership m
  JOIN membership_summary ms USING (membership_id, membership_id)
   SET m.fixed_dues = null
      ,m.update_reason = 'FIXED=CALCULATED'
 WHERE ms.fixed_dues = ms.calculated_dues
   AND m.update_reason IS NULL;

-- If the fixed_dues > the calculated_dues
-- then we can set the incremental_dues and clear out the fixed_due
-- We will assume the difference is due to incremental dues that hasn't been set
UPDATE membership m
  JOIN membership_summary ms USING (membership_id, membership_id)
   SET m.incremental_dues = (ms.fixed_dues - ms.calculated_dues)
      ,m.fixed_dues = NULL
      ,m.update_reason = 'FIXED>CALCULATED'
 WHERE ms.fixed_dues > ms.calculated_dues
   AND m.update_reason IS NULL;

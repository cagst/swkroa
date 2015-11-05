-- If the fixed_dues = the calculated_dues then we can clear out the
-- fixed_dues (set to NULL) since we care calculating the dues correctly.
UPDATE membership m
  JOIN membership_summary ms USING (membership_id, membership_id)
   SET m.fixed_dues = null
      ,m.update_reason = 'FIXED=CALCULATED'
 WHERE ms.fixed_dues = ms.calculated_dues
   AND m.update_reason IS NULL;

-- If the fixed_dues > the calculated_dues (and the incremental_dues hasn't been set)
-- then we can set the incremental_dues and clear out the fixed_due
-- We will assume the difference is due to incremental dues that hasn't been set
UPDATE membership m
  JOIN membership_summary ms USING (membership_id, membership_id)
   SET m.incremental_dues = (ms.fixed_dues - ms.calculated_dues)
      ,m.fixed_dues = NULL
      ,m.update_reason = 'FIXED>CALCULATED_WITH-NO-INCREMENTAL'
 WHERE ms.fixed_dues > ms.calculated_dues
   AND ms.incremental_dues IS NULL
   AND m.update_reason IS NULL;

-- If the fixed_dues > the calculated_dues (and the incremental_dues has been set)
-- then we can clear out the fixed_due since we have already updated the incremental_dues
UPDATE membership m
  JOIN membership_summary ms USING (membership_id, membership_id)
   SET m.fixed_dues = NULL
      ,m.update_reason = 'FIXED>CALCULATED_WITH-INCREMENTAL'
 WHERE ms.fixed_dues > ms.calculated_dues
   AND ms.incremental_dues IS NOT NULL
   AND (ms.incremental_dues + ms.calculated_dues) = ms.fixed_dues
   AND m.update_reason IS NULL;

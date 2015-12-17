CREATE OR REPLACE VIEW _base_primary_email AS
  SELECT e.parent_entity_id
    ,e.parent_entity_name
    ,MIN(e.email_id) AS primary_email_id
    ,null AS secondary_email_id
  FROM email e
  WHERE e.active_ind = 1 AND e.primary_ind = 1
  GROUP BY e.parent_entity_id, e.parent_entity_name
  UNION
  SELECT e.parent_entity_id
    ,e.parent_entity_name
    ,null AS primary_email_id
    ,MIN(e.email_id) AS secondary_email_id
  FROM email e
  WHERE e.active_ind = 1 AND e.primary_ind = 0
  GROUP BY e.parent_entity_id, e.parent_entity_name;

CREATE OR REPLACE VIEW _primary_email AS
  SELECT parent_entity_id
    ,parent_entity_name
    ,COALESCE(primary_email_id, secondary_email_id) AS email_id
  FROM _base_primary_email
  GROUP BY parent_entity_id, parent_entity_name;

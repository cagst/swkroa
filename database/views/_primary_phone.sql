CREATE OR REPLACE VIEW _primary_phone AS
  SELECT parent_entity_id
    ,parent_entity_name
    ,COALESCE(primary_phone_id, secondary_phone_id) AS phone_id
  FROM _base_primary_phone
  GROUP BY parent_entity_id, parent_entity_name;

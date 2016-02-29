CREATE OR REPLACE VIEW _primary_address AS
  SELECT parent_entity_id
    ,parent_entity_name
    ,COALESCE(primary_address_id, secondary_address_id) AS address_id
  FROM _base_primary_address
  GROUP BY parent_entity_id, parent_entity_name;

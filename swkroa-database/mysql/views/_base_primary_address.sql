CREATE OR REPLACE VIEW _base_primary_address AS
  SELECT a.parent_entity_id
    ,a.parent_entity_name
    ,MIN(a.address_id) AS primary_address_id
    ,null AS secondary_address_id
  FROM address a
  WHERE a.active_ind = 1 AND a.primary_ind = 1
  GROUP BY a.parent_entity_id, a.parent_entity_name
  UNION
  SELECT a.parent_entity_id
    ,a.parent_entity_name
    ,null AS primary_address_id
    ,MIN(a.address_id) AS secondary_address_id
  FROM address a
  WHERE a.active_ind = 1 AND a.primary_ind = 0
  GROUP BY a.parent_entity_id, a.parent_entity_name;

CREATE OR REPLACE VIEW _base_primary_phone AS
  SELECT p.parent_entity_id
    ,p.parent_entity_name
    ,MIN(p.phone_id) AS primary_phone_id
    ,null AS secondary_phone_id
  FROM phone p
  WHERE p.active_ind = 1 AND p.primary_ind = 1
  GROUP BY p.parent_entity_id, p.parent_entity_name
  UNION
  SELECT p.parent_entity_id
    ,p.parent_entity_name
    ,null AS primary_phone_id
    ,MIN(p.phone_id) AS secondary_phone_id
  FROM phone p
  WHERE p.active_ind = 1 AND p.primary_ind = 0
  GROUP BY p.parent_entity_id, p.parent_entity_name;

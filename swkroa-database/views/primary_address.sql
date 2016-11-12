CREATE OR REPLACE VIEW primary_address AS
  SELECT ba.parent_entity_id
    ,ba.parent_entity_name
    ,a.address_type_cd
    ,a.address1
    ,a.address2
    ,a.address3
    ,a.city
    ,a.state_code
    ,a.country_code
    ,a.postal_code
    ,IF(LENGTH(a.postal_code) > 5, CONCAT(LEFT(a.postal_code, 5), "-", MID(a.postal_code, 6)), a.postal_code) AS postal_code_formatted
  FROM _primary_address ba INNER JOIN address a USING (address_id);

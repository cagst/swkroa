CREATE OR REPLACE VIEW primary_phone AS
  SELECT bp.parent_entity_id
    ,bp.parent_entity_name
    ,p.phone_type_cd
    ,p.phone_number
    ,p.phone_extension
  FROM _primary_phone bp INNER JOIN phone p USING (phone_id);

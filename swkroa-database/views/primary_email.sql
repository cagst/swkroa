CREATE OR REPLACE VIEW primary_email AS
  SELECT be.parent_entity_id
    ,be.parent_entity_name
    ,e.email_type_cd
    ,e.email_address
  FROM _primary_email be INNER JOIN email e USING (email_id);

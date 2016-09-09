UPDATE document d
  INNER JOIN transaction t ON (t.membership_id = d.parent_entity_id
                               AND d.parent_entity_name = 'MEMBERSHIP'
                               AND t.transaction_desc = d.document_desc
                               AND t.transaction_type_flag = 0)
SET d.parent_entity_name = 'TRANSACTION'
  ,d.parent_entity_id   = t.transaction_id;

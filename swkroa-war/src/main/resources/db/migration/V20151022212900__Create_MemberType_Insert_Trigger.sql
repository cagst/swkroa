DROP TRIGGER IF EXISTS member_type_insert;

delimiter |

CREATE TRIGGER member_type_insert BEFORE INSERT ON member_type
  FOR EACH ROW BEGIN
    DECLARE next_id BIGINT;
    SET next_id = (SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='member_type');
    SET NEW.member_type_id = next_id;

    IF NEW.prev_member_type_id IS NULL OR NEW.prev_member_type_id = 0 THEN
      SET NEW.prev_member_type_id = next_id;
    END IF;
  END;
|

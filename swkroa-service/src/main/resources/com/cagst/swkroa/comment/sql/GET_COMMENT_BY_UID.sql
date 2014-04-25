SELECT c.comment_id
      ,c.parent_entity_id
      ,c.parent_entity_name
      ,c.comment_dt
      ,c.comment_txt
      ,c.active_ind
      ,c.updt_cnt AS comment_updt_cnt
  FROM comment c
 WHERE c.comment_id = :comment_id

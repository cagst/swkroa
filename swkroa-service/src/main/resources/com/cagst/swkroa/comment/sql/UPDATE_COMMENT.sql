UPDATE comment
   SET comment_txt        = :comment_txt
      ,comment_dt         = :comment_dt
      ,active_ind         = :active_ind
      ,updt_id            = :updt_id
      ,updt_dt_tm         = CURRENT_TIMESTAMP
      ,updt_cnt           = updt_cnt + 1
 WHERE comment_id = :comment_id
   AND updt_cnt   = :comment_updt_cnt

UPDATE user_question
   SET question_cd = :question_cd
      ,answer      = :answer
      ,active_ind  = :active_ind
      ,updt_id     = :updt_id
      ,updt_dt_tm  = CURRENT_TIMESTAMP
      ,updt_cnt    = updt_cnt + 1
 WHERE user_question_id = :user_question_id
   AND updt_cnt         = :updt_cnt

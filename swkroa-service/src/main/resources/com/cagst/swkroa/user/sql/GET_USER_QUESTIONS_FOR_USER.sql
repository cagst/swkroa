SELECT user_question_id
      ,question_cd
      ,answer
      ,active_ind
      ,updt_cnt
  FROM user_question
 WHERE user_id    = :user_id
   AND active_ind = 1

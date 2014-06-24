SELECT count(*)
  FROM user u
 WHERE u.username = :username
   AND u.active_ind = 1
   AND u.user_id != :user_id

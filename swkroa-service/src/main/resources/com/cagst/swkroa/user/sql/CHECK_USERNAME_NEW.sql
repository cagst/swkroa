SELECT count(*)
  FROM user u
 WHERE u.username = :username
   AND u.active_ind = 1
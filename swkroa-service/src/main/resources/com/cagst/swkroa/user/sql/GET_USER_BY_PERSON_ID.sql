SELECT u.user_id
      ,u.person_id
      ,u.username
      ,u.password
      ,u.temporary_pwd_ind
      ,u.signin_attempts
      ,u.last_signin_dt_tm
      ,u.last_signin_ip
      ,u.account_locked_dt_tm
      ,u.account_expired_dt_tm
      ,u.password_changed_dt_tm
      ,u.active_ind
      ,u.updt_cnt AS user_updt_cnt
      ,u.create_dt_tm AS user_create_dt_tm
      ,u.updt_dt_tm
      ,p.title_cd
      ,p.name_last
      ,p.name_first
      ,p.name_middle
      ,p.locale_language
      ,p.locale_country
      ,p.time_zone
      ,p.updt_cnt AS person_updt_cnt
  FROM user u, person p
 WHERE u.person_id = :person_id
   AND p.person_id = u.person_id

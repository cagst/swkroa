SELECT p.person_id
      ,p.title_cd
      ,p.name_last
      ,p.name_first
      ,p.name_middle
      ,p.locale_language
      ,p.locale_country
      ,p.updt_cnt AS person_updt_cnt
      ,p.active_ind
  FROM person p
 WHERE p.person_id = :person_id

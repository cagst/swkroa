UPDATE person
   SET title_cd        = :title_cd
      ,name_last       = :name_last
      ,name_last_key   = :name_last_key
      ,name_first      = :name_first
      ,name_first_key  = :name_first_key
      ,name_middle     = :name_middle
      ,locale_language = :locale_language
      ,locale_country  = :locale_country
      ,time_zone       = :time_zone
      ,active_ind      = :active_ind
      ,updt_id         = :updt_id
      ,updt_dt_tm      = CURRENT_TIMESTAMP
      ,updt_cnt        = updt_cnt + 1
 WHERE person_id = :person_id
   AND updt_cnt  = :person_updt_cnt

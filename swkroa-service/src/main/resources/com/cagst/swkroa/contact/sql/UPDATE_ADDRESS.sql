UPDATE address
   SET address_type_cd = :address_type_cd
      ,address1        = :address1
      ,address2        = :address2
      ,address3        = :address3
      ,city            = :city
      ,state_code      = :state_code
      ,postal_code     = :postal_code
      ,country_code    = :country_code
      ,active_ind      = :active_ind
      ,updt_id         = :updt_id
      ,updt_dt_tm      = CURRENT_TIMESTAMP
      ,updt_cnt        = updt_cnt + 1
WHERE address_id = :address_id
  AND updt_cnt   = :updt_cnt

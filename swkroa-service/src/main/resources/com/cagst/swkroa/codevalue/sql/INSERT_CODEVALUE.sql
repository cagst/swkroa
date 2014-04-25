INSERT INTO codevalue (codeset_id
                      ,codevalue_display
                      ,codevalue_meaning
                      ,active_ind
                      ,create_id
                      ,create_dt_tm
                      ,updt_id
                      ,updt_dt_tm)
              VALUES (:codeset_id
                     ,:codevalue_display
                     ,:codevalue_meaning
                     ,:active_ind
                     ,:create_id
                     ,CURRENT_TIMESTAMP
                     ,:updt_id
                     ,CURRENT_TIMESTAMP)

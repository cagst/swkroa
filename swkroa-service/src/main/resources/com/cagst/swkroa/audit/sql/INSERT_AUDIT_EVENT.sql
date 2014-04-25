INSERT INTO audit_log (audit_event_type
                      ,audit_action
                      ,audit_instigator
                      ,audit_message
                      ,create_dt_tm)
     VALUES (:audit_event_type
            ,:audit_action
            ,:audit_instigator
            ,:audit_message
            ,CURRENT_TIMESTAMP)
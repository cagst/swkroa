SELECT @dues_payment := codevalue_id
  FROM codevalue
 WHERE codeset_id = 8
   AND codevalue_meaning = 'TRANS_PAYMENT';

SELECT @base_dues := codevalue_id
  FROM codevalue
 WHERE codeset_id = 8
   AND codevalue_meaning = 'TRANS_DUES_BASE';

UPDATE codevalue
   SET active_ind = 0
 WHERE codevalue_id = @dues_payment;

UPDATE transaction_entry
   SET transaction_entry_type_cd = @base_dues
 WHERE transaction_entry_type_cd = @dues_payment;

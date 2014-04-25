SELECT count(*)
  FROM member m
 WHERE m.owner_ident LIKE :ownerIdent
   AND m.active_ind = 1
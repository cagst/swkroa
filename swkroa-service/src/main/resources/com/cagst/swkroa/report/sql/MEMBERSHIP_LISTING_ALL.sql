SELECT ms.membership_id AS membershipId
      ,cv1.codevalue_display AS membershipTypeDisplay
      ,m.member_id AS memberId
      ,m.company_name AS companyName
      ,m.owner_ident AS ownerIdent
      ,p.name_last AS nameLast
      ,p.name_first AS nameFirst
      ,p.name_middle AS nameMiddle
      ,concat_ws(', ', p.name_last, p.name_first) AS nameFull
  FROM membership ms
      ,codevalue cv1
      ,member m
  LEFT OUTER JOIN person p ON (p.person_id = m.person_id)
 WHERE ms.active_ind = 1
   AND cv1.codevalue_id = ms.membership_type_cd
   AND m.membership_id = ms.membership_id
   AND m.active_ind = 1
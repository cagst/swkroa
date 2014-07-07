-- Transactions with no payments
select t.membership_id
      ,t.transaction_id
      ,t.transaction_desc
      ,t.transaction_amount
      ,0.0 as amount_paid
      ,t.transaction_amount as amount_remaining
  from transaction t
 where t.transaction_type_flag = 0
   and t.active_ind = 1
   and not exists (select *
                     from transaction_entry te
                    where te.related_transaction_id = t.transaction_id
                      and te.active_ind = 1)
union
-- Transactions with partial payments
select t.membership_id
      ,t.transaction_id
      ,t.transaction_desc
      ,t.transaction_amount
      ,iq.amount_paid
      ,t.transaction_amount + iq.amount_paid as amount_remaining
  from (select t.transaction_id
              ,sum(te.transaction_entry_amount) as amount_paid
          from transaction t
              ,transaction_entry te
         where te.related_transaction_id = t.transaction_id
           and te.active_ind = 1
         group by t.transaction_id
      )iq
      ,transaction t
where t.transaction_id = iq.transaction_id
  and t.active_ind = 1
  and t.transaction_amount + iq.amount_paid != 0.0

select arena.set_pa_ctxt('admin', null);
select arena.set_pa_ctxt(null, 1714159772);    --ZDemo Gold (admindemo) Views

select hr.ttcustomer_id, hr.human_resource_id, hr.first_name, hr.last_name, hr.is_active, hr.hire_date, 
  hr.ts_approver_id, ts.first_name as ts_approver_first_name, ts.last_name as ts_approver_last_name, 
  ro.role_id, ro.title as role
  from arena.t_human_resource hr
  join arena.t_human_resource ts on (ts.ttcustomer_id = hr.ttcustomer_id and ts.human_resource_id = hr.ts_approver_id)
  join arena.t_role ro on (ro.ttcustomer_id = hr.ttcustomer_id and ro.role_id = hr.admin_id)
 where hr.ttcustomer_id = 1714159772 
   ;
   
update arena.t_entity_band_field
set lock_column = 2   
where band_id = 468   
  and field_id = 30702
  and band_scope_id = in (
  select category_id 
  from arena.t_intake_request_category cat
   join arena.t_ttcustomer cust ON(cat.TTCUSTOMER_ID = cust.TTCUSTOMER_ID)
  where cust.CURRENT_ENTRY = 1
    and cust.IS_ACTIVE = 1
    and cat.TITLE = 'Request');
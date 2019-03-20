select arena.set_pa_ctxt('admin', null);
select arena.set_pa_ctxt(null, 1714159772);    --ZDemo Gold (admindemo) Views


SET default_tablespace = arena_data;

SET ROLE arena;
REFRESH materialized view arenaadmin.mv_ts_res_sum_by_type; 
REFRESH materialized view arenaadmin.mv_ts_period_sum_by_type; 

delete from arena.changelog where id = 20181130093300;
delete from arena.changelog where id = 20181130132800;
delete from arena.changelog where id = 20181219141400;

ALTER TABLE arena.t_intake_request 
DROP CONSTRAINT IF EXISTS fk_intake_request_gate_journey;

--Zdemo gold ttcustomer_id = 1714159772

CREATE INDEX IF NOT EXISTS t_gate_journey_entity_ix ON arena.t_gate_journey (ttcustomer_id, entity_id, entity_type_id) TABLESPACE arena_ndx;

select count(*) from arena.t_internal_ttobject_method;
--where filter_visibility = 0; 

DROP VIEW arena.all_gate;
DROP VIEW arenaadmin.all_gate;

ALTER TABLE t_gate
ADD COLUMN alert_event_id NUMERIC(38);
ALTER TABLE ht_gate
ADD COLUMN alert_event_id NUMERIC(38);

ALTER TABLE arena.cu_page_view ALTER view_name TYPE varchar(1000);
ALTER TABLE arena.t_gate DROP COLUMN approver_id;
ALTER TABLE arena.t_internal_calculation_type 
  ALTER COLUMN current_entry TYPE NUMERIC(1); 
ALTER TABLE arena.t_intake_request 
  ADD CONSTRAINT fk_intake_request_gate_journey FOREIGN KEY (ttcustomer_id, gate_journey_id) REFERENCES t_gate_journey (ttcustomer_id, gate_journey_id);
ALTER TABLE arena.t_internal_calculation_type
  ADD CONSTRAINT chk_internal_calculation_type_current_entry CHECK ((current_entry = ANY(ARRAY[(0)::NUMERIC, (1)::NUMERIC])));  
ALTER TABLE arena.t_internal_calculation_type
 DROP CONSTRAINT chk_internal_calculation_type_current_entry;
ALTER TABLE arena.t_internal_ttobject_method DROP COLUMN report_title_template; 
ALTER TABLE arena.t_internal_ttobject_method ADD COLUMN report_title_template varchar(80);

CREATE OR REPLACE VIEW arena.all_gate AS
  SELECT 1 AS current_entry, g.* FROM arena.t_gate g
  UNION ALL
  SELECT 0 AS current_entry, g.* FROM arena.ht_gate g;

SELECT arena.migration_util$clone_view('all_gate', 'arena', 'arenaadmin', 'arenaadmin');

SELECT
  1 AS level, note_id, current_entry, created_by, create_date, modified_by, modify_date, title, cl_content_id, parent_id, parent_type_id, ttcustomer_id
FROM arenaadmin.current_note
WHERE parent_type_id = 307 AND parent_id = 1949362691
ORDER BY modify_date;

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

  
select ebf.entity_band_field_id, ebf.field_id, ebf.band_scope_id, ebf.band_id, 
    tom.field_type_id, tom.field_subtype_id, tom.description, tom.user_class_id
  from arena.t_entity_band_field ebf 
  join arena.t_internal_ttobject_method tom on (ebf.field_id = tom.method_id)
 where ebf.ttcustomer_id = 1714159772 
   and ebf.band_id = 462    --project.Details genband id
   and (tom.field_type_id != 9)
   and ebf.band_scope_id in (
    select pc.category_id 
    from arenaadmin.active_project_category pc
    where pc.has_request_associations = 1
    )
 order by ebf.band_scope_id, ebf.field_id; 

DROP VIEW arena.all_gate;
DROP VIEW arenaadmin.all_gate;
DROP VIEW arena.all_gate_section;
DROP VIEW arenaadmin.all_gate_section;
DROP VIEW arena.all_gate_field;
DROP VIEW arenaadmin.all_gate_field;

DROP TABLE if exists arena.t_gate_section;
DROP TABLE if exists arena.t_gate_field;
DROP TABLE if exists arena.t_gate;
DROP TABLE if exists arena.ht_gate_section;
DROP TABLE if exists arena.ht_gate_field;
DROP TABLE if exists arena.ht_gate;

drop view arenaadmin.active_issue_category;

create view arenaadmin.active_issue_category AS
 SELECT 1 AS current_entry, ic.* 
   FROM arena.t_issue_category ic
  WHERE ic.is_active = 1::numeric;

SELECT intake_request.* 
FROM arenaadmin.all_intake_request intake_request 
WHERE (intake_request.intake_request_id = 1949361296) 
AND intake_request.ttcustomer_id = 1714159772
ORDER BY intake_request.intake_request_id, intake_request.modify_date desc;

ALTER TABLE arena.t_jsp_menu ALTER COLUMN category_entity_id TYPE NUMERIC(38);
ALTER TABLE arena.t_jsp_menu ALTER COLUMN category_entity_id TYPE BIGINT USING category_entity_id::bigint;
ALTER TABLE arena.t_jsp_menu ALTER COLUMN dd_entity_id TYPE NUMERIC(38);
ALTER TABLE arena.t_jsp_menu ALTER COLUMN helper_function TYPE NUMERIC(38);

ALTER TABLE arena.t_intake_request_category
DROP COLUMN send_backwards;
ALTER TABLE arena.ht_intake_request_category
DROP COLUMN send_backwards;

ALTER TABLE arena.t_intake_request_category
ADD COLUMN allow_send_backwards NUMERIC(1) DEFAULT 0;
ALTER TABLE arena.t_intake_request_category
ADD CONSTRAINT chk_intake_request_allow_send_backwards CHECK ((allow_send_backwards = ANY(ARRAY[(0)::NUMERIC, (1)::NUMERIC])));
ALTER TABLE arena.ht_intake_request_category
ADD COLUMN allow_send_backwards NUMERIC(1) DEFAULT 0;
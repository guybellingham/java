select arena.set_pa_ctxt('admin', null);

SET default_tablespace = arena_data;

SET ROLE arena;


DROP VIEW arena.all_gate;
DROP VIEW arenaadmin.all_gate;

ALTER TABLE t_gate
ADD COLUMN alert_event_id NUMERIC(38);
ALTER TABLE ht_gate
ADD COLUMN alert_event_id NUMERIC(38);

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
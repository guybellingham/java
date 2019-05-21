SELECT arena.set_pa_ctxt('admin', NULL);

select ae.ttcustomer_id, ae.alert_event_id, ae.send_to, ae.alert_subject, ae.entity_type_id, ae.entity_id, 
        sj.job_id, sj.title, sj.period, sj."hour", sj."minute", sj.next_run, sj.create_date, sj.time_zone 
  from arena.t_alert_event ae 
  join arena.t_scheduled_job sj ON sj.ttcustomer_id = ae.ttcustomer_id and sj.job_id = ae.scheduled_job_id
where ae.ttcustomer_id = 1585886522
  and ae.current_entry = 1
  and sj.job_type = 'com.innotas.taskscheduler.TimesheetAlertsScheduleJob'
  and sj.is_active = 1
  and sj.current_entry = 1;
  
select *
  from arena.t_scheduled_job sj 
 where sj.ttcustomer_id = 1585886522
   and sj.job_id = 1710978461
   and sj.current_entry = 1; 
   
select *
  from arena.t_alert_event ae
 where ae.ttcustomer_id = 1585886522
   and ae.scheduled_job_id = 1710978461
   and ae.current_entry = 1; 
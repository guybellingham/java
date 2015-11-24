package com.gus.streams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

public class AllocationBeanFilterTest {

	
	private static final SimpleDateFormat MMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");
	static final Date EOY = getEndOfYear();
	static final Date SNY = getStartOfNextYear(); 
	static final Date SNY12 = getPlusMonths(SNY,12);
	static final RoleBean[] roles = 	{
			new RoleBean(702510,"Project Manager","Project Manager",true,true),
			new RoleBean(702523,"Architect","Software Architect",true,true),
			new RoleBean(702535,"Lead Developer","Developer I",true,true),
			new RoleBean(702547,"Senior Developer","Developer II",true,true),
			new RoleBean(702559,"Developer","Developer III",true,true),
			new RoleBean(702561,"QA Lead","QA I",true,true),
			new RoleBean(702573,"QA Engineer","QA II",true,true),
			new RoleBean(702585,"Tester","QA Tester",true,true),
			new RoleBean(702597,"Sr Analyst","Business Analyst I",true,true),
			new RoleBean(702597,"Analyst","Business Analyst II",true,true),
			new RoleBean(702601,"Manager","Manager",true,false),
			new RoleBean(702613,"Database Admin","DBA I",true,true),
			new RoleBean(702625,"Tech Lead","Tech Ops I",true,true),
			new RoleBean(702625,"Tech","Tech Ops II",true,true),
			new RoleBean(702625,"UX","UX Designer",true,true),
			new RoleBean(702625,"Sr Web Developer","Web Developer I",true,true),
			new RoleBean(702625,"Web Developer","Web Developer II",true,true),
			new RoleBean(702625,"Jr Developer","Developer IV",true,true),
			new RoleBean(702625,"Intern","Intern",true,true),
			new RoleBean(702625,"Consultant","Consultant",true,true),
	};
	
	static final String[] firstNameArray = {
			"Clark",	"Bruce",	"Otto",		"Bruce",	"Norman",	
			"Harry",	"Dick",		"Anthony",	"Natalia",	"Wanda",	
			"Lois",		"Jimmy",	"Remy",		"James",	"Steve",
			"Karl",		"Hank",		"Scott",	"Victor",	"Norrin",
			"Ororro",	"Benjamin",	"Jake",		"Stephen",	"Alec",
	};
	static final String[] lastNameArray = {
			"Kent",		"Wayne",	"Octavius",	"Banner",	"Osborn",
			"Osborn",	"Grayson",	"Stark",	"Romanova",	"Maximoff",
			"Lane",		"Olson",	"LeBeau",	"Howlett",	"Rogers",
			"Mordo",	"McCoy",	"Lang",		"Creed",	"Rad",
			"Munroe",	"Grimm",	"Olsen",	"Strange",	"Holland",
	};
	static final List<ResourceBean> resourceList = generateResourceList(50);
	
	static Date getEndOfYear() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("PST"));
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		System.out.println("Generating EndOfYear "+calendar.getTime());
		return calendar.getTime();
	}
	static Date getStartOfNextYear() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("PST"));
		calendar.add(Calendar.YEAR,1);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		System.out.println("Generating StartOfNextYear "+calendar.getTime());
		return calendar.getTime();
	}
	static Date getPlusMonths(Date date, int months) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("PST"));
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}
	static Date getPlusDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("PST"));
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}
	
	
	static final List<ProjectBean> projectList = generateProjectList(200,10,new Date(2015,12,29),new Date(2016,12,31)); 
	
	static List<ProjectBean> generateProjectList(int size, int days, Date startDate, Date endDate) {
		List<ProjectBean> projects = new ArrayList<>(size);
		
		Calendar startCalendar = new GregorianCalendar(TimeZone.getTimeZone("PST"));
		startCalendar.setTime(startDate);
		Calendar endCalendar = new GregorianCalendar(TimeZone.getTimeZone("PST"));
		endCalendar.setTime(endDate);
		int index = 0; 
		Random rand = new Random();
		long id = Math.round(rand.nextDouble() * 999999);
		projects.add(new ProjectBean(id,"Project A","Project in APPROVAL phase",ProjectStatus.APPROVAL,SNY,SNY12));
		projects.add(new ProjectBean(id+=13,"Project F","Project in FEASIBILITY phase",ProjectStatus.FEASIBILITY,SNY,SNY12));
		projects.add(new ProjectBean(id+=13,"Project I","Project in INVESTIGATION phase",ProjectStatus.INVESTIGATION,SNY,SNY12));
		projects.add(new ProjectBean(id+=13,"Project O","Project in ONHOLD",ProjectStatus.ONHOLD,SNY,SNY12));
		projects.add(new ProjectBean(id+=13,"Project T","Project is TERMINATED",ProjectStatus.TERMINATED,SNY,SNY12));
		for (index = 4; index < size; index++) {
			id+=13;
			projects.add( new ProjectBean(id, "Project "+id, "Description "+id,ProjectStatus.INPROGRESS,startCalendar.getTime(),endCalendar.getTime()) );
			startCalendar.add(Calendar.DAY_OF_YEAR, days);
			endCalendar.add(Calendar.DAY_OF_YEAR, days);	
		}
		System.out.println("Generated "+projects.size()+" projects from startDate "+MMDDYYYY.format(startDate)+" to endDate "+MMDDYYYY.format(endDate)+".");
		System.out.println("First project="+projects.get(0));
		System.out.println("Last project="+projects.get(size-1));
		return projects;
	}
	
	static List<ResourceBean> generateResourceList(int size) {
		List<ResourceBean> resources = new ArrayList<>(size);
		Random rand = new Random();
		int index = 0; 
		long id = Math.round(rand.nextDouble() * 999999);
		resources.add(new ResourceBean(1,"Not","Available",false,false));
		resources.add(new ResourceBean(2,"Tina","Terminated",false,true));
		for (index = 2; index < size; index++) {
			id+=13;
			resources.add( new ResourceBean(id, firstNameArray[rand.nextInt(25)], lastNameArray[rand.nextInt(25)], true, false) );
		}
		return resources;
	}
	
	public static List<AllocationBean> generateAllocationList() {
		List<AllocationBean> allocationList = new ArrayList<AllocationBean>();
		Random rand = new Random();
		long id = Math.round(rand.nextDouble() * 999999); 
		System.out.println("Generating Allocations for "+projectList.size()+" projects and "+resourceList.size()+" resources, starting with id "+id+" ...");
		for (Iterator<ProjectBean> projectIter = projectList.iterator(); projectIter.hasNext();) {
			ProjectBean project = projectIter.next();
			Date startDate = project.getStartDate();
			Date endDate = project.getEndDate();
			for (Iterator<ResourceBean> resourceIter = resourceList.iterator(); resourceIter.hasNext();) {
				ResourceBean resource = resourceIter.next();
				RoleBean role = roles[rand.nextInt(20)];
				id += 13;
				allocationList.add(new AllocationBean(id,project,role,resource,startDate,endDate));
			}
		}
		System.out.println("Generated "+allocationList.size()+" allocations");
		System.out.println("First allocation="+allocationList.get(0));
		return allocationList;
	}

	@Test
	public void testParallelFilterList() {
		List<AllocationBean> allocationList = generateAllocationList();
		long start = System.currentTimeMillis(), stop = 0L;
		System.out.println("Parallel Filtering list of "+allocationList.size()+" allocations ...");
		List<AllocationBean> filteredAllocations = allocationList.parallelStream()
			.filter(allocation -> (
					!allocation.getProject().isClosed()) && 
					 allocation.getProject().getStartDate().after(EOY) && 
					 allocation.getResource().isAvailable()
					)
			.collect(Collectors.toCollection(ArrayList::new));
		stop = System.currentTimeMillis();
		System.out.println("Parallel Filtered list of "+filteredAllocations.size()+" allocations in "+(stop - start)+" msec.");
	}
	@Test
	public void testSequentialFilterList() {
		List<AllocationBean> allocationList = generateAllocationList();
		long start = System.currentTimeMillis(), stop = 0L;
		System.out.println("Sequential Filtering list of "+allocationList.size()+" allocations ...");
		List<AllocationBean> filteredAllocations = allocationList.stream()
			.filter(allocation -> (
					!allocation.getProject().isClosed()) && 
					 allocation.getProject().getStartDate().after(EOY) && 
					 allocation.getResource().isAvailable()
					)
			.collect(Collectors.toCollection(ArrayList::new));
		stop = System.currentTimeMillis();
		System.out.println("Sequential Filtered list of "+filteredAllocations.size()+" allocations in "+(stop - start)+" msec.");
	}
	@Test
	public void testIterativeFilterList() {
		List<AllocationBean> allocationList = generateAllocationList();
		long start = System.currentTimeMillis(), stop = 0L;
		System.out.println("Iterative Filtering list of "+allocationList.size()+" allocations ...");
		for (ListIterator<AllocationBean> iterator = allocationList.listIterator(); iterator.hasNext();) {
			AllocationBean allocation = (AllocationBean) iterator.next();
			if(!allocation.getProject().isClosed() && 
				allocation.getProject().getStartDate().after(EOY) && 
				allocation.getResource().isAvailable()) {
				//don't do anything 
			} else {
				iterator.remove();
			}
		}
		stop = System.currentTimeMillis();
		System.out.println("Iterative Filtered list of "+allocationList.size()+" allocations in "+(stop - start)+" msec.");
	}
}

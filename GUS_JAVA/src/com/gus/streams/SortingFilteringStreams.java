package com.gus.streams;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import org.dozer.Mapping;
/**
 * App to demo the different ways of sorting and filtering Streams.
 * @author Gus
 */
public class SortingFilteringStreams {

	public static void main(String[] args) {
		int[] intArr = {2,4,8,16,32,64,128,256}; 		
		double[] doubleArr = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
		String[] fruit = {"apple", "banana", "clementine", "apple", "cherry", "banana", "apple"};
		String[] numberTextArr = {"2", "9", "6", "15", "22", "10"};
		
		NumberFormat nmf = NumberFormat.getCurrencyInstance();
		
		Stream<String> unsortedStringStm = Stream.of(numberTextArr);
		System.out.print("Unsorted number text: ");
		unsortedStringStm.forEach(str -> System.out.print(str + " "));
		System.out.println(" ");
		//1. Sort numbers as text
		unsortedStringStm = Stream.of(numberTextArr);
		System.out.print("Sorted number text: ");
		unsortedStringStm
			.sorted()
			.forEach(str -> System.out.print(str + " "));
		System.out.println(" ");
		
		//2. Map to int and Sort as integers
		unsortedStringStm = Stream.of(numberTextArr);
		System.out.print("Number text mapToInt sorted: ");
		unsortedStringStm
			.mapToInt(str -> Integer.parseInt(str))
			.sorted()
            .forEach(str -> System.out.print(str + " "));
		System.out.println(" ");
		
		//3. Count fruit by their names 
		System.out.println("Fruit Counts:");
		Map<String, Long> fruitCount = Arrays.stream(fruit)
			.collect(
					Collectors.groupingBy(Function.identity(), Collectors.counting())
					);
		
		fruitCount.forEach((name, count) -> {
			System.out.println("fruit:"+name+" count="+count);
		});
		System.out.println(" ");
		System.out.println("Fruit by Count descending:");
		fruitCount.entrySet()
			.stream()
			.sorted(Map.Entry.<String,Long>comparingByValue().reversed())    //by the highest count 3, 2, 1
			.forEachOrdered(entry -> { System.out.println("Map.Entry. key:"+entry.getKey()+",value:"+entry.getValue()); });
		//Map.Entry<String,Long>
		System.out.println(" ");
		
		 //4. Filter a Set of ProjectBeans (sorted by Budget ascending as you go down the set) that are not TERMINATED!
        Set<ProjectBean> projects = new TreeSet<>(	Comparator.comparingDouble(proj -> proj.getBudget()) );
        projects.add(new ProjectBean(1, 125549.50D, "Project1","CRM Modernization",ProjectStatus.APPROVAL, new Date(2019, 11, 30), null));
        projects.add(new ProjectBean(2, 2450000.50D, "Project2","Security Compliance",ProjectStatus.APPROVAL, new Date(2020, 1, 31), null));
        projects.add(new ProjectBean(3, 3100500D, "Project3","Accounts Payable",ProjectStatus.TERMINATED, new Date(2019, 8, 31), new Date(2019, 10, 15)));
        projects.add(new ProjectBean(4, 90500.50D, "Project4","Mobile Deployment",ProjectStatus.INPROGRESS, new Date(2020, 2, 1), new Date(2020, 12, 31)));
        List<ProjectBean> notTerminated = projects.stream()
        	.filter(proj -> { return proj.getStatus() != ProjectStatus.TERMINATED; })
        	.collect(Collectors.toList());
        System.out.println("Projects != TERMINATED:");
        notTerminated.forEach(System.out::println);
        System.out.println(" ");
        
        //5. Sum of projects budgets that are not TERMINATED
        Double totalBudget = projects.stream()
        	.filter(proj -> proj.getStatus() != ProjectStatus.TERMINATED)
        	.collect(Collectors.summingDouble(proj -> proj.getBudget()));
        System.out.println("Collectors.summingDouble(proj.getBudget()): "+ nmf.format(totalBudget)); 
        System.out.println(" ");
        //6. Projects total budgets grouped by status
        Map<ProjectStatus, Double> projectBudgetByStatus = 
        		projects.stream()
        			.filter(proj -> proj.getStatus() != ProjectStatus.TERMINATED)
        			.collect(Collectors.groupingBy(ProjectBean::getStatus, 
        					Collectors.summingDouble(proj -> proj.getBudget())
        			));
        System.out.println("Collectors.groupingBy(ProjectBean::getStatus) ");
        projectBudgetByStatus.forEach((status, budgetSum ) -> {
        	System.out.println("status="+status+" totalBudget="+ nmf.format(budgetSum));
        });
        System.out.println(" ");
        
        //7. Project names grouped by status 
        Map<ProjectStatus, Set<String>> projectsByStatus = projects.stream()
		.filter(proj -> proj.getStatus() != ProjectStatus.TERMINATED)
		.collect(Collectors.groupingBy(ProjectBean::getStatus, 
				Collectors.mapping(ProjectBean::getName, Collectors.toSet())
		));
        System.out.println("Collectors.mapping(ProjectBean::getName, Collectors.toSet()) ");
        projectsByStatus.forEach((status, name ) -> {
        	System.out.println("status="+status+" projectName="+name);
        });
        System.out.println(" ");
	}

	public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
				+ " Value : " + entry.getValue());
        }
    }

}

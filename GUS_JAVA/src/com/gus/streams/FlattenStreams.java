package com.gus.streams;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
/**
 * App to demo flattening Streams.
 * @author Gus
 */
public class FlattenStreams {

	public static void main(String[] args) {
		//1. Flatten a 2D int array
		int intArr[][] = {{ 1, 2 }, { 3, 4, 5, 6 }, { 7, 8, 9 }};
		Integer[] integerArray = flatten(intArr);
		// Print the flattened array
        System.out.println("flatten 2d int[][]: "+Arrays.toString(integerArray));
        
        //2. Flatten a 2D Character array
        // Can't do it with char here
        Character charArr[][] = {{'G', 'e', 'e', 'k', 's'}, {'F', 'o', 'r'}, {'G'}, {'e', 'e', 'k'}, {'s'}};
        List<Character> charList = flatten(charArr);
        Character[] characterArray = charList.toArray(Character[] ::new); 
        // Print the flattened array
        System.out.println("flatten 2d Character[][]: "+Arrays.toString(characterArray));
        System.out.println("");
        
        //3. Let's try Stream.flatMap() and Arrays.stream() to flatten the Character[][] 
        Stream<Character[]> charStrm = Arrays.stream(charArr);
        Stream<Character> combined = 
        		charStrm.flatMap(arr -> Arrays.stream(arr));        
        System.out.print("flatMap(arr -> Arrays.stream(arr)): ");
        combined.forEach(System.out::print);
        System.out.println("");
        
        //4. Flatten a Map<Integer,List<Integer>)
        Map<Integer, List<Integer>> mapOfIntegerList = new HashMap<>();
        // = {1=[1, 2], 2=[3, 4, 5, 6], 3=[7, 8, 9]};
        mapOfIntegerList.put(1, List.of(1,2));
        mapOfIntegerList.put(2, List.of(3,4,5,6));
        mapOfIntegerList.put(3, List.of(7,8,9));
        List<Integer> integerList = flatten(mapOfIntegerList);
        System.out.println("flatten 2d Map<Integer, List<Integer>>: "+Arrays.toString(integerList.toArray()));
        System.out.println("");
        
       
	}
	
	/**
	 * Method to flatten a 2D int[][]  
	 * @param arr
	 * @return Integer[]
	 */
	public static Integer[] flatten(int[][] arr) {
		List<Integer> list = new ArrayList<>(); 
		
		for(int[] ar1 : arr) {
			Arrays.stream(ar1).forEach(list::add);
		}
		return list.toArray(new Integer[list.size()]);
	}
	/**
	 * Method to flatten a 2D Array of type T  
	 * @param arr T[][]
	 * @return List<T>
	 */
	public static <T> List<T> flatten(T[][] arr) {
		List<T> list = new ArrayList<>(); 
		
		for(T[] ar1 : arr) {
			Arrays.stream(ar1).forEach(list::add);
		}
		return list;
	}
	/**
	 * Method to flatten a Map<Integer, List<T>>.  
	 * @param arr map of Integers to List<T>
	 * @return List<T> concatenation of all the lists of <T>
	 */
	public static <T> List<T> flatten(Map<Integer, List<T>> map) {
		List<T> list = new ArrayList<>(); 
		
		for(List<T> l1 : map.values()) {
			l1.stream().forEach(list::add);
		}
		return list;
	}
}

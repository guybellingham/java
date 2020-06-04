package com.gus.collections;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArraysMergeSorted {
	
	/**
	 * Given 2 sorted int arrays, return 1 array that is the result of merging them: 
	 * <li>Given <code>{1, 2, 4, 5}</code> and <code>{3, 4, 7, 8, 9}</code> the result 
	 * would be {1, 2, 3, 4, 5, 7, 8, 9}.
	 * You're not allowed to use a Hash or Tree Set or Maps.
	 * @param arr - SORTED array of int
	 * @return the integer that is missing from the sorted array or zero
	 */
	public Integer[] mergeSortedArrays(Integer[] arr1, Integer[] arr2) {
	    
	    if (arr1.length == 0) {
	        System.out.println("arr1 is empty");
	        return arr2;
	    } else
	        if (arr2.length == 0) {
	            System.out.println("arr2 is empty");
	            return arr1;
	        } else{
	            List<Integer> list = new ArrayList<>((arr1.length + arr2.length));
	            // i is index for arr1 and j is index for arr2 
	            int i = 0, j = 0;
    	        //2. Starting at the first element do this until you're at the end of BOTH arrays 
    	        do {
    	            System.out.println("i="+i+" j="+j);
    	            // if we've run out of arr1 ..add the rest of arr2?
    	            if (i >= arr1.length && j < arr2.length) {
    	                while (j < arr2.length) {
    	                    list.add( arr2[j] );
    	                    j++;
    	                }
    	                continue;
    	            }
    	            // if we've run out of arr2 ..add rest of arr1?
    	            if (j >= arr2.length && i < arr1.length) {
                        while (i < arr1.length) {
                            list.add( arr1[i] );
                            i++;
                        }
                        continue;
                    }
    	            // we have 2 entries to compare
    	            // if arr1[i] < arr2[j] - add arr1[i] and continue to next i++
                    if (arr1[i] < arr2[j]) {
                        System.out.println("arr1 is lessthan arr1["+i+"]="+arr1[i]);
                        list.add( arr1[i] );
                        i++;
                    } else 
                    // if  arr1[i] == arr2[j] - add arr1[i] , increment j and i
                    if (arr1[i] == arr2[j]) {
                        System.out.println("arrays equal! adding "+arr1[i]);
                        list.add( arr1[i] );
                        j++;  i++; 
                    } else 
                    if (arr1[i] > arr2[j]) {
                        System.out.println("arr1 greaterthan arr2["+j+"]="+arr2[j]);
                        list.add( arr2[j] );
                        j++;
                    } 
    	        } while ( i < arr1.length || j < arr2.length);
                System.out.println("result"+list);
                return list.toArray(new Integer[list.size()]);
	    }
	   
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	@Test
    public void testEmptyElements() {
        Integer[] arr1 = {};
        Integer[] arr2 = {};
        Integer[] result = mergeSortedArrays(arr1, arr2);
        assertEquals(0, result.length);
    }
	@Test
    public void testOneElements() {
	    Integer[] arr1 = {2};
        Integer[] arr2 = {};
        Integer[] result = mergeSortedArrays(arr1, arr2);
        assertEquals(1, result.length);
        assertTrue(result[0].equals(2));
	}
	@Test
    public void testTwoElementsEqual() {
	    Integer[] arr1 = {2};
        Integer[] arr2 = {2};
        Integer[] result = mergeSortedArrays(arr1, arr2);
        assertEquals(1, result.length);
        assertTrue(result[0].equals(2));
    }    
	@Test
    public void testTwoElements() {
        Integer[] arr1 = {2};
        Integer[] arr2 = {1};
        Integer[] result = mergeSortedArrays(arr1, arr2);
        assertEquals(2, result.length);
        assertTrue(result[0].equals(1));
    } 
	@Test
    public void testSimple() {
        Integer[] arr1 = {2, 3, 5, 7, 9};
        Integer[] arr2 = {1, 3, 4, 5, 6, 8};
        Integer[] result = mergeSortedArrays(arr1, arr2);
        assertEquals(9, result.length);
        assertTrue(result[0].equals(1));
    }
	@Test
    public void testComplex() {
        Integer[] arr1 = {0, 1, 2, 3, 5, 7, 9};
        Integer[] arr2 = {1, 3, 4, 5, 6, 7, 8, 10};
        Integer[] result = mergeSortedArrays(arr1, arr2);
        assertEquals(11, result.length);
        assertTrue(result[0].equals(0));
    }
}

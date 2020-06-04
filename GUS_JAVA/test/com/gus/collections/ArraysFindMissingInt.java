package com.gus.collections;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ArraysFindMissingInt {
	
	/**
	 * Given a sorted int array, find the missing integer in the sequence: 
	 * <li><code>{1, 2, 4, 5}</code> = 3 is missing!
	 * <li><code>{4, 6, 7, 8, 9}</code> = 5 is missing!
	 * @param arr - SORTED array of int
	 * @return the integer that is missing from the sorted array or zero
	 */
	public int findMissing(int[] arr) {
	    int result = 0;
	    if (arr.length >= 2) {
	        //1. array is already sorted 
	        //2. Starting at the first element -> penultimate
	        for (int i = 0; i < (arr.length - 1); i++) {
                if (arr[i] + 1 < arr[i+1]) {
                    result = arr[i] + 1;
                    System.out.println(" i="+i+" missing="+result);
                }
	        }
	    }
	    
	    return result;
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	@Test
    public void testEmptyElements() {
        int[] array = {};
        int result = findMissing(array);
        assertEquals(0, result);
    }
	@Test
    public void testTwoElements() {
        int[] array = {1,2};
        int result = findMissing(array);
        assertEquals(0, result);
	}
	@Test
    public void testTwoElementsWithMissing() {
        int[] array = {1,3};
        int result = findMissing(array);
        assertEquals(2, result);
    }    
	@Test
	public void testNoMissing() {
	    int[] array1 = {4,5,6,7,8};
		int result = findMissing(array1);
		assertEquals(0, result);
	}
	@Test
    public void testOneMissing() {
	    // 1 + 3 = 4
        int[] array1 = {3,4,5,6,8};
        int result = findMissing(array1);
        assertEquals(7, result);
    }
	
}

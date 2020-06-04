package com.gus.collections;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ArraysFindTripletsTest {
	
	/**
	 * Find all combinations of integers x, y and z in the given array where: 
	 * <code>x + y = z</code>.
	 * @param arr
	 * @return number of triplets found
	 */
	public int findTriplets(int[] arr) {
	    int count = 0;
	    if (arr.length > 2) {
	        //1. This is much easier if the array is sorted first 
	        Arrays.sort(arr);
	        //2. Starting at the first element -> 2nd from last element
	        for (int i = 0; i < (arr.length - 2); i++) {
	            //3. Compare arr[i] with all the following elements -> penultimate
	            for (int j = i+1; j < (arr.length - 1); j++) {
	                if (arr[i] + arr[j] == arr[j+1]) {
	                    count++;
	                    System.out.println(count+". i="+i+" j="+j+" "+arr[i]+" + "+arr[j]+" == "+arr[j+1]);
	                }
                }
	        }
	    }
	    
	    return count;
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
        int count = findTriplets(array);
        assertEquals(0, count);
    }
	@Test
    public void testOnlyTwoElements() {
        int[] array = {1,3};
        int count = findTriplets(array);
        assertEquals(0, count);
	}
        
	@Test
	public void testNoTriplets() {
	    int[] array1 = {1,3,7,9,15};
		int count = findTriplets(array1);
		assertEquals(0, count);
	}
	@Test
    public void testOneTriplets() {
	    // 1 + 3 = 4
        int[] array1 = {1,3,4,9,15};
        int count = findTriplets(array1);
        assertEquals(1, count);
    }
	@Test
    public void testTwoTriplets() {
        // 1 + 3 = 4,  1 + 9 = 10
        int[] array1 = {1,3,4,9,10};
        int count = findTriplets(array1);
        assertEquals(2, count);
    }
	@Test
    public void testFourTriplets() {
        // 1 + 3 = 4,  3 + 4 = 7,  3 + 7 = 10
        int[] array1 = {1,3,4,7,10};
        int count = findTriplets(array1);
        assertEquals(3, count);
    }
}

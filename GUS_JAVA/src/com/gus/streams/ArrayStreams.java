package com.gus.streams;
import java.util.*; 
import java.util.stream.*;
/**
 * App to demo the differences between <code>Arrays.stream(arr)</code> 
 * and <code>Stream.of(arr) </code>.
 * @author Gus
 *
 */
public class ArrayStreams {

	public static void main(String[] args) {
		int[] intArr = {2,4,8,16,32,64,128,256}; 
		long[] longArr = {5000000000L, 6000000000L, 7000000000L};
		double[] doubleArr = {1.0, 2.0, 3.141, 4.0, 5.0, 6.0};
		char[] charArr = {'a', 'b', 'c', 'd', 'e', 'f'};
		String[] numberTextArr = {"2", "4", "6", "8"};
		
		//Arrays.stream can ONLY be used with arrays of built-in types int, long, double.
		//It returns the corresponding IntStream, LongStream, DoubleStream (there is no FloatStream)
		IntStream intStm = Arrays.stream(intArr);
		// Displaying elements in IntStream 
		System.out.println(" ");
		System.out.print("int stream forEach: ");
        intStm.forEach(str -> System.out.print(str + " "));  					//terminating operation closes the Stream!
        //You get an IllegalStateException if you try another stream OP on the same stream!
		//System.out.println("int stream average: "+intStm.average());
        
		//Stream.of() is generic and can be used with other types of objects.
		//If you use it to create a Stream of int, you have to then 'flatten' it 
        Stream<int[]> stream = Stream.of(intArr); 
        // flattenning Stream<int[]> into IntStream using flatMapToInt() 
        IntStream intStreamNew = stream.flatMapToInt(Arrays::stream);
        System.out.println(" ");
        System.out.println("int stream new sum: "+intStreamNew.sum());  		//terminating operation
        
        
		LongStream longStm = Arrays.stream(longArr);
		//Java lambdas use the -> single arrow whereas JavaScript uses the => double arrow!
		System.out.println(" ");
		System.out.print("long stream forEach: ");
		longStm.forEach(num -> System.out.print(num + ","));   //terminating operation closes the Stream
		//You get an IllegalStateException if you try another stream OP on the closed stream!
		//System.out.println("long stream min: "+longStm.min());
		System.out.println(" ");
		System.out.println("long stream max: "+Arrays.stream(longArr).max());
		System.out.println("long stream count: "+Arrays.stream(longArr).count());		
		//no such class - won't compile CharStream charStm = Arrays.stream(charArr);
		
		DoubleStream doubleStm = Arrays.stream(doubleArr);
		System.out.println(" ");
		System.out.print("double stream forEach: ");
		doubleStm.forEach(dbl -> System.out.print(dbl + " "));
		DoubleStream doubleSubStm = Arrays.stream(doubleArr,2,6);
		System.out.println(" ");
		System.out.print("double stream(2,6) forEach: ");
		doubleSubStm.forEach(dbl -> System.out.print(dbl + " "));
		
		//Stream.of characters!
		Stream<char[]> charStm = Stream.of(charArr); 
        // Displaying elements in Stream 
		System.out.println(" ");
		System.out.print("Stream<char[]>: ");
		charStm.forEach(System.out::print);  			//terminating and methodethod reference!
		System.out.println(" ");
		
        //Stream of text "2","4","6" 
        Stream<String> numberStm = Stream.of(numberTextArr);
        System.out.print("Stream<String>: ");
        numberStm.forEach(System.out::print);  			//terminating and method reference!
		System.out.println(" ");
		numberStm = Stream.of(numberTextArr);  			//recreate the stream!
        //Using Collectors to average the number Strings!
        double avg =  numberStm.collect( Collectors.averagingInt(nbr -> Integer.parseInt(nbr)) );        
        System.out.println("Collectors.averagingInt: "+avg);
        
	}

}

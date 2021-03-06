package com.gus.thread;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FibonacciNumbers implements Runnable {
	
	private static final Logger logger = Logger.getLogger("com.gus.thread");
	
	public FibonacciNumbers(int size) {
		setSize(size);
	}
	private int size; 
	
	
	@Override
	public void run() {
		long startTime = 0L, elapsedTime = 0L;
        
        	startTime = System.nanoTime();
        	logger.log(Level.FINEST, "FibonacciNumbers("+getSize()+") starting...");
		
        BigInteger[] fibonacci = new BigInteger[size];
		calculateNumbers(fibonacci);
		
		logger.info(printFibonacci(fibonacci));
		
		
        	elapsedTime = System.nanoTime() - startTime;
        	logger.log(Level.FINEST, "FibonacciNumbers finished elapsed=" + (elapsedTime / 1000000.0) + " msec");
        
	}
	
	//set up the fibonacci sequence
	public void calculateNumbers(BigInteger[] fibonacci) {
	    
		fibonacci[0] = BigInteger.ONE;
		fibonacci[1] = BigInteger.ONE;       // we know the sequence always starts with 1,1
	    
		logger.log(Level.FINEST, "FibonacciNumbers("+getSize()+") calculating...");
		
	    for (int i=2;i<fibonacci.length;i++) {
	        //This value is the sum of the previous two values in the array
	    	fibonacci[i] = (fibonacci[i-1].add( fibonacci[i-2] ));
	    	Thread.yield();   //be a good thread
	    }
	    logger.log(Level.FINEST, "FibonacciNumbers("+getSize()+") calculating done.");
		
	}
	
	public String printFibonacci(BigInteger[] fibonacci) {
		String threadName = Thread.currentThread().getName();  //get the Thread that is running me!
		StringBuilder sb = new StringBuilder();   //local variables are Thread safe (stored on the stack) 
		
		logger.log(Level.FINEST, threadName+" printing Fibonacci numbers...");
		
		for (int i=0;i<fibonacci.length;i++) {
			 if(i>0){
				 sb.append(","); 
				 sb.append(fibonacci[i]);
			 } else {
				 sb.append(fibonacci[i]);
			 }
			 Thread.yield();   //be a good thread
		}
		logger.log(Level.FINEST, threadName+" finished printing Fibonacci numbers.");
		return sb.toString();
	}

	public int getSize() {
		return size;
	}
	protected void setSize(int size) {
		this.size = size;
	}

}

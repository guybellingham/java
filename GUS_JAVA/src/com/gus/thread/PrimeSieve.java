package com.gus.thread;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrimeSieve implements Runnable {
	
	private static final Logger logger = Logger.getLogger("com.gus.thread");
	
	public PrimeSieve(int size) {
		setSize(size);
	}
	
	private int size;
	private boolean[] primes;
	
	@Override
	public void run() {
		long startTime = 0L, elapsedTime = 0L;
        
        	startTime = System.nanoTime();
        	logger.log(Level.FINEST, "PrimeSieve("+getSize()+") starting...");
		

        setPrimes(new boolean[getSize()]);
        
		fillSieve();
		
		logger.log(Level.FINEST, printPrimes());
		
		
        	elapsedTime = System.nanoTime() - startTime;
        	logger.log(Level.FINEST, "PrimeSieve finished elapsed=" + (elapsedTime / 1000000.0) + " msec");
        
	}
	
	//set up the primesieve
	public void fillSieve() {
	    Arrays.fill(primes,true);        // assume all integers are prime!
	    primes[0]=primes[1]=false;       // we know 0 and 1 are not prime.
	    
	    logger.log(Level.FINEST, "PrimeSieve("+getSize()+") sieving...");
		
	    for (int i=2;i<primes.length;i++) {
	        //starting from 2 if the number is prime, 
	        //then go through all its multiples and make their values false.
	    	//if a value is already false then skip it
	        if(primes[i]) {
	            for (int j=2;i*j<primes.length;j++) {
	                primes[i*j]=false;
	            }
	            Thread.yield();   //be a good thread
	        }
	    }
	    logger.log(Level.FINEST, "PrimeSieve("+getSize()+") sieving done.");
		
	}
	
	public String printPrimes() {
		String threadName = Thread.currentThread().getName();  //get the Thread that is running me!
		StringBuilder sb = new StringBuilder();   //local variables are Thread safe (stored on the stack) 
		
		logger.log(Level.FINEST, threadName+" printing Primes...");
		
		for (int i=2;i<primes.length;i++) {
			 if(primes[i]) {
				 if(sb.length()>0){
					 sb.append(","); 
					 sb.append(i);
				 } else {
					 sb.append(i);
				 }
				 Thread.yield();
			 }
		}
		logger.log(Level.FINEST, threadName+" finished printing Primes.");
		
		//object properties are NOT Thread safe they are stored on the (shared) Heap
		return sb.toString();
	}
	
	public int getSize() {
		return size;
	}
	protected void setSize(int size) {
		this.size = size;
	}

	public boolean[] getPrimes() {
		return primes;
	}

	public void setPrimes(boolean[] primes) {
		this.primes = primes;
	}
	
}

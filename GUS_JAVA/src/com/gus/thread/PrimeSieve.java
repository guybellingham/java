package com.gus.thread;
import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class PrimeSieve implements Runnable {
	
	private static final Logger logger = LogManager.getLogger(PrimeSieve.class);
	
	public PrimeSieve(int size) {
		setSize(size);
	}
	
	private int size;
	private boolean[] primes;
	
	@Override
	public void run() {
		long startTime = 0L, elapsedTime = 0L;
        if(logger.isDebugEnabled()) {
        	startTime = System.nanoTime();
			logger.debug("PrimeSieve("+getSize()+") starting...");
		}

        setPrimes(new boolean[getSize()]);
        
		fillSieve();
		
		logger.info(printPrimes());
		
		if(logger.isDebugEnabled()) {
        	elapsedTime = System.nanoTime() - startTime;
        	logger.debug("PrimeSieve finished elapsed=" + (elapsedTime / 1000000.0) + " msec");
        }
	}
	
	//set up the primesieve
	public void fillSieve() {
	    Arrays.fill(primes,true);        // assume all integers are prime!
	    primes[0]=primes[1]=false;       // we know 0 and 1 are not prime.
	    
	    if(logger.isDebugEnabled()) {
			logger.debug("PrimeSieve("+getSize()+") sieving...");
		}
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
	    if(logger.isDebugEnabled()) {
			logger.debug("PrimeSieve("+getSize()+") sieving done.");
		}
	}
	
	public String printPrimes() {
		String threadName = Thread.currentThread().getName();  //get the Thread that is running me!
		StringBuilder sb = new StringBuilder();   //local variables are Thread safe (stored on the stack) 
		
		logger.debug(threadName+" printing Primes...");
		
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
		logger.debug(threadName+" finished printing Primes.");
		
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

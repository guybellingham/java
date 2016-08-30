package com.gus.thread;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimitedConsolePrinter implements Runnable {
	
	boolean finished = false; 
	char character = 'x';
	long sleepDuration = 100;  	//milliseconds
//	long sleepDuration = new Random().nextInt(100);  	//milliseconds
	TokenBucket bucket = TokenBucket.getInstance();   	//thread safe
	RateLimiter limiter = RateLimiter.create(15);  		//@Beta 15 per second 
	long timeToAcquire = 0;
	
	public RateLimitedConsolePrinter(char character) {
		setCharacter(character);
		//For testing use 15 per second
		bucket.rate = 15;
		bucket.timespan = 1000;
	}
	
	@Override
	public void run() {
		//Thread current = Thread.currentThread();
		long start =0, stop = 0;
		boolean rc = false;
		
        while (!finished) {
        	start = System.nanoTime();
        	rc = limiter.tryAcquire();
        	rc = bucket.get();
//        	stop = System.nanoTime();
        	timeToAcquire += stop - start;

        	if(rc) {
        		System.out.print(character);
        	} else {
        		System.out.print('z');
				try {
					Thread.sleep(sleepDuration);
				} catch (InterruptedException e) {
					//ignore
				}
        	}
		}

	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("rateLimitedPrinter:{");
		sb.append("char:\"").append(getCharacter()).append("\"");
//		sb.append(",").append(bucket);
		sb.append(",").append(limiter);
		sb.append(",").append("timeToAcquire:").append(timeToAcquire);
		sb.append(",").append("finished:").append(isFinished()).append("}");
		return sb.toString();
	}
}

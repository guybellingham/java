package com.gus.thread;

public class RateLimitedConsolePrinter implements Runnable {
	
	boolean finished = false; 
	char character = 'x';
	long sleepDuration = 200;  		//milliseconds
	TokenBucket bucket = TokenBucket.getInstance();   //thread safe
	
	public RateLimitedConsolePrinter(char character) {
		setCharacter(character);
		//For testing use 5 per second
		bucket.rate = 5;
		bucket.timespan = 1000;
	}
	
	@Override
	public void run() {
		//Thread current = Thread.currentThread();
		
        while (!finished) {
        	if(bucket.get()) { 
        		System.out.print(character);
        	} else {
        		//System.out.print("\n Thread "+current.getName()+" waiting");
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
		sb.append(",").append(bucket);
		sb.append(",").append("finished:").append(isFinished()).append("}");
		return sb.toString();
	}
}

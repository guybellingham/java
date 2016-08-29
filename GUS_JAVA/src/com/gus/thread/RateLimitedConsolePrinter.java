package com.gus.thread;

public class RateLimitedConsolePrinter implements Runnable {
	
	boolean finished = false; 
	char character = 'x';
	TokenBucket bucket = TokenBucket.getInstance();   //thread safe
	
	public RateLimitedConsolePrinter(char character) {
		setCharacter(character);
	}
	
	@Override
	public void run() {
		Thread current = Thread.currentThread();
		
        while (!finished) {
        	if(bucket.getToken()) {
        		System.out.print(character);
        	}
        	//sleep for 0 - 200 msec
        	try {
        		long millis = Math.round(Math.random() * 200);
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
		sb.append("char:\"").append(getCharacter()).append("\",");
		sb.append(bucket);
		sb.append("finished:").append(isFinished()).append("}");
		return sb.toString();
	}
}

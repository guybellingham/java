package com.gus.threads;

public class CountingRunnable implements Runnable {
	private long count = 0, max = 30;
	
	public CountingRunnable() {
		
	}
	public CountingRunnable(long max) {
		setMax(max);
	}
	
	@Override
	public void run() {
		do {
		System.out.print(count++);
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {
			if(Thread.interrupted()) {
				throw new Error("I was interrupted! e="+e);
			}
		}
		} while(count < 61);
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public long getMax() {
		return max;
	}
	public void setMax(long max) {
		this.max = max;
	}

}

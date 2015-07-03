package com.gus.thread;

public class CountingRunnable implements Runnable {
	private long count = 0, max = 30;
	private boolean finished = false;
	
	public CountingRunnable() {
		
	}
	public CountingRunnable(long max) {
		setMax(max);
	}
	
	@Override
	public void run() {
		do {
			System.out.print(count++);
			System.out.print(',');
			try {
				Thread.sleep(500);
			} catch(InterruptedException e) {
				if(Thread.interrupted()) {
					throw new Error("I was interrupted! e="+e);
				}
			}
		} while(!isFinished() && count<max);
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
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

}

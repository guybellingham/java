package com.gus.thread;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

	private MonitorObject monitor;
	private boolean finished = false; 
	private BlockingQueue<Integer> queue; 
	
	public Consumer(MonitorObject monitor) {
		this.monitor = monitor;
	}
	public Consumer(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}
	
	@Override
	public void run() {
		while(!isFinished()) {
			try {
				 
				Integer counter = queue.take();
				System.out.println(Thread.currentThread().getName()+" just took counter="+counter+" from queue size="+queue.size());
				Thread.sleep(counter * 1000);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void oldRun() {
		// 
		System.out.println("Consumer waiting on shared resource...");
		monitor.doWait();
		System.out.println("Consumer consuming shared object...");
		
		Object obj = monitor.getSharedObject();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("sharedObject="+obj);
		
		System.out.println("Consumer done...");
	}

	public boolean isFinished() {
		return finished;
	}

	protected void setFinished(boolean finished) {
		this.finished = finished;
	}
}

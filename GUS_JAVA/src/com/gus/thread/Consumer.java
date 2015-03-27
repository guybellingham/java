package com.gus.thread;

public class Consumer implements Runnable {

	private MonitorObject monitor;
	
	public Consumer(MonitorObject monitor) {
		this.monitor = monitor;
	}

	@Override
	public void run() {
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
}

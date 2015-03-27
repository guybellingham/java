package com.gus.thread;

public class Producer implements Runnable {

	private MonitorObject monitor;
	
	public Producer(MonitorObject monitor) {
		this.monitor = monitor;
	}

	@Override
	public void run() {
		// 
		System.out.println("Producer producing a shared resource...please wait");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		monitor.setSharedObject("Hello from the Producer "+this);
		System.out.println("Producer sending signal to Consumer...");
		monitor.doNotify();
		System.out.println("Producer done.");
	}
}

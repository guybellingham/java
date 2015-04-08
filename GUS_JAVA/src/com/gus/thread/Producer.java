package com.gus.thread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

	private MonitorObject monitor;
	private boolean finished = false; 
	private BlockingQueue<Integer> queue; 
	
	public Producer(MonitorObject monitor) {
		this.monitor = monitor;
	}
	public Producer(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}
	@Override
	public void run() {
		while(!isFinished()) {
			Integer counter = new Random().nextInt(10);
			try {
				 
				queue.put(counter);
				System.out.println("Producer just put counter="+counter+" onto queue size="+queue.size());
				Thread.sleep(1000);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void oldRun() {
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
	public boolean isFinished() {
		return finished;
	}
	protected void setFinished(boolean finished) {
		this.finished = finished;
	}
	
}

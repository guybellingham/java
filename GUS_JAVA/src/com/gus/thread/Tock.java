package com.gus.thread;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tick will wait for Tock but not the other way around!
 * @author Guy
 *
 */
public class Tock implements Runnable {
	
	private static final Logger logger = Logger.getLogger("com.gus.thread");
	
	public Tock(Semaphore semaphore) {
		setLockObject(semaphore);
	}
	private boolean finished; 
	
	private Semaphore semaphore;
	
	@Override
	public void run() {
		Thread current = Thread.currentThread();
		
		logger.log(Level.FINEST, current.getName()+" running Tock starting...");
		

        while (!finished) {
        	semaphore.release();  
        	//print
    		System.out.print("tock ");
    		try {
				Thread.sleep(1000);    //Tock must wait longer than Tick
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
        
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Semaphore getLockObject() {
		return semaphore;
	}

	public void setLockObject(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

}

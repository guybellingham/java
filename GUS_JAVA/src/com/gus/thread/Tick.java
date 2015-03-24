package com.gus.thread;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Tick implements Runnable {
	
	private static final Logger logger = LogManager.getLogger(Tick.class);
	
	public Tick(LockObject lockObject) {
		setLockObject(lockObject);
	}
	private boolean finished; 
	
	private LockObject lockObject;
	
	@Override
	public void run() {
		Thread current = Thread.currentThread();
		
        if(logger.isDebugEnabled()) {
			logger.debug(current.getName()+" running Tick starting...");
		}

        while (!finished) {
        	//take the lock
        	synchronized(lockObject) {
        		while(lockObject.isWait()){
        			try {
						lockObject.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
        		}
        		//print
        		System.out.print("tick ");
        		try {
    				Thread.sleep(500);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
        		lockObject.setWait(true);  	//make me wait my turn again
        		lockObject.notify();		//notify other Threads who may be waiting
        	}
        	
		}
        
        
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public LockObject getLockObject() {
		return lockObject;
	}

	public void setLockObject(LockObject lockObject) {
		this.lockObject = lockObject;
	}

}

package com.gus.thread;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LockingConsolePrinter implements Runnable {
	
	private static final Logger logger = LogManager.getLogger(LockingConsolePrinter.class);
	
	public LockingConsolePrinter(char character, LockObject lockObject) {
		setCharacter(character);
		setLockObject(lockObject);
	}
	private boolean finished; 
	private char character;
	
	private LockObject lockObject;
	
	@Override
	public void run() {
		Thread current = Thread.currentThread();
		
        if(logger.isDebugEnabled()) {
			logger.debug(current.getName()+" running LockingConsolePrinter("+getCharacter()+") starting...");
		}

        while (!finished) {
        	//take the lock
        	synchronized(lockObject) {
        		//print
        		System.out.print(getCharacter());
        	}
        	//yield and sleep
        	Thread.yield();
        	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        
        System.out.print('\n');
		
		if(logger.isDebugEnabled()) {
        	logger.debug("LockingConsolePrinter("+getCharacter()+") finished.");
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

	public LockObject getLockObject() {
		return lockObject;
	}

	public void setLockObject(LockObject lockObject) {
		this.lockObject = lockObject;
	}

}

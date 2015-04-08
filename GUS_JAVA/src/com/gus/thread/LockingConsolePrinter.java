package com.gus.thread;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LockingConsolePrinter implements Runnable {
	
	private static final Logger logger = LogManager.getLogger(LockingConsolePrinter.class);
	
	public LockingConsolePrinter(char character, SimpleLock lock) {
		setCharacter(character);
		setLock(lock);
	}
	private boolean finished; 
	private char character;
	
	private SimpleLock lock;
	
	@Override
	public void run() {
		Thread current = Thread.currentThread();
		
        if(logger.isDebugEnabled()) {
			logger.debug(current.getName()+" running LockingConsolePrinter("+getCharacter()+") starting...");
		}

        while (!finished) {
        	//take the lock
        	try {
        		lock.lock();
        		//print
        		System.out.print(getCharacter());
        		
        	} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
        		lock.unlock();    //ALWAYS unlock
        	}
        	//sleep
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

	public SimpleLock getLock() {
		return lock;
	}

	public void setLock(SimpleLock lock) {
		this.lock = lock;
	}

}

package com.gus.thread;
/**
 * Usage: This lock is not reentrant, a Thread must only call lock() once and always call unlock()
 * <pre> 
 * try {
 *    lock.lock(); 
 *    //do something critical 
 * } finally {
 *    lock.unlock();
 * }
 * </pre>
 * @author Guy
 * @see java.util.concurrent.locks
 */
public class SimpleLock {

	/**
	 * represents the 'state' of this lock - it starts out as unlocked
	 */
	private boolean locked = false; 
	
	public synchronized void lock() throws InterruptedException {
		//spin lock - a Thread executing lock() will wait here
		//until the current 'owner' of the lock calls unlock()
		while(locked) {
			wait();
		}
		locked=true;
	}
	/**
	 * A thread that has previously 'escaped from' the lock() method can then call unlock()
	 */
	public synchronized void unlock() {
		locked=false;
		notify();   
	}
}

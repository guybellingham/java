package com.gus.thread;
/**
 * @see java.util.concurrent.Semaphore
 * @author Guy
 *
 */
public class Semaphore {

	private boolean wait;

	public synchronized boolean isWait() {
		return wait;
	}

	public synchronized void setWait(boolean wait) {
		this.wait = wait;
	}
	/**
	 * TODO - synchronized(this) is reentrant so the same Thread can take() me twice!
	 */
	public synchronized void take() {
		while(!isWait()){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		setWait(false);  	//make me wait my turn again
		this.notify();		//notify other Threads who may be waiting on me
	}
	
	public synchronized void release() {
		while(isWait()){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		setWait(true);  	//make me wait my turn again
		this.notify();		//notify other Threads who may be waiting on me
	}
	/**
	 * Even when shared between 2 threads a ThreadLocal value is only visible 
	 * to it's 'owning' thread and cannot be seen by other threads. 
	 */
	private ThreadLocal<String> message = new ThreadLocal<String>() {
	    @Override protected String initialValue() {
	        return "This is the initial value";
	    }
	};
	/**
	 * Each Thread is guaranteed to get it's own value. 
	 * @return
	 */
	public String getMessage() {
		return message.get();
	}
	/**
	 * Without over-writing the value of another thread.
	 * @param newMessage
	 */
	public void setMessage(String newMessage) {
		this.message.set(newMessage);
	}
}

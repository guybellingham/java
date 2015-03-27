package com.gus.thread;

public class LockObject {

	private boolean wait;

	public synchronized boolean isWait() {
		return wait;
	}

	public synchronized void setWait(boolean wait) {
		this.wait = wait;
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

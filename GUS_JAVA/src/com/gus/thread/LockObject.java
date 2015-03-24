package com.gus.thread;

public class LockObject {

	private boolean wait;

	public synchronized boolean isWait() {
		return wait;
	}

	public synchronized void setWait(boolean wait) {
		this.wait = wait;
	}    
		
}

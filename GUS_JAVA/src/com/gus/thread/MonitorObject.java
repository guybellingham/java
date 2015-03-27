package com.gus.thread;
/**
 * Java has a builtin wait mechanism that enable threads to become inactive while waiting for signals. 
 * The class java.lang.Object defines three methods, wait(), notify(), and notifyAll(), to facilitate this
 * A thread that calls {{@link #doWait()} on this object becomes inactive until another thread calls {{@link #doNotify()} on this object.
 *  
 * @author Gus
 *
 */
public class MonitorObject {

	/**
	 * One Thread (Producer) can setup this reference to a (shared) Object on the Heap. 
	 * The 'volatile' keyword here guarantees that data setup by one thread is visible to another (Consumer) 
	 * (ensuring any changes in CPU cache are flushed to main memory before another CPU reads it).
	 */
	private volatile Object sharedObject; 
	
	//In order not to 'miss' signals from Threads due to timing issues or interruptions - a flag is used
	private boolean signal = false;

	/**
	 * In order to call either doWait() or doNotify() the calling thread must first obtain the lock on this object (to prevent race conditions).
	 */
	public synchronized void doWait() {
		while(!signal) {
			try {
				this.wait();
				//Once a thread calls wait() it releases the lock it holds on the monitor object. 
				//This allows other threads to call wait() or notify() on the monitor too
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Once a thread is awakened it cannot exit the wait() call until the thread calling notify() has left its synchronized block
		signal = false;  //make me wait again 
		
	}
	public synchronized void doNotify() {		
		signal = true;    //send the signal
		this.notify();    //Don't use notifyAll unless you have multiple threads!
	}
	
	public Object getSharedObject() {
		return sharedObject;
	}
	public void setSharedObject(Object sharedObject) {
		this.sharedObject = sharedObject;
	}
}

package com.gus.thread;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsolePrinter implements Runnable {
	
	private static final Logger logger = Logger.getLogger("com.gus.thread");
	
	public ConsolePrinter(char character) {
		setCharacter(character);
	}
	private boolean finished; 
	private char character;
	private static StringBuffer sb = new StringBuffer();
	
	@Override
	public void run() {
		Thread current = Thread.currentThread();
		
		logger.log(Level.FINEST, current.getName()+" running ConsolePrinter("+getCharacter()+") starting...");
		

        while (!finished) {
        	synchronized(System.out) {
        		System.out.print(getCharacter());
        	}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        System.out.print('\n');
		
        logger.log(Level.FINEST, "ConsolePrinter("+getCharacter()+") finished.");
        
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
	

	
}

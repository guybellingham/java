package com.gus.thread;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ConsolePrinter implements Runnable {
	
	private static final Logger logger = LogManager.getLogger(ConsolePrinter.class);
	
	public ConsolePrinter(char character) {
		setCharacter(character);
	}
	private boolean finished; 
	private char character;
	private static StringBuffer sb = new StringBuffer();
	
	@Override
	public void run() {
		Thread current = Thread.currentThread();
		
        if(logger.isDebugEnabled()) {
			logger.debug(current.getName()+" running ConsolePrinter("+getCharacter()+") starting...");
		}

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
		
		if(logger.isDebugEnabled()) {
        	logger.debug("ConsolePrinter("+getCharacter()+") finished.");
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
	

	
}

package com.gus.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
/**
 * 
 * The Java 'volatile' keyword guarantees visibility of changes to variables across threads. 
 * That is once one thread has made a change to the variable it is 'flushed' from CPU caches 
 * and written back out to the Main Memory so that other threads can 'read' the new value. 
 * However with 2 threads both reading and writing to a shared variable, volatile is not enough, 
 * the read and write methods must also be synchronized to prevent 'race' conditions.    
 * @author Gus
 *
 */
public class ThreadRunner {
	
	private static final Logger logger = LogManager.getLogger(ThreadRunner.class);
	private static ThreadRunner app;
	
	public static void main(String[] args) {
		org.apache.log4j.BasicConfigurator.configure();
		
		app = new ThreadRunner();
		UncaughtExceptionHandler eh = new UncaughtExceptionHandler() {		
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				logger.error("Thread "+t+" Failed!",e);
			}
		};
		Thread.setDefaultUncaughtExceptionHandler(eh);
		
//		startCountingPrinters();
		
//		startConsolePrinters(); 
		
//		startWaitingConsolePrinters();
		
//		startTickTockPrinters();
		
//		startProducerAndConsumer();
		
		startRateLimitedPrinters();
	}
	
	/**
	 * Something 'funny' happens when 2 Threads share the same Runnable?  
	 */
	public static void startCountingPrinters() {
		CountingRunnable run1 = new CountingRunnable();

		Thread thread1 = new Thread(run1,"Count1Thread");
		//thread1.setPriority (Thread.NORM_PRIORITY + 1); 
		Thread thread2 = new Thread(run1,"Count2Thread");
		logger.debug("Starting thread Count1");
		thread1.start();
		logger.debug("Starting thread Count2");
		thread2.start();
		try
	      {
	          Thread.sleep (10000);
	      }
	      catch (InterruptedException e)
	      {
	      }
		run1.setFinished (true);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Even when Threads don't share the same Runnable there is 
	 * no guarantee about the order in which they execute!
	 */
	public static void startConsolePrinters() {
		ConsolePrinter printer1 = new ConsolePrinter('.');
		ConsolePrinter printer2 = new ConsolePrinter('+');
		Thread thread1 = new Thread(printer1,"Printer1Thread");
		//thread1.setPriority (Thread.NORM_PRIORITY + 1); 
		Thread thread2 = new Thread(printer2,"Printer2Thread");
		logger.debug("Starting thread Printer1");
		thread1.start();
		logger.debug("Starting thread Printer2");
		thread2.start();
		try
	      {
	          Thread.sleep (10000);
	      }
	      catch (InterruptedException e)
	      {
	      }
		printer1.setFinished (true);
		printer2.setFinished (true);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Even when 2 Threads use the same 'lock' things can still get out of order. 
	 * The lock only guarantees that the 2 Threads won't write at the same time. 
	 */
	public static void startWaitingConsolePrinters() {
		SimpleLock lock = new SimpleLock();
		LockingConsolePrinter printer1 = new LockingConsolePrinter('<',lock);
		LockingConsolePrinter printer2 = new LockingConsolePrinter('>',lock);   
		Thread thread1 = new Thread(printer1,"Locking1Thread");
		Thread thread2 = new Thread(printer2,"Locking2Thread");
		logger.debug("Starting thread Locking1Thread");
		thread1.start();
		logger.debug("Starting thread Locking2Thread");
		thread2.start();
		try
	      {
	          Thread.sleep (10000);
	      }
	      catch (InterruptedException e)
	      {
	      }
		printer1.setFinished (true);
		printer2.setFinished (true);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void startTickTockPrinters() {
		Semaphore lock = new Semaphore();
		Tick tick = new Tick(lock);
		Tock tock = new Tock(lock);
		Thread thread1 = new Thread(tick,"TickThread");
		Thread thread2 = new Thread(tock,"TockThread");
		logger.debug("Starting thread TickThread");
		thread1.start();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.debug("Starting thread TockThread");
		thread2.start();
		try
	      {
	          Thread.sleep (10000);
	      }
	      catch (InterruptedException e)
	      {
	      }
		tick.setFinished (true);
		tock.setFinished (true);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void startRateLimitedPrinters() {
		RateLimitedConsolePrinter printer1 = new RateLimitedConsolePrinter('.');
		RateLimitedConsolePrinter printer2 = new RateLimitedConsolePrinter('-');
		RateLimitedConsolePrinter printer3 = new RateLimitedConsolePrinter('|');
		
		Thread thread1 = new Thread(printer1,"DotThread");
		Thread thread2 = new Thread(printer2,"DashThread");
		Thread thread3 = new Thread(printer3,"SlashThread");
		logger.debug("Starting 3 RateLimitedPrinters:");
		thread1.start();
		thread2.start();
		thread3.start();
		
		try
	      {
	          Thread.sleep (10000);
	      }
	      catch (InterruptedException e)
	      {
	      }
		printer1.setFinished(true);
		printer2.setFinished(true);
		printer3.setFinished(true);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void startProducerAndConsumer() {
//		MonitorObject monitor = new MonitorObject();
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(5);
		Producer producer = new Producer(queue);
		Consumer consumer = new Consumer(queue);
		Thread thread1 = new Thread(producer,"ProducerThread");
		Thread thread2 = new Thread(consumer,"ConsumerThread1");
		Thread thread3 = new Thread(consumer,"ConsumerThread2");
		logger.debug("Starting thread ProducerThread");
		thread1.start();
		logger.debug("Starting thread ConsumerThread1");
		thread2.start();
		logger.debug("Starting thread ConsumerThread2");
		thread3.start();
		try
	      {
	          Thread.sleep (30000);
	      }
	      catch (InterruptedException e)
	      {
	      }
		producer.setFinished(true);
		consumer.setFinished(true);
		logger.debug("Finished!"); 
	}
	
	public static void startPrimeSievePrinters() {

		//You don't want to have 2 Threads trying to run the same instance of PrimeSieve 
		//because a prime sieve has a 'mutable' instance property that is not threadsafe and results will be unpredictable!
//		PrimeSieve sieve = new PrimeSieve(2000000);
//		Thread thread1 = new Thread(sieve,"Runner1");
//		Thread thread2 = new Thread(sieve,"Runner2");	
		
		//Instead each Thread should have it's own sieve like this!
		Thread thread1 = new Thread(new PrimeSieve(2000000),"Primes1Thread");
		Thread thread2 = new Thread(new PrimeSieve(1000000),"Primes2Thread");
		logger.debug("Starting thread Primes1Thread");
		thread1.start();
		
		synchronized(app) {
			//Do a random wait to simulate waiting for some resource
			try {
	        	Random rand = new Random();
	        	int millis = rand.nextInt(10); 
	        	logger.debug("main Thread waiting for "+millis+" msec");
	        	app.wait(millis);
	        	logger.debug("Starting thread Primes2Thread");
				thread2.start();
			} catch (InterruptedException e) {
				logger.error("main Thread wait was interrupted!", e);
			}
			
		}
	}
	
	public static void startFibonacciPrinters() {
		//FibonacciNumbers on the other hand uses only local (method scoped) variables for calculations and 
		//all instance variables are immutable, therefore it IS Thread safe 
		FibonacciNumbers fibonacciNumbers = new FibonacciNumbers(500); 
		Thread thread3 = new Thread(fibonacciNumbers,"Fibonacci1");
		Thread thread4 = new Thread(fibonacciNumbers,"Fibonacci2");
		logger.debug("Starting thread Fibonacci1");
		thread3.start();
		logger.debug("Starting thread Fibonacci2");
		thread4.start();
	}
}

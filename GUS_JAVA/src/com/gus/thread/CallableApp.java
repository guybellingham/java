package com.gus.thread;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * <p>CompletableFuture class implements the Future interface, so you can use it as a Future implementation, but with additional completion logic.
 * The best part of the CompletableFuture API is the ability to combine CompletableFuture instances in a chain of computation steps.
 * <pre>
 * CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
 *    .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));
 * </pre>
 * The <code>thenCompose</code> method together with <code>thenApply</code> implement basic building blocks of the monadic pattern. 
 * <li> The <code>thenApply</code> method is useful when we want to <em>transform</em> the result of the previous CompletableFuture call. 
 * This is analogous to the <code>map()</code> method of reactive Streams/Observables.
 * <li> The <code>thenCompose</code> method however is used to <em>chain</em> CompletableFutures together in a way analogous to the <code>flatMap</code>
 * method of reactive Streams/Observables (this allows us to unwind/prevent nesting of Streams Stream<Stream<String>>).  
 * <p>The most generic way to process the result of a computation is to feed it to a function. 
 * The <code>thenApply()</code> method does exactly that: accepts a Function instance, uses it to process the result 
 * and returns a Future that holds a value returned by that function.
 * <p>If you don't need to return a future the <code>thenAccept()</code> method receives a Consumer and passes it the result of the computation
 * and returns the <code>Void</code> type.
 * <p>If you neither need the value of the computation nor want to return some value at the end of the chain, 
 * then you can pass a Runnable lambda to the <code>thenRun()</code> method.  
 * <p>When we need to execute multiple Futures in parallel, we usually want to wait for all of them to execute and then process their combined results.
 * The <code>CompletableFuture.allOf(cf1, cf2, ...)</code> static method allows to wait for completion of all of the Futures provided.
 * The return type of the CompletableFuture.allOf() is a CompletableFuture<Void>. 
 * The limitation of this method is that it does not return the combined results of all Futures. 
 * Instead you have to manually get results from Futures. Fortunately, <code>CompletableFuture.join()</code> method and Java 8 Streams API makes it simple:
 * <pre>
 * String combined = Stream.of(cf1, cf2, future3)
 *   .map(CompletableFuture::join)
 *   .collect(Collectors.joining(" "));
 * </pre>
 * <p>Most methods of the fluent API in CompletableFuture class have two additional variants with the <code>Async</code> postfix. 
 * These methods are usually intended for running a corresponding step of execution in another thread.
 * <p>Instead of catching an exception in a syntactic block, the CompletableFuture class allows you to handle it 
 * in a special <code>handle(data, error)</code> method.
 * @author Gus
 *
 */
public class CallableApp {
	
	private static final Logger logger = Logger.getLogger("com.gus.thread");
	private static Random RANDOM = new Random(); 
	
	public static void main(String[] args) {
		logger.info("CompletableFuture creating cachedThreadPool...");
		ExecutorService cachedThreads = Executors.newCachedThreadPool();
		
		try {
			Future<String> fu1 = calculateAsync(randomInt(4000));
			//Block for the result 
			logger.info("Future1.get()...");
			String result1 = fu1.get();
			logger.info("Future1 returned "+result1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e) {
			logger.severe("fu1 execution FAILED:"+e.getCause());
		}
		
		System.out.println(System.currentTimeMillis()+"ms");
		//The Supplier interface is a generic functional interface with a single method that has no arguments and returns a value of a parameterized type
		CompletableFuture<String> cf1 = CompletableFuture.<String>supplyAsync(() -> {
			int delay = randomInt(3000);
			logger.info("cf1 waiting for "+delay+"ms...");
			try {
				Thread.sleep(delay);
			} catch (InterruptedException ignored) {
				//ignore
			}
			return "This is CompletableFuture1 cf1";
		});
		//The thenAccept() method receives a Consumer (lambda) and passes it the result of the computation. 
		//The final future.get() call returns an instance of the Void type.
		cf1.thenAccept(str -> logger.info(str+".thenAccept()"));
		System.out.println(System.currentTimeMillis()+"ms");
				
		try {
			logger.info("Blocking on cf1.get()");
			logger.info("cf1.get()="+cf1.get());
			System.out.println(System.currentTimeMillis()+"ms");
		} catch (InterruptedException e) {			
			e.printStackTrace();
		} catch (ExecutionException e) {
			logger.severe("cf1 execution FAILED:"+e.getCause());
		}
		
		cf1 = CompletableFuture.<String>supplyAsync(() -> {
			int delay = randomInt(3000);
			logger.info("cf1 waiting for "+delay+"ms...");
			try {
				Thread.sleep(delay);
			} catch (InterruptedException ignored) {
				//ignore
			}
			return "This is CompletableFuture1";
		});
		CompletableFuture<String> cf2 = CompletableFuture.<String>supplyAsync(() -> {
			int delay = randomInt(3000);
			logger.info("cf2 waiting for "+delay+"ms...");
			try {
				Thread.sleep(delay);
			} catch (InterruptedException ignored) {
				//ignore
			}
			return "and this is CompletableFuture2!";
		});
		//CompletableFuture.allOf(cf1, cf2).join();
		String combined = Stream.of(cf1,cf2)
				.map(CompletableFuture::join)
				.collect(Collectors.joining(" "));
		logger.info("cf1,cf2 joined: "+combined);
	}

	/**
	 * You can create an instance <code>new CompletableFuture<>()</code> with a no-arg constructor to represent some future result, 
	 * hand it out to the consumers and complete it at some time in the future using the <code>complete()</code>  method. 
	 * The consumers may use the <code>get()</code> method to block the current thread until this result will be provided.
	 * @param delay
	 * @return a (Completable)Future immediately
	 * @throws InterruptedException
	 */
	public static Future<String> calculateAsync(int delay) throws InterruptedException {
	    CompletableFuture<String> completableFuture 
	      = new CompletableFuture<>();
	 
	    Executors.newCachedThreadPool().submit(() -> {
	    	//This code runs when the consumer 'gets' the future value
	    	logger.info("cachedThread executing with "+delay+" delay");
	        Thread.sleep(delay);
	        completableFuture.complete("Hello World!"); 
	        //If we were to .cancel() this Future it would throw a CancellationException (ExecutionException)
	        return null;
	    });
	 
	    return completableFuture;
	}
	
	public static int randomInt(int max) {
		return RANDOM.nextInt(max);
	}
}

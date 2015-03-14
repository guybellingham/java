package com.gus.pattern.command;

/**
 * <p>A Command always has a <code>receiver</code> type upon which it acts when the {@link #execute()} method is called. 
 * Some commands also provide an {@link #undo()} method which reverses the effects of the <code>execute()</code>. </p>
 * <h3>Why use the 'command' pattern?</h3>
 * <ol>   
 * <li> Using Commands decouples the 'commander' (aka requester) from the 'commanded' (aka receiver) objects. 
 * It's almost like a 'callback' which Java does not have, because it has no 'pointers' 
 * to methods which it can pass around. </li>   
 * <li> You can keep a List (history) of commands around. </li> 
 * <li> You can provide an {@link #undo()} in case of failure somewhere along the line.</li>
 * </ol>
 * @author Gus
 * @param <R> the 'Receiver' type.
 */
public interface Command<R> {
	
	public void execute();

	public void undo();
	
	public R getReceiver();
	public void setReceiver(R receiver);
}

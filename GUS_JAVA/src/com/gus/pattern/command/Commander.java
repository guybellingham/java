package com.gus.pattern.command;

import java.util.ArrayList;
import java.util.List;
/**
 * Abstract class for executing a sequence of {@link Command}s on a type <code>T</code>.
 * @author Gus
 *
 * @param <T> - the type of object the commands operate on.
 */
public abstract class Commander<T> {

	private List<Command<T>> commands;  //sequence of Commands on type T
	
	public Commander(Command<T> command) {
		List<Command<T>> commands = new ArrayList<Command<T>>();
		commands.add(command);
		setCommands(commands);
	}
	public Commander(List<Command<T>> commands) {
		setCommands(commands);
	}
	
	public abstract void executeCommands();
	
	protected List<Command<T>> getCommands() {
		return commands;
	}

	protected void setCommands(List<Command<T>> commands) {
		this.commands = commands;
	}
	
	protected void addCommand(Command<T> newCommand) {
		this.commands.add(newCommand);
	}
	
	protected void clearCommands() {
		this.commands.clear();
	}

}

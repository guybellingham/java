package com.gus.pattern.command;

import java.util.Iterator;
import java.util.List;

public class LightCommander extends Commander<Light> {
	
	public LightCommander(Command<Light> command) {
		super(command);
	}
	public LightCommander(List<Command<Light>> commands) {
		super(commands);
	}
	
	public void executeCommands() {
		for (Iterator<Command<Light>> iterator = getCommands().iterator(); iterator.hasNext();) {
			Command<Light> command = iterator.next();
			command.execute();
		}
	}

}

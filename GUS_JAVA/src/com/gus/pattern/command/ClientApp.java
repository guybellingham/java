package com.gus.pattern.command;

import java.util.ArrayList;
import java.util.List;

/**
 * App to demonstrate the Command pattern use. 
 * @author Gus
 */
public class ClientApp {

	public static void main(String[] args) {
		ClientApp app = new ClientApp();
		Light outsideLight = new Light("Outside",LightBulbType.cfl,30,120);  //off
		Light hallLight = new Light("Hallway");  //incandescent 60w off
		System.out.println("Command Pattern ClientApp starting with outsideLight="+outsideLight+" hallLight="+hallLight); 
		List<Command<Light>> commands = new ArrayList<Command<Light>>();
		Command<Light> command1 = new LightOnCommand(outsideLight);
		Command<Light> command2 = new LightOffCommand(hallLight); 
		Command<Light> command3 = new ChangeLightBulbCommand(hallLight, new LightBulb(LightBulbType.led,15,120));
		Command<Light> command4 = new LightOnCommand(hallLight);
		commands.add(command1);
		commands.add(command2);
		commands.add(command3);
		commands.add(command4);
		Commander commander = new LightCommander(commands);
		System.out.println("Command Pattern ClientApp calling commander.executeCommands()...");
		commander.executeCommands();
		System.out.println("Command Pattern ClientApp ending with outsideLight="+outsideLight+" hallLight="+hallLight);
		System.out.println("Command Pattern ClientApp calling command1.undo()...");
		command1.undo();
		System.out.println("Command Pattern ClientApp ending with outsideLight="+outsideLight+" switch should be OFF!");
	}

}

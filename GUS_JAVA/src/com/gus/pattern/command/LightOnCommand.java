package com.gus.pattern.command;
/**
 * This command 'has a' <code>receiver</code> that is a {@link Light}. 
 * It 'knows' how to turn a light on ({@link #execute()}) and off ({@link #undo()}.  
 * @author Gus
 *
 */
public class LightOnCommand implements Command<Light> {

	private Light light;
	
	public LightOnCommand(Light light) {
		setLight(light);
	}
	/**
	 * This method will turn the {@link Light.Switch} ON.
	 */
	@Override
	public void execute() {
		getLight().getOnOffSwitch().turnOn();
	}
	/**
	 * This method will turn the {@link Light} OFF again.
	 */
	public void undo() {
		getLight().getOnOffSwitch().turnOff();
	}
	
	public Light getLight() {
		return light;
	}

	public void setLight(Light light) {
		this.light = light;
	}
	
	@Override
	public Light getReceiver() {
		return light;
	}
	@Override
	public void setReceiver(Light light) {
		setLight(light);
	}

}

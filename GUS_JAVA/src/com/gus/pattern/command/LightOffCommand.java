package com.gus.pattern.command;
/**
 * This command 'has a' <code>receiver</code> that is a {@link Light}. 
 * It 'knows' how to turn a light off ({@link #execute()}) and on ({@link #undo()}.  
 * @author Gus
 *
 */
public class LightOffCommand implements Command<Light> {

	private Light light;
	
	public LightOffCommand(Light light) {
		setLight(light);
	}
	/**
	 * This method will turn the {@link Light.Switch} OFF.
	 */
	@Override
	public void execute() {
		getLight().getOnOffSwitch().turnOff();
	}
	/**
	 * This method will turn the {@link Light} ON again.
	 */
	public void undo() {
		getLight().getOnOffSwitch().turnOn();
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

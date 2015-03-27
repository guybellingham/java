package com.gus.pattern.command;

/**
 * This command 'has a' <code>receiver</code> that is a {@link Light}.  
 * It 'knows' how to change the LightBulb in a Light. 
 * @author Gus
 *
 */
public class ChangeLightBulbCommand implements Command<Light> {

	private Light light;
	private LightBulb newLightBulb, oldLightBulb;
	
	/**
	 * @param light  - the Light to change
	 * @param newBulb - the new LightBulb to swap in or out
	 */
	public ChangeLightBulbCommand(Light light, LightBulb newBulb) {
		setLight(light);
		setNewLightBulb(newBulb);
	}
	/**
	 * This command will change the {@link Light.LightBulb}
	 */
	@Override
	public void execute() {
		LightBulb bulb = getLight().changeBulb(getNewLightBulb());
		setOldLightBulb(bulb);
	}
	/**
	 * This method will put the old LightBulb back in the Light. 
	 */
	public void undo() {
		getLight().changeBulb(getOldLightBulb());
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
		this.light = light;
	}
	public LightBulb getNewLightBulb() {
		return newLightBulb;
	}
	public void setNewLightBulb(LightBulb lightBulb) {
		this.newLightBulb = lightBulb;
	}
	public LightBulb getOldLightBulb() {
		return oldLightBulb;
	}
	public void setOldLightBulb(LightBulb oldLightBulb) {
		this.oldLightBulb = oldLightBulb;
	}

}

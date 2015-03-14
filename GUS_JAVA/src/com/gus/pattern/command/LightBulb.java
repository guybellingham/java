package com.gus.pattern.command;

public class LightBulb {
	int watts, voltage; 
	LightBulbType type;
	
	/**
	 * JavaBeans no-arg constructor
	 * Builds an ordinary 'incandescent' 60W / 120 volt bulb. 
	 */
	public LightBulb() { 
		setType(LightBulbType.incandescent);
		setWatts(60);
		setVoltage(120);
	}
	public LightBulb(LightBulbType type,int watts,int voltage) {
		setType(type);
		setWatts(watts);
		setVoltage(voltage);			
	}
	
	public int getWatts() {
		return watts;
	}
	public void setWatts(int watts) {
		this.watts = watts;
	}
	public int getVoltage() {
		return voltage;
	}
	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}
	public LightBulbType getType() {
		return type;
	}
	public void setType(LightBulbType type) {
		this.type = type;
	}
	public String toString() {
		return "lightbulb:{ type:"+getType()+", watts:"+getWatts()+", voltage:"+getVoltage()+"}";
	}
}

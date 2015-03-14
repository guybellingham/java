package com.gus.pattern.command;

/**
 * This is the so called 'Receiver of the Commands. 
 * It does not need to be loosely coupled from all things 'Light'.  
 * It contains a {@link LightBulb} of different {@link LightBulbType}s and a 
 * {@link Switch} which can be 'on' or 'off'.
 * @author Gus
 *
 */
public class Light {

	private String name; 
	private Switch onOffSwitch;  
	private LightBulb bulb;
	
	public Light(String name) {	
		setName(name);
		onOffSwitch = new Switch();
		bulb = new LightBulb();
	}
	public Light(String name,LightBulbType type,int watts,int voltage) {		
		this(name,type,watts,voltage,false);
	}
	public Light(String name,LightBulbType type,int watts,int voltage,boolean on) {		
		setName(name);
		onOffSwitch = new Switch(on);
		bulb = new LightBulb(type,watts,voltage);
	}
	
	public class Switch {
		boolean on; 
		public Switch() {
			//defaults to off
		}
		public Switch(boolean on){
			setOn(on);
		}
		public boolean isOn() {
			return on;
		}
		public void setOn(boolean on) {
			this.on = on;
		}
		public boolean turnOn() {
			if(isOn()) { return false; }
			setOn(true);
			return true;
		}
		public boolean turnOff() {
			if(!isOn()) { return false; }
			setOn(false);
			return true;
		}
		public String toString() {
			return "switch:"+ (isOn()?"ON":"OFF");
		}
	}
	
	public String toString() {
		return getName()+" light:{"+getOnOffSwitch()+","+getBulb()+"}";
	}

	public Switch getOnOffSwitch() {
		return onOffSwitch;
	}

	public void setOnOffSwitch(Switch onOffSwitch) {
		this.onOffSwitch = onOffSwitch;
	}

	public LightBulb getBulb() {
		return bulb;
	}

	public void setBulb(LightBulb bulb) {
		this.bulb = bulb;
	}
	/**
	 * Changes the <code>LightBulb</code> in this Light 
	 * @param newBulb
	 * @return the 'old' LightBulb
	 */
	public LightBulb changeBulb(LightBulb newBulb) {
		LightBulb oldBulb = getBulb();
		setBulb(newBulb);
		return oldBulb;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

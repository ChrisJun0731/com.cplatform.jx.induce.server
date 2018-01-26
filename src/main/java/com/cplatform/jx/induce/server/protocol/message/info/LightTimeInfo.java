package com.cplatform.jx.induce.server.protocol.message.info;


public class LightTimeInfo extends SwitchTimeInfo {

	/**¡¡∂»÷µ 0-255*/
	private int lightValue;

	
    public int getLightValue() {
    	return lightValue;
    }

	
    public void setLightValue(int lightValue) {
    	this.lightValue = lightValue;
    }
	
}

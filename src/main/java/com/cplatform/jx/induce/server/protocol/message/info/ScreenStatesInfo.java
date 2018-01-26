package com.cplatform.jx.induce.server.protocol.message.info;

/**
 * 
 * 设备状态实体类. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月27日 上午10:42:49
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ScreenStatesInfo {

	private String address;
	 /**日期*/
    private String date;
    /**时间*/
    private String time;
    /**门状态 1-打开  2-关闭*/
    private int doorState;
    /**屏体电源 1-供电  2-断电 */
    private int screenPower;
    /**开关屏标记 1-开屏  2-人工关屏 3-温度过高关 4-坏点过多关*/
    private int switchState;
    /**当前温度符号 1-表示正数 2-表示负数 */
    private int symbol;
    /**采集温度*/
    private int value;
    /**采集亮度*/
    private int lightValue;
    /**亮度控制方式  1-自动  2-手动 */
    private int controlLightType;
    /**亮度级别  1-255*/
    private int lightLevel;

	
    
    
    public String getAddress() {
    	return address;
    }


	
    public void setAddress(String address) {
    	this.address = address;
    }


	public String getDate() {
    	return date;
    }

	
    public void setDate(String date) {
    	this.date = date;
    }

	
    public String getTime() {
    	return time;
    }

	
    public void setTime(String time) {
    	this.time = time;
    }

	
    public int getDoorState() {
    	return doorState;
    }

	
    public void setDoorState(int doorState) {
    	this.doorState = doorState;
    }

	
    public int getScreenPower() {
    	return screenPower;
    }

	
    public void setScreenPower(int screenPower) {
    	this.screenPower = screenPower;
    }

	
    public int getSwitchState() {
    	return switchState;
    }

	
    public void setSwitchState(int switchState) {
    	this.switchState = switchState;
    }

	
    public int getSymbol() {
    	return symbol;
    }

	
    public void setSymbol(int symbol) {
    	this.symbol = symbol;
    }

	
    public int getValue() {
    	return value;
    }

	
    public void setValue(int value) {
    	this.value = value;
    }
	
    public int getLightValue() {
    	return lightValue;
    }

	
    public void setLightValue(int lightValue) {
    	this.lightValue = lightValue;
    }

	
    public int getControlLightType() {
    	return controlLightType;
    }

	
    public void setControlLightType(int controlLightType) {
    	this.controlLightType = controlLightType;
    }

	
    public int getLightLevel() {
    	return lightLevel;
    }

	
    public void setLightLevel(int lightLevel) {
    	this.lightLevel = lightLevel;
    }
    
    
    @Override
	public String toString(){
		
    	StringBuffer buf = new StringBuffer(500);
    	buf.append("address=").append(address).append(", ");
		buf.append("date=").append(date).append(", ");
		buf.append("time=").append(time).append(", ");
		buf.append("doorState=").append(doorState).append(", ");
		buf.append("screenPower=").append(screenPower).append(", ");
		buf.append("switchState=").append(switchState).append(", ");
		buf.append("symbol=").append(symbol).append(", ");
		buf.append("value=").append(value).append(", ");
		buf.append("lightValue=").append(lightValue).append(", ");
		buf.append("controlLightType=").append(controlLightType).append(", ");
		buf.append("lightLevel=").append(lightLevel);
    	return buf.toString();
	}
}

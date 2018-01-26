package com.cplatform.jx.induce.server.protocol.message.info;

/**
 * 
 * �豸״̬ʵ����. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��27�� ����10:42:49
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ScreenStatesInfo {

	private String address;
	 /**����*/
    private String date;
    /**ʱ��*/
    private String time;
    /**��״̬ 1-��  2-�ر�*/
    private int doorState;
    /**�����Դ 1-����  2-�ϵ� */
    private int screenPower;
    /**��������� 1-����  2-�˹����� 3-�¶ȹ��߹� 4-��������*/
    private int switchState;
    /**��ǰ�¶ȷ��� 1-��ʾ���� 2-��ʾ���� */
    private int symbol;
    /**�ɼ��¶�*/
    private int value;
    /**�ɼ�����*/
    private int lightValue;
    /**���ȿ��Ʒ�ʽ  1-�Զ�  2-�ֶ� */
    private int controlLightType;
    /**���ȼ���  1-255*/
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

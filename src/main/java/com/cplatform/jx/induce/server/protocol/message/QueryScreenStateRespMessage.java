package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 
 * ��ѯ�豸״̬��Ӧ��Ϣ. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��6�� ����2:29:41
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class QueryScreenStateRespMessage extends AbstractMessage {
static Logger log = Logger.getLogger(QueryScreenStateRespMessage.class);
	
	public QueryScreenStateRespMessage(Header header)
    {
        super(header);
        getHeader().setTotalLength(7+18);
    }
    
    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {

         return null;
        
    }

    /**
     * ���ӿ�
     */
    public int byteToMessage(ByteBuffer buffer)
    {
        if (buffer == null)
		{
			return MESSAGE_ERROR;
		}
		try
		{				 
			byte[] bytes =new byte[2];
			buffer.get(bytes);
			buffer.position(2);
			//��
			int year =ToolKits.byteToShort(bytes);			
			//��
			int month =buffer.get();
			//��
			int day = buffer.get();
			setDate(year+"-"+month+"-"+day);// ����
			//ʱ
			int hours = buffer.get();
			//��
			int min = buffer.get();
			//��
			int sec = buffer.get();
			setTime(hours+":"+min+":"+sec);//ʱ��
			setDoorState(buffer.get());
			setScreenPower(buffer.get());
			setSwitchState(buffer.get());
			setSymbol(buffer.get());
			setValue(buffer.get());
			setParam1(buffer.get());
			setParam2(buffer.get());
			setParam3(buffer.get());
			int light = buffer.get();
			setLightValue(light < 0 ? light+256:light );
			setControlLightType(buffer.get());
			setLightLevel(buffer.get());
		}
		catch (Exception e)
		{
			log.error("���������쳣",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
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
    /**�����ֽ�*/
    private int param1;
    /**�����ֽ�*/
    private int param2;
    /**�����ֽ�*/
    private int param3;
    /**�ɼ�����*/
    private int lightValue;
    /**���ȿ��Ʒ�ʽ  1-�Զ�  2-�ֶ� */
    private int controlLightType;
    /**���ȼ���  1-255*/
    private int lightLevel;

	
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

	
    public int getParam1() {
    	return param1;
    }

	
    public void setParam1(int param1) {
    	this.param1 = param1;
    }

	
    public int getParam2() {
    	return param2;
    }

	
    public void setParam2(int param2) {
    	this.param2 = param2;
    }

	
    public int getParam3() {
    	return param3;
    }

	
    public void setParam3(int param3) {
    	this.param3 = param3;
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
    	buf.append("address=").append(getHeader().getAddress()).append(", ");
		buf.append("date=").append(date).append(", ");
		buf.append("time=").append(time).append(", ");
		buf.append("doorState=").append(doorState).append(", ");
		buf.append("screenPower=").append(screenPower).append(", ");
		buf.append("switchState=").append(switchState).append(", ");
		buf.append("symbol=").append(symbol).append(", ");
		buf.append("value=").append(value).append(", ");
		buf.append("param1=").append(param1).append(", ");
		buf.append("param2=").append(param2).append(", ");
		buf.append("param3=").append(param3).append(", ");
		buf.append("lightValue=").append(lightValue).append(", ");
		buf.append("controlLightType=").append(controlLightType).append(", ");
		buf.append("lightLevel=").append(lightLevel);
    	return buf.toString();
	}

	

    
}

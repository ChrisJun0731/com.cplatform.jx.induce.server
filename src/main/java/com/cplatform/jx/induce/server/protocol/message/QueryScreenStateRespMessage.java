package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 
 * 查询设备状态响应消息. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月6日 下午2:29:41
 * <p>
 * Company: 北京宽连十方数字技术有限公司
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
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {

         return null;
        
    }

    /**
     * 见接口
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
			//年
			int year =ToolKits.byteToShort(bytes);			
			//月
			int month =buffer.get();
			//日
			int day = buffer.get();
			setDate(year+"-"+month+"-"+day);// 日期
			//时
			int hours = buffer.get();
			//分
			int min = buffer.get();
			//秒
			int sec = buffer.get();
			setTime(hours+":"+min+":"+sec);//时间
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
			log.error("解析数据异常",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
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
    /**保留字节*/
    private int param1;
    /**保留字节*/
    private int param2;
    /**保留字节*/
    private int param3;
    /**采集亮度*/
    private int lightValue;
    /**亮度控制方式  1-自动  2-手动 */
    private int controlLightType;
    /**亮度级别  1-255*/
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

package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;


/**
 * 查询设置的点检参数
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月16日 下午5:14:57
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class QueryDetectionPointRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(QueryDetectionPointRespMessage.class);
	
	public QueryDetectionPointRespMessage(Header header)
    {
        super(header);
        getHeader().setTotalLength(7+8);
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
			setLightColor(buffer.get());
			setType(buffer.get());
			setLimit(buffer.get());
			setIsCurrentAdd(buffer.get());
			setRed(buffer.get());
			setGreen(buffer.get());
			setBlue(buffer.get());			
		}
		catch (Exception e)
		{
			log.error("解析数据异常",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
  
	/**显示屏灯色  红色：0x01；绿色：0x02；蓝色：0x04； */
	private int lightColor;
	/**点检类型 开路：0；短路：1*/
	private int type;
	/**点检阈值  四种：值分别为 1,2,3,4，选择一种下发*/
	private int limit;
	/**是否用电流增益 是：1；否：0*/
	private int IsCurrentAdd;
	/**电流增益 增益包括红色、绿色、蓝色三种，各占一个字节，取值范围均为 1-255*/
	private int red;
	/**电流增益 增益包括红色、绿色、蓝色三种，各占一个字节，取值范围均为 1-255*/
	private int green;
	/**电流增益 增益包括红色、绿色、蓝色三种，各占一个字节，取值范围均为 1-255*/
	private int blue;
	

	
    public int getLightColor() {
    	return lightColor;
    }

	
    public void setLightColor(int lightColor) {
    	this.lightColor = lightColor;
    }

	
    public int getType() {
    	return type;
    }

	
    public void setType(int type) {
    	this.type = type;
    }

	
    public int getLimit() {
    	return limit;
    }

	
    public void setLimit(int limit) {
    	this.limit = limit;
    }

    public int getIsCurrentAdd() {
    	return IsCurrentAdd;
    }

	
    public void setIsCurrentAdd(int isCurrentAdd) {
    	IsCurrentAdd = isCurrentAdd;
    }

	public int getRed() {
    	return red;
    }

	
    public void setRed(int red) {
    	this.red = red;
    }

	
    public int getGreen() {
    	return green;
    }

	
    public void setGreen(int green) {
    	this.green = green;
    }

	
    public int getBlue() {
    	return blue;
    }

	
    public void setBlue(int blue) {
    	this.blue = blue;
    }
    
    
    @Override
	public String toString(){
		
    	StringBuffer buf = new StringBuffer(500);
    	buf.append("address=").append(getHeader().getAddress()).append(", ");
		buf.append("lightColor=").append(lightColor).append(", ");
		buf.append("type=").append(type).append(", ");
		buf.append("limit=").append(limit).append(", ");
		buf.append("IsCurrentAdd=").append(IsCurrentAdd).append(", ");
		buf.append("red=").append(red).append(", ");
		buf.append("green=").append(green).append(", ");
		buf.append("blue=").append(blue);
    	return buf.toString();
	}
}

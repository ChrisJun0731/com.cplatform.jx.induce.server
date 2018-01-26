package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;


/**
 * ��ѯ���õĵ�����
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��16�� ����5:14:57
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
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
			log.error("���������쳣",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
  
	/**��ʾ����ɫ  ��ɫ��0x01����ɫ��0x02����ɫ��0x04�� */
	private int lightColor;
	/**������� ��·��0����·��1*/
	private int type;
	/**�����ֵ  ���֣�ֵ�ֱ�Ϊ 1,2,3,4��ѡ��һ���·�*/
	private int limit;
	/**�Ƿ��õ������� �ǣ�1����0*/
	private int IsCurrentAdd;
	/**�������� ���������ɫ����ɫ����ɫ���֣���ռһ���ֽڣ�ȡֵ��Χ��Ϊ 1-255*/
	private int red;
	/**�������� ���������ɫ����ɫ����ɫ���֣���ռһ���ֽڣ�ȡֵ��Χ��Ϊ 1-255*/
	private int green;
	/**�������� ���������ɫ����ɫ����ɫ���֣���ռһ���ֽڣ�ȡֵ��Χ��Ϊ 1-255*/
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

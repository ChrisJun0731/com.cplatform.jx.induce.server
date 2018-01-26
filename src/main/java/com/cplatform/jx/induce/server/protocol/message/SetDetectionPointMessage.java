package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 设置点检参数 
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月16日 下午4:44:44
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetDetectionPointMessage extends AbstractMessage {

	/**显示屏灯色 红色：0x01；绿色：0x02；蓝色：0x04*/
	public static final int SCREEN_COLOR_RED = 0x01;
	/**显示屏灯色 红色：0x01；绿色：0x02；蓝色：0x04*/
	public static final int SCREEN_COLOR_GREEN = 0x02;
	/**显示屏灯色 红色：0x01；绿色：0x02；蓝色：0x04*/
	public static final int SCREEN_COLOR_BLUE = 0x04;
	
	/**点检类型  开路：0；短路：1； */
	public static final int TYPE_OPEN = 0;
	/**点检类型  开路：0；短路：1； */
	public static final int TYPE_SHORT = 1;
	
	/**点检阈值  四种：值分别为 1,2,3,4，选择一种下发*/
	public static final int CURRENT_ONE = 1;
	/**点检阈值  四种：值分别为 1,2,3,4，选择一种下发*/
	public static final int CURRENT_TWO = 2;
	/**点检阈值  四种：值分别为 1,2,3,4，选择一种下发*/
	public static final int CURRENT_THREE = 3;
	/**点检阈值  四种：值分别为 1,2,3,4，选择一种下发*/
	public static final int CURRENT_FOUR = 4;
	/**是否用电流增益 是：1；否：0； */
	public static final int OPEN_CURRENT_ADD =1;
	/**是否用电流增益 是：1；否：0； */
	public static final int CLOSE_CURRENT_ADD =0;
	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public SetDetectionPointMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_DETECTION_POINT_PARAM);
        this.getHeader().setTotalLength(15);//有消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
    	 if (length < 15) // 此消息长度为7个字节
         {
             return null;
         }
    	 this.getHeader().setAddress(getAddress());
         //拷贝头部 除 开始符外
         //转义  0xAA 或 0xCC 或 0xEE
         byte[] headbytes = getHeader().byteMessage(3);
         int len =3+8;
         byte[] conbytes =new byte[len];
         System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);
         conbytes[conbytes.length-1]=(byte)getLightColor();
         conbytes[conbytes.length-2]=(byte)getType();
         conbytes[conbytes.length-3]=(byte)getLimit();
         conbytes[conbytes.length-4]=(byte)getIsCurrentAdd();
         
         conbytes[conbytes.length-5]=(byte)getRed();
         conbytes[conbytes.length-6]=(byte)getGreen();
         conbytes[conbytes.length-7]=(byte)getBlue();
         
         conbytes[conbytes.length-8]=(byte)0;
         
         byte[] ecsbytes= ToolKits.covertChar(conbytes);
         int ecslength = ecsbytes.length;
         //转义后 加上 开始结束符
         byte[] bytes = new byte[ecslength+2];
         bytes[0] =(byte)MESSAGE_BEGIN;
         System.arraycopy(ecsbytes, 0, bytes, 1, ecslength);
         bytes[ecslength+1] =(byte)MESSAGE_END;
         
         //计算CRC16验证码       
         ByteBuffer buffer = ByteBuffer.allocate(ecslength+2+2);
         buffer.put(bytes);
         buffer.put(byteCRC16(bytes));
         buffer.flip();
         return buffer;
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
        else
        {
        return MESSAGE_SUCCESS;
        }
    }
    
	/**设备地址*/
	private int address;
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
	
	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
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

    
}

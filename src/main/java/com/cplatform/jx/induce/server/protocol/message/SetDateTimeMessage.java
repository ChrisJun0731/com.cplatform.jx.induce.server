package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 设置时间日期请求
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年11月1日 下午3:37:31
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetDateTimeMessage extends AbstractMessage {

	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public SetDateTimeMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_DATETIME);
        this.getHeader().setTotalLength(14);//有消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length != 14) // 此消息长度为8个字节
        {
            return null;
        }

        this.getHeader().setAddress(getAddress());
        //拷贝头部 除 开始符外
        //转义  0xAA 或 0xCC 或 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        int len =3+7;
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);


        conbytes = ToolKits.int2buf(getYear(), conbytes, 3);      
        conbytes[conbytes.length-5]=(byte)getMonth();
        conbytes[conbytes.length-4]=(byte)getDay();
        conbytes[conbytes.length-3]=(byte)getHours();
        
        conbytes[conbytes.length-2]=(byte)getMinutes();
        conbytes[conbytes.length-1]=(byte)getSeconds();
        
        
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
	
	/**年 */
	private int year;
	/**月*/
	private int month;
	/**日*/
	private int day;
	/**小时*/
	private int hours;
	/**分钟*/
	private int minutes;
	/**秒*/
	private int seconds;

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getYear() {
    	return year;
    }

	
    public void setYear(int year) {
    	this.year = year;
    }

	
    public int getMonth() {
    	return month;
    }

	
    public void setMonth(int month) {
    	this.month = month;
    }

	
    public int getDay() {
    	return day;
    }

	
    public void setDay(int day) {
    	this.day = day;
    }

	
    public int getHours() {
    	return hours;
    }

	
    public void setHours(int hours) {
    	this.hours = hours;
    }

	
    public int getMinutes() {
    	return minutes;
    }

	
    public void setMinutes(int minutes) {
    	this.minutes = minutes;
    }

	
    public int getSeconds() {
    	return seconds;
    }

	
    public void setSeconds(int seconds) {
    	this.seconds = seconds;
    }
    
    

}

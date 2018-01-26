package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 设置温度过高操作参数
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月7日 下午3:31:15
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetHighTemperatureParamMessage extends AbstractMessage {

	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public SetHighTemperatureParamMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_HIGH_TEMPERATURE_PARAM);
        this.getHeader().setTotalLength(12);//有消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 12) // 此消息长度为8个字节
        {
            return null;
        }
              
        this.getHeader().setAddress(getAddress());
        //拷贝头部 除 开始符外
        //转义  0xAA 或 0xCC 或 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        int len =3+5;
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[conbytes.length-5]=(byte)getLimit();
        conbytes[conbytes.length-4]=(byte)getIsOpen();
        conbytes[conbytes.length-3]=(byte)getIsClosePower();
        conbytes[conbytes.length-2]=(byte)getFanLocationCms();
        conbytes[conbytes.length-1]=(byte)getFanLocation();
        
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
	
	/**温度阈值 */
	private int limit;
	/**是否开启风扇 0-不开启  1-开启*/
	private int isOpen;
	/**是否关闭电源 0-不关闭  1-关闭 */
	private int isClosePower;
	/**风扇接入的多功能卡位置 */
	private int fanLocationCms;
	/**风扇接入位置*/
	private int fanLocation;
	
	
	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getLimit() {
    	return limit;
    }

	
    public void setLimit(int limit) {
    	this.limit = limit;
    }

	
    public int getIsOpen() {
    	return isOpen;
    }

	
    public void setIsOpen(int isOpen) {
    	this.isOpen = isOpen;
    }

	
    public int getIsClosePower() {
    	return isClosePower;
    }

	
    public void setIsClosePower(int isClosePower) {
    	this.isClosePower = isClosePower;
    }

	
    public int getFanLocationCms() {
    	return fanLocationCms;
    }

	
    public void setFanLocationCms(int fanLocationCms) {
    	this.fanLocationCms = fanLocationCms;
    }

	
    public int getFanLocation() {
    	return fanLocation;
    }

	
    public void setFanLocation(int fanLocation) {
    	this.fanLocation = fanLocation;
    }



	
}

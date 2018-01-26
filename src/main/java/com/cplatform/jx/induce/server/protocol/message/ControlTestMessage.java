package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

/**
 * 测试控制请求
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年11月1日 下午2:56:28
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ControlTestMessage extends AbstractMessage {

	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public ControlTestMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(TEST_SCREEN);
        this.getHeader().setTotalLength(8);//有消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length != 8) // 此消息长度为8个字节
        {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(getHeader().byteMessage(4));
        buffer.put((byte)getMode());//测试模式
        buffer.put((byte)MESSAGE_END); // 结束码
        buffer.position(6);
        //计算CRC16验证码
        byte[] data = new byte[6];
        System.arraycopy(getHeader().byteMessage(4), 0, data, 0, 4);
        data[4] = (byte)getMode();
        data[5] = (byte)MESSAGE_END;
        
        buffer.put(byteCRC16(data)); // 验证码
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
	
	/**测试模式*/
	private int mode;
	

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getMode() {
    	return mode;
    }

	
    public void setMode(int mode) {
    	this.mode = mode;
    }
	
    
}

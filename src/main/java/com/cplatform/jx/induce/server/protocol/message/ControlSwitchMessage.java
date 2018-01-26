package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 控制开关屏请求
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年11月1日 下午3:14:20
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ControlSwitchMessage extends AbstractMessage {

	/**1-开屏，2-关（黑）屏*/
	public static final int SCREEN_OPEN = 1;
	/**1-开屏，2-关（黑）屏*/
	public static final int SCREEN_CLOSE = 2;
	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public ControlSwitchMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(CONTROL_SCREEN);
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

        this.getHeader().setAddress(getAddress());
        //拷贝头部 除 开始符外
        //转义  0xAA 或 0xCC 或 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        byte[] conbytes =new byte[3+1];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);
        conbytes[conbytes.length-1]=(byte)getSwitchs();
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
	
	/**开关状态 1-开屏，2-关（黑）屏*/
	private int switchs;
	

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getSwitchs() {
    	return switchs;
    }

	
    public void setSwitchs(int switchs) {
    	this.switchs = switchs;
    }

	

	
}

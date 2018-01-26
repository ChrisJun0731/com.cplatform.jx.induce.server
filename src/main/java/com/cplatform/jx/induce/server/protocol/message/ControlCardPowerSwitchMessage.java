package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 控制多功能卡电源开关 
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月10日 下午2:59:39
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ControlCardPowerSwitchMessage extends AbstractMessage {

	/**1-开启电源 ，2-关闭电源 其它值无效，不做处理。 */
	public static final int POWER_OPEN = 1;
	/**1-开启电源 ，2-关闭电源 其它值无效，不做处理。*/
	public static final int POWER_CLOSE = 2;
	
	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public ControlCardPowerSwitchMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(CONTROL_CARD_POWER_SWITCH);
        this.getHeader().setTotalLength(10);//有消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 10) // 此消息长度为8个字节
        {
            return null;
        }
        
        this.getHeader().setAddress(getAddress());
        //拷贝头部 除 开始符外
        //转义  0xAA 或 0xCC 或 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        byte[] conbytes =new byte[3+3];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);
        conbytes[conbytes.length-3]=(byte)getLocIndex();
        conbytes[conbytes.length-2]=(byte)getPowerIndex();
        conbytes[conbytes.length-1]=(byte)getStatus();
        
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
	
	/**多功能卡位置索引  0-255*/
	private int locIndex;
	/**电源路数索引  0-7*/
	private int powerIndex;
	/**电源状态  1-开启电源  2-关闭电源*/
	private int status;

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getLocIndex() {
    	return locIndex;
    }

	
    public void setLocIndex(int locIndex) {
    	this.locIndex = locIndex;
    }

	
    public int getPowerIndex() {
    	return powerIndex;
    }

	
    public void setPowerIndex(int powerIndex) {
    	this.powerIndex = powerIndex;
    }

	
    public int getStatus() {
    	return status;
    }

	
    public void setStatus(int status) {
    	this.status = status;
    }

    

}

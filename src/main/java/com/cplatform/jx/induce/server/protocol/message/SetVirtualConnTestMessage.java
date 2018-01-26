package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 设置虚连接检测信息 
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月7日 下午3:21:48
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetVirtualConnTestMessage extends AbstractMessage {

	/**0-不开启  1-开启虚连接检测*/
	public static final int CLOSE =0;
	public static final int OPEN =1;
	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public SetVirtualConnTestMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_VIRTUAL_CONN_TEST);
        this.getHeader().setTotalLength(11);//有消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 11) // 此消息长度为11个字节
        {
            return null;
        }
        
        this.getHeader().setAddress(getAddress());
        //拷贝头部 除 开始符外
        //转义  0xAA 或 0xCC 或 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        int len =3+4;
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[conbytes.length-4]=(byte)getIsOpen();
        ToolKits.int2buf(getTime(), conbytes, conbytes.length-3);
        conbytes[conbytes.length-1]=(byte)getBroadcast();
        
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
	
	/**是否开启虚连接检测0-不开启  1-开启虚连接检测  */
	private int isOpen;
	/**虚连接检测时间阈值  按低位在前，高位在后存储（单位为秒） */
	private int time;
	/**虚连接状态播放节目*/
	private int broadcast;
	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getIsOpen() {
    	return isOpen;
    }

	
    public void setIsOpen(int isOpen) {
    	this.isOpen = isOpen;
    }

	
    public int getTime() {
    	return time;
    }

	
    public void setTime(int time) {
    	this.time = time;
    }

	
    public int getBroadcast() {
    	return broadcast;
    }

	
    public void setBroadcast(int broadcast) {
    	this.broadcast = broadcast;
    }

	
   

}

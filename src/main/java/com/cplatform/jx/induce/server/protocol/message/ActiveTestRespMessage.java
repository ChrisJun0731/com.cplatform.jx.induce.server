package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 心跳
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月13日 下午4:23:08
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ActiveTestRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(ActiveTestRespMessage.class);
	
	public ActiveTestRespMessage(Header header)
    {
        super(header);
        getHeader().setTotalLength(11);
    }
    
    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {

    	try{
            if (length < 8) // 此消息长度为8个字节
            {
                return null;
            }
                   
            this.getHeader().setAddress(getAddress());
            //拷贝头部 除 开始符外
            //转义  0xAA 或 0xCC 或 0xEE
            byte[] headbytes = getHeader().byteMessage(3);
            int len =3+4;

            
            byte[] conbytes =new byte[len];
            System.arraycopy(headbytes, 0, conbytes, 0, 3);

            System.arraycopy(ToolKits.intToByte(getTime()), 0, conbytes, 3, 4);
            
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
        	catch(Exception e){
        		
        	}
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
			byte[] bytes =new byte[4];
			buffer.get(bytes);
			setTime(ToolKits.bytesToInt(bytes));
		}
		catch (Exception e)
		{
			log.error("解析数据异常",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }

    /**设备地址*/
	private int address;
	/**时间戳*/
	private int time;

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getTime() {
    	return time;
    }

	
    public void setTime(int time) {
    	this.time = time;
    }

    @Override
   	public String toString(){
   		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
   		buf.append("time=").append(time);
       	return buf.toString();
   	}
		
}

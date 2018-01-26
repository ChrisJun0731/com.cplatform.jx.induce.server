package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 查询版本信息
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月7日 上午11:04:59
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class QueryVersionMessage extends AbstractMessage {
	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public QueryVersionMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(QUERY_VERSION);
        this.getHeader().setTotalLength(7);//无消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
    	 if (length < 7) // 此消息长度为7个字节
         {
             return null;
         }
         this.getHeader().setAddress(getAddress());
         //拷贝头部 除 开始符外
         //转义  0xAA 或 0xCC 或 0xEE
         byte[] headbytes = getHeader().byteMessage(3);
         byte[] ecsbytes= ToolKits.covertChar(headbytes);
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
	
	
	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

}

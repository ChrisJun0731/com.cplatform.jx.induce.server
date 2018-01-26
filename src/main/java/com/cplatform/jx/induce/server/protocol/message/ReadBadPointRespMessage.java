package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 读取坏点信息 1
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月8日 上午9:53:52
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ReadBadPointRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(ReadBadPointRespMessage.class);
	
	public ReadBadPointRespMessage(Header header)
    {
        super(header);
        getHeader().setTotalLength(10);
    }
    
    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {

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
			setResult(buffer.get());// 执行情况
			byte[] bytes =new byte[2];
			buffer.get(bytes);
			int b = ToolKits.byteToShort(bytes);
			if(b<0) b+= 256;
			setBlock(b);
		}
		catch (Exception e)
		{
			log.error("解析数据异常",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**回应标志 固定值：1*/
    private int result;

    /**块大小  上传文件太大时分块上传，低字节在前，高字节在后 */
    private int block;
	
    public int getResult() {
    	return result;
    }

	
    public void setResult(int result) {
    	this.result = result;
    }

	
    public int getBlock() {
    	return block;
    }

	
    public void setBlock(int block) {
    	this.block = block;
    }
    
    @Override
   	public String toString(){
   		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
   		buf.append("result=").append(result).append(", ");
   		buf.append("block=").append(block);
       	return buf.toString();
   	}
}

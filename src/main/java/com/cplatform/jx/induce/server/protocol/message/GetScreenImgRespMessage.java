package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 屏幕当前显示内容 bmp
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月10日 下午2:04:20
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class GetScreenImgRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(GetScreenImgRespMessage.class);
	
	public GetScreenImgRespMessage(Header header)
    {
        super(header);
      
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
     * length 数据域长度
     */
    public int byteToMessage(ByteBuffer buffer,int length)
    {
        if (buffer == null)
		{
			return MESSAGE_ERROR;
		}
		try
		{	   
			byte[] bytes=new byte[2];
			buffer.get(bytes);
			setBlockNum(ToolKits.byteToShort(bytes));//块号
			int num = length - 2;
			
			if(num>0){
			  bytes = new byte[num];
			  buffer.get(bytes);
			  setBlock(bytes);
			}
			
		}
		catch (Exception e)
		{
			log.error("解析数据异常",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**块序号 */
    private int blockNum;
    /**数据块*/
    private byte[] block;

	
    public int getBlockNum() {
    	return blockNum;
    }

	
    public void setBlockNum(int blockNum) {
    	this.blockNum = blockNum;
    }

	
    public byte[] getBlock() {
    	return block;
    }

	
    public void setBlock(byte[] block) {
    	this.block = block;
    }

    @Override
   	public String toString(){		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
       	buf.append("blockNum=").append(blockNum).append(", ");
   		buf.append("block=").append(block);
       	return buf.toString();
   	}
   
}

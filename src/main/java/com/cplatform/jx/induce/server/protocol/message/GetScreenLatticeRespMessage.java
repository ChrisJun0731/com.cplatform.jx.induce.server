package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 获取屏幕点阵数据
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月10日 下午2:36:29
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class GetScreenLatticeRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(GetScreenLatticeRespMessage.class);
	
	public GetScreenLatticeRespMessage(Header header)
    {
        super(header);
        getHeader().setTotalLength(11);
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
		{	byte[] bytes=new byte[2]; 
		    buffer.get(bytes);
			setWide(ToolKits.byteToShort(bytes));// 宽
			buffer.get(bytes);
			setHigh(ToolKits.byteToShort(bytes));// 高
		}
		catch (Exception e)
		{
			log.error("解析数据异常",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**宽*/
    private int wide;

    /**高*/
    private int high;

	
    public int getWide() {
    	return wide;
    }

	
    public void setWide(int wide) {
    	this.wide = wide;
    }

	
    public int getHigh() {
    	return high;
    }

	
    public void setHigh(int high) {
    	this.high = high;
    }
	
    @Override
   	public String toString(){
   		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
   		buf.append("wide=").append(wide).append(", ");
   		buf.append("high=").append(high);
       	return buf.toString();
   	}
}

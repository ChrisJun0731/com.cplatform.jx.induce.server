package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 点检检测
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月7日 下午5:04:11
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class DetectionPointRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(DetectionPointRespMessage.class);
	
	public DetectionPointRespMessage(Header header)
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
			byte[] bytes= new byte[2];
			buffer.get(bytes);
			int badnum =ToolKits.byteToShort(bytes);
			if(badnum<0)
				badnum += 256;
			setBadNum(badnum);
		}
		catch (Exception e)
		{
			log.error("解析数据异常",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**执行情况*/
    private int result;

    /**坏点总数*/
    private int badNum;
	
    public int getResult() {
    	return result;
    }

	
    public void setResult(int result) {
    	this.result = result;
    }

	
    public int getBadNum() {
    	return badNum;
    }

	
    public void setBadNum(int badNum) {
    	this.badNum = badNum;
    }
    
    @Override
   	public String toString(){
   		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
   		buf.append("result=").append(result).append(", ");
   		buf.append("badNum=").append(badNum);
       	return buf.toString();
   	}
}

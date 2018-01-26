package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * 
 * 查询版本信息 响应. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月7日 上午11:05:16
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class QueryVersionRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(QueryVersionRespMessage.class);
	
	public QueryVersionRespMessage(Header header)
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
//			int one = buffer.get();
//			int two = buffer.get();
//			int three = buffer.get();
//			setVersion(one+"."+two+"."+three);
			
			int count = buffer.limit();
			byte[] bytes = new byte[count-buffer.position()];
			buffer.get(bytes);
			setVersion(new String(bytes,"utf-8").trim());
		}
		catch (Exception e)
		{
			log.error("解析数据异常",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**版本信息  版本格式：1.3.5（VER1 = 1,VER2 = 3,VER3 = 5）*/
    private String version;

	
    public String getVersion() {
    	return version;
    }

	
    public void setVersion(String version) {
    	this.version = version;
    }

    @Override
   	public String toString(){
   		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
   		buf.append("version=").append(version);
       	return buf.toString();
   	}
  
}

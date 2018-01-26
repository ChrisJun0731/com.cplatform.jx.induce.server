package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * 控制卡名称操作
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月7日 下午4:27:32
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetControlCardNameRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(SetControlCardNameRespMessage.class);
	
	public SetControlCardNameRespMessage(Header header)
    {
        super(header);
        getHeader().setTotalLength(8);
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
			int pos = buffer.position();
			int limit = buffer.limit();
			if(limit > pos){
			byte[] bytes = new byte[limit - pos];
			buffer.get(bytes);
			setName(new String(bytes,"utf-8").trim());
			}
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

    /**控制卡名称*/
    private String name;
	
    public int getResult() {
    	return result;
    }

	
    public void setResult(int result) {
    	this.result = result;
    }

	
    public String getName() {
    	return name;
    }

	
    public void setName(String name) {
    	this.name = name;
    }
    
    @Override
   	public String toString(){		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
   		buf.append("result=").append(result).append(", ");
   		buf.append("name=").append(name);
       	return buf.toString();
   	}
}

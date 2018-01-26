package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 *  查询环境参数 
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月7日 上午11:45:41
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class QueryControlParamRespMessage extends AbstractMessage {
static Logger log = Logger.getLogger(QueryControlParamRespMessage.class);
	
	public QueryControlParamRespMessage(Header header)
    {
        super(header);
        getHeader().setTotalLength(13);
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
			buffer.get();
			buffer.get();
			buffer.get();
			buffer.get();
			buffer.get();
			int sc =buffer.get();
			if(sc<0)
				setCloseScreen(sc+256);
			else
			setCloseScreen(sc);// 执行情况
		}
		catch (Exception e)
		{
			log.error("解析数据异常",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**报警关屏温度  正数   0 表示不进行关屏处理*/
    private int closeScreen;

	
    public int getCloseScreen() {
    	return closeScreen;
    }

	
    public void setCloseScreen(int closeScreen) {
    	this.closeScreen = closeScreen;
    }

    @Override
   	public String toString(){		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
   		buf.append("closeScreen=").append(closeScreen);
       	return buf.toString();
   	}

}

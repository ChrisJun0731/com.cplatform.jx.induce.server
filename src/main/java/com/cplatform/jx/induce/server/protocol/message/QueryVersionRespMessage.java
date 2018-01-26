package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * 
 * ��ѯ�汾��Ϣ ��Ӧ. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��7�� ����11:05:16
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
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
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {

         return null;
        
    }

    /**
     * ���ӿ�
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
			log.error("���������쳣",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**�汾��Ϣ  �汾��ʽ��1.3.5��VER1 = 1,VER2 = 3,VER3 = 5��*/
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

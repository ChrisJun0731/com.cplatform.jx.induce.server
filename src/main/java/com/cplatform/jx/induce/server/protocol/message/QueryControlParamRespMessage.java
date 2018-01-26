package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 *  ��ѯ�������� 
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��7�� ����11:45:41
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
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
			buffer.get();
			buffer.get();
			buffer.get();
			buffer.get();
			buffer.get();
			int sc =buffer.get();
			if(sc<0)
				setCloseScreen(sc+256);
			else
			setCloseScreen(sc);// ִ�����
		}
		catch (Exception e)
		{
			log.error("���������쳣",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**���������¶�  ����   0 ��ʾ�����й�������*/
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

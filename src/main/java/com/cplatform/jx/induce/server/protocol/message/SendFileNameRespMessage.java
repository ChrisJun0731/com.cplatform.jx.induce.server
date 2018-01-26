package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * 
 * �����ļ�����Ӧ. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��6�� ����3:42:35
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SendFileNameRespMessage extends AbstractMessage {

	/**��1-�ɹ���0-ʧ�ܣ�2-�ļ��� ���ڡ� */
	public static final int RESULT_OK=1;
	/**��1-�ɹ���0-ʧ�ܣ�2-�ļ��� ���ڡ� */
	public static final int RESULT_FAIL=0;
	/**��1-�ɹ���0-ʧ�ܣ�2-�ļ��� ���ڡ� */
	public static final int RESULT_SIMILAR=2;
	
  static Logger log = Logger.getLogger(SendFileNameRespMessage.class);
	
	public SendFileNameRespMessage(Header header)
    {
        super(header);
        getHeader().setTotalLength(8);
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
			setResult(buffer.get());// ִ�����
		}
		catch (Exception e)
		{
			log.error("���������쳣",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**ִ�����*/
    private int result;

	
    public int getResult() {
    	return result;
    }

	
    public void setResult(int result) {
    	this.result = result;
    }
    
    @Override
   	public String toString(){		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
   		buf.append("result=").append(result);
       	return buf.toString();
   	}
}

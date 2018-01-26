package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * �����
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��7�� ����5:04:11
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
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
			byte[] bytes= new byte[2];
			buffer.get(bytes);
			int badnum =ToolKits.byteToShort(bytes);
			if(badnum<0)
				badnum += 256;
			setBadNum(badnum);
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

    /**��������*/
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

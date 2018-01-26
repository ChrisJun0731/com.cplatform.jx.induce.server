package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ��ȡ��Ļ��������
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��10�� ����2:36:29
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
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
		{	byte[] bytes=new byte[2]; 
		    buffer.get(bytes);
			setWide(ToolKits.byteToShort(bytes));// ��
			buffer.get(bytes);
			setHigh(ToolKits.byteToShort(bytes));// ��
		}
		catch (Exception e)
		{
			log.error("���������쳣",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**��*/
    private int wide;

    /**��*/
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

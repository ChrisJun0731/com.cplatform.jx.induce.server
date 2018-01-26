package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ��ȡ������Ϣ 1
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��8�� ����9:53:52
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ReadBadPointRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(ReadBadPointRespMessage.class);
	
	public ReadBadPointRespMessage(Header header)
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
			byte[] bytes =new byte[2];
			buffer.get(bytes);
			int b = ToolKits.byteToShort(bytes);
			if(b<0) b+= 256;
			setBlock(b);
		}
		catch (Exception e)
		{
			log.error("���������쳣",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**��Ӧ��־ �̶�ֵ��1*/
    private int result;

    /**���С  �ϴ��ļ�̫��ʱ�ֿ��ϴ������ֽ���ǰ�����ֽ��ں� */
    private int block;
	
    public int getResult() {
    	return result;
    }

	
    public void setResult(int result) {
    	this.result = result;
    }

	
    public int getBlock() {
    	return block;
    }

	
    public void setBlock(int block) {
    	this.block = block;
    }
    
    @Override
   	public String toString(){
   		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
   		buf.append("result=").append(result).append(", ");
   		buf.append("block=").append(block);
       	return buf.toString();
   	}
}

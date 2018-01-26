package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ��Ļ��ǰ��ʾ���� bmp
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��10�� ����2:04:20
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class GetScreenImgRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(GetScreenImgRespMessage.class);
	
	public GetScreenImgRespMessage(Header header)
    {
        super(header);
      
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
     * length �����򳤶�
     */
    public int byteToMessage(ByteBuffer buffer,int length)
    {
        if (buffer == null)
		{
			return MESSAGE_ERROR;
		}
		try
		{	   
			byte[] bytes=new byte[2];
			buffer.get(bytes);
			setBlockNum(ToolKits.byteToShort(bytes));//���
			int num = length - 2;
			
			if(num>0){
			  bytes = new byte[num];
			  buffer.get(bytes);
			  setBlock(bytes);
			}
			
		}
		catch (Exception e)
		{
			log.error("���������쳣",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**����� */
    private int blockNum;
    /**���ݿ�*/
    private byte[] block;

	
    public int getBlockNum() {
    	return blockNum;
    }

	
    public void setBlockNum(int blockNum) {
    	this.blockNum = blockNum;
    }

	
    public byte[] getBlock() {
    	return block;
    }

	
    public void setBlock(byte[] block) {
    	this.block = block;
    }

    @Override
   	public String toString(){		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
       	buf.append("blockNum=").append(blockNum).append(", ");
   		buf.append("block=").append(block);
       	return buf.toString();
   	}
   
}

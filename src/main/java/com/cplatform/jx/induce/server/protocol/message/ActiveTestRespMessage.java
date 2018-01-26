package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ����
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��13�� ����4:23:08
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ActiveTestRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(ActiveTestRespMessage.class);
	
	public ActiveTestRespMessage(Header header)
    {
        super(header);
        getHeader().setTotalLength(11);
    }
    
    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {

    	try{
            if (length < 8) // ����Ϣ����Ϊ8���ֽ�
            {
                return null;
            }
                   
            this.getHeader().setAddress(getAddress());
            //����ͷ�� �� ��ʼ����
            //ת��  0xAA �� 0xCC �� 0xEE
            byte[] headbytes = getHeader().byteMessage(3);
            int len =3+4;

            
            byte[] conbytes =new byte[len];
            System.arraycopy(headbytes, 0, conbytes, 0, 3);

            System.arraycopy(ToolKits.intToByte(getTime()), 0, conbytes, 3, 4);
            
            byte[] ecsbytes= ToolKits.covertChar(conbytes);
            int ecslength = ecsbytes.length;
            //ת��� ���� ��ʼ������
            byte[] bytes = new byte[ecslength+2];
            bytes[0] =(byte)MESSAGE_BEGIN;
            System.arraycopy(ecsbytes, 0, bytes, 1, ecslength);
            bytes[ecslength+1] =(byte)MESSAGE_END;
            
            //����CRC16��֤��       
            ByteBuffer buffer = ByteBuffer.allocate(ecslength+2+2);
            buffer.put(bytes);
            buffer.put(byteCRC16(bytes));
            buffer.flip();
            return buffer;
        	}
        	catch(Exception e){
        		
        	}
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
			byte[] bytes =new byte[4];
			buffer.get(bytes);
			setTime(ToolKits.bytesToInt(bytes));
		}
		catch (Exception e)
		{
			log.error("���������쳣",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }

    /**�豸��ַ*/
	private int address;
	/**ʱ���*/
	private int time;

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getTime() {
    	return time;
    }

	
    public void setTime(int time) {
    	this.time = time;
    }

    @Override
   	public String toString(){
   		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
   		buf.append("time=").append(time);
       	return buf.toString();
   	}
		
}

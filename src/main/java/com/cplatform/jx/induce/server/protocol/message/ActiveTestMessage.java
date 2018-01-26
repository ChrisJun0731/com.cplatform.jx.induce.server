package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 
 * �������. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��13�� ����3:53:55
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ActiveTestMessage extends AbstractMessage {

	static Logger log = Logger.getLogger(ActiveTestMessage.class);
	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public ActiveTestMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(ACTIVE_TEST);
        this.getHeader().setTotalLength(13);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
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
        int len =3;
        byte[] bytesname = getName().getBytes("utf-8");
        len += bytesname.length;
        
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, 3);

        System.arraycopy(bytesname, 0, conbytes, 3, bytesname.length);
        
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
        else
        {
            return MESSAGE_SUCCESS;
        }
    }
    
	/**�豸��ַ*/
	private int address;
	
	/**�豸����*/
	private String name;
	

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public String getName() {
    	return name;
    }

	
    public void setName(String name) {
    	this.name = name;
    }
    
    
}

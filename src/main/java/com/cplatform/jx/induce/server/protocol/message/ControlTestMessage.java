package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

/**
 * ���Կ�������
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2016��11��1�� ����2:56:28
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ControlTestMessage extends AbstractMessage {

	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public ControlTestMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(TEST_SCREEN);
        this.getHeader().setTotalLength(8);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length != 8) // ����Ϣ����Ϊ8���ֽ�
        {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(getHeader().byteMessage(4));
        buffer.put((byte)getMode());//����ģʽ
        buffer.put((byte)MESSAGE_END); // ������
        buffer.position(6);
        //����CRC16��֤��
        byte[] data = new byte[6];
        System.arraycopy(getHeader().byteMessage(4), 0, data, 0, 4);
        data[4] = (byte)getMode();
        data[5] = (byte)MESSAGE_END;
        
        buffer.put(byteCRC16(data)); // ��֤��
        buffer.flip();
        return buffer;
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
	
	/**����ģʽ*/
	private int mode;
	

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getMode() {
    	return mode;
    }

	
    public void setMode(int mode) {
    	this.mode = mode;
    }
	
    
}

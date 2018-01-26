package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ���������Ӽ����Ϣ 
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��7�� ����3:21:48
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetVirtualConnTestMessage extends AbstractMessage {

	/**0-������  1-���������Ӽ��*/
	public static final int CLOSE =0;
	public static final int OPEN =1;
	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public SetVirtualConnTestMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_VIRTUAL_CONN_TEST);
        this.getHeader().setTotalLength(11);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 11) // ����Ϣ����Ϊ11���ֽ�
        {
            return null;
        }
        
        this.getHeader().setAddress(getAddress());
        //����ͷ�� �� ��ʼ����
        //ת��  0xAA �� 0xCC �� 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        int len =3+4;
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[conbytes.length-4]=(byte)getIsOpen();
        ToolKits.int2buf(getTime(), conbytes, conbytes.length-3);
        conbytes[conbytes.length-1]=(byte)getBroadcast();
        
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
	
	/**�Ƿ��������Ӽ��0-������  1-���������Ӽ��  */
	private int isOpen;
	/**�����Ӽ��ʱ����ֵ  ����λ��ǰ����λ�ں�洢����λΪ�룩 */
	private int time;
	/**������״̬���Ž�Ŀ*/
	private int broadcast;
	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getIsOpen() {
    	return isOpen;
    }

	
    public void setIsOpen(int isOpen) {
    	this.isOpen = isOpen;
    }

	
    public int getTime() {
    	return time;
    }

	
    public void setTime(int time) {
    	this.time = time;
    }

	
    public int getBroadcast() {
    	return broadcast;
    }

	
    public void setBroadcast(int broadcast) {
    	this.broadcast = broadcast;
    }

	
   

}

package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ���������Դ���أ�������ʹ�ã����ÿ��Ʊ����Դ���غͿ��� �����ƴ��棩
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��7�� ����11:23:13
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ControlScreenPowerMessage extends AbstractMessage {

	/**1-������2-�أ��ڣ���*/
	public static final int SCREEN_OPEN = 1;
	/**1-������2-�أ��ڣ���*/
	public static final int SCREEN_CLOSE = 2;
	
	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public ControlScreenPowerMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(CONTROL_SCREEN_POWER);
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

        this.getHeader().setAddress(getAddress());
        //����ͷ�� �� ��ʼ����
        //ת��  0xAA �� 0xCC �� 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        byte[] conbytes =new byte[3+1];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);
        conbytes[conbytes.length-1]=(byte)getPowerswitch();
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
	
	/**������Ļ��Դ����  1-��  2-�ر� */
	private int powerswitch;


	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getPowerswitch() {
    	return powerswitch;
    }

	
    public void setPowerswitch(int powerswitch) {
    	this.powerswitch = powerswitch;
    }
    
    

}

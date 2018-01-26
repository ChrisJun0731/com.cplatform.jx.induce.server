package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ������������
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2016��11��1�� ����3:22:19
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ControlLightMessage extends AbstractMessage{

	/**���ȿ��Ʒ�ʽ��1B��1-�Զ���2-�ֶ���3-��ʱ*/
	public static final int LIGHT_TYPE_AUTO =1;
	
	/**���ȿ��Ʒ�ʽ��1B��1-�Զ���2-�ֶ���3-��ʱ*/
	public static final int LIGHT_TYPE_MANUAL =2;
	
	/**���ȿ��Ʒ�ʽ��1B��1-�Զ���2-�ֶ���3-��ʱ*/
	public static final int LIGHT_TYPE_TIMING =3;
	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public ControlLightMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(CONTROL_LIGHT);
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
        int len =3+1;
        if(getType() == 2) len = 3+1+1;
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);
        
        if(getType() == 2) {
        	conbytes[conbytes.length-2]=(byte)getType();
        	conbytes[conbytes.length-1]=(byte)getLevel();
        }
        else{
        	conbytes[conbytes.length-1]=(byte)getType();
        }
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
	
	/**���ȿ��Ʒ�ʽ��1B 1-�Զ� 2-�ֶ��� */
	private int type;
	/**�ֶ����ȼ���1B 1-255����*/
	private int level;

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getType() {
    	return type;
    }

	
    public void setType(int type) {
    	this.type = type;
    }

	
    public int getLevel() {
    	return level;
    }

	
    public void setLevel(int level) {
    	this.level = level;
    }

	
   

}

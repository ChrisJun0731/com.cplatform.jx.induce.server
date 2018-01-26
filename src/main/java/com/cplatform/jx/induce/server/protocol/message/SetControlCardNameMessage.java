package com.cplatform.jx.induce.server.protocol.message;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ���ƿ����Ʋ���
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��7�� ����4:29:09
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetControlCardNameMessage extends AbstractMessage {

	/**0-���ÿ��ƿ����� ��1-��ȡ���ƿ����� */
	public static final int SET_NAME =0;
	
	public static final int GET_NAME =1;
	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public SetControlCardNameMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_CONTROL_CARD_NAME);
        this.getHeader().setTotalLength(8);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 8) // ����Ϣ����Ϊ8���ֽ�
        {
            return null;
        }       
        
        this.getHeader().setAddress(getAddress());
        //����ͷ�� �� ��ʼ����
        //ת��  0xAA �� 0xCC �� 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        
        int len =3+1;
        byte [] namebytes =null;
        if(getType() == SET_NAME){       	
        	setName(replace(getName()));
        	try {
	            namebytes = getName().getBytes("utf-8");
            }
            catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
            }
        	len+=namebytes.length;
        }
       
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[3] = (byte)getType();
        
        if(getType() == SET_NAME){
        System.arraycopy(namebytes, 0, conbytes, 4, namebytes.length);
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
	
	/**������ʽ  0-���ÿ��ƿ����� 1-��ȡ���ƿ����� */
	private int type;
	/** ���ƿ�����  ����ʱΪ���ƿ����ƣ���ȡʱΪ 0 �ֽ�*/
	private String name;

	
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

	
    public String getName() {
    	return name;
    }

	
    public void setName(String name) {
    	this.name = name;
    }

    /**
     * �����ÿ��ƿ�����ʱ���ܰ��� �� # �� �� '���� ��
     * @param name
     * @return
     */
	private String replace(String name){
		name = name.replaceAll("#", "");
		name = name.replaceAll("��", "");
		return name;
	}
 

}

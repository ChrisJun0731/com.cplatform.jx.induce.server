package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * �����¶ȹ��߲�������
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��7�� ����3:31:15
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetHighTemperatureParamMessage extends AbstractMessage {

	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public SetHighTemperatureParamMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_HIGH_TEMPERATURE_PARAM);
        this.getHeader().setTotalLength(12);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 12) // ����Ϣ����Ϊ8���ֽ�
        {
            return null;
        }
              
        this.getHeader().setAddress(getAddress());
        //����ͷ�� �� ��ʼ����
        //ת��  0xAA �� 0xCC �� 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        int len =3+5;
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[conbytes.length-5]=(byte)getLimit();
        conbytes[conbytes.length-4]=(byte)getIsOpen();
        conbytes[conbytes.length-3]=(byte)getIsClosePower();
        conbytes[conbytes.length-2]=(byte)getFanLocationCms();
        conbytes[conbytes.length-1]=(byte)getFanLocation();
        
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
	
	/**�¶���ֵ */
	private int limit;
	/**�Ƿ������� 0-������  1-����*/
	private int isOpen;
	/**�Ƿ�رյ�Դ 0-���ر�  1-�ر� */
	private int isClosePower;
	/**���Ƚ���Ķ๦�ܿ�λ�� */
	private int fanLocationCms;
	/**���Ƚ���λ��*/
	private int fanLocation;
	
	
	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getLimit() {
    	return limit;
    }

	
    public void setLimit(int limit) {
    	this.limit = limit;
    }

	
    public int getIsOpen() {
    	return isOpen;
    }

	
    public void setIsOpen(int isOpen) {
    	this.isOpen = isOpen;
    }

	
    public int getIsClosePower() {
    	return isClosePower;
    }

	
    public void setIsClosePower(int isClosePower) {
    	this.isClosePower = isClosePower;
    }

	
    public int getFanLocationCms() {
    	return fanLocationCms;
    }

	
    public void setFanLocationCms(int fanLocationCms) {
    	this.fanLocationCms = fanLocationCms;
    }

	
    public int getFanLocation() {
    	return fanLocation;
    }

	
    public void setFanLocation(int fanLocation) {
    	this.fanLocation = fanLocation;
    }



	
}

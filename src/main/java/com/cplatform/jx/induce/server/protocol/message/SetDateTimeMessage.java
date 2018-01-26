package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ����ʱ����������
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2016��11��1�� ����3:37:31
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetDateTimeMessage extends AbstractMessage {

	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public SetDateTimeMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_DATETIME);
        this.getHeader().setTotalLength(14);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length != 14) // ����Ϣ����Ϊ8���ֽ�
        {
            return null;
        }

        this.getHeader().setAddress(getAddress());
        //����ͷ�� �� ��ʼ����
        //ת��  0xAA �� 0xCC �� 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        int len =3+7;
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);


        conbytes = ToolKits.int2buf(getYear(), conbytes, 3);      
        conbytes[conbytes.length-5]=(byte)getMonth();
        conbytes[conbytes.length-4]=(byte)getDay();
        conbytes[conbytes.length-3]=(byte)getHours();
        
        conbytes[conbytes.length-2]=(byte)getMinutes();
        conbytes[conbytes.length-1]=(byte)getSeconds();
        
        
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
	
	/**�� */
	private int year;
	/**��*/
	private int month;
	/**��*/
	private int day;
	/**Сʱ*/
	private int hours;
	/**����*/
	private int minutes;
	/**��*/
	private int seconds;

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getYear() {
    	return year;
    }

	
    public void setYear(int year) {
    	this.year = year;
    }

	
    public int getMonth() {
    	return month;
    }

	
    public void setMonth(int month) {
    	this.month = month;
    }

	
    public int getDay() {
    	return day;
    }

	
    public void setDay(int day) {
    	this.day = day;
    }

	
    public int getHours() {
    	return hours;
    }

	
    public void setHours(int hours) {
    	this.hours = hours;
    }

	
    public int getMinutes() {
    	return minutes;
    }

	
    public void setMinutes(int minutes) {
    	this.minutes = minutes;
    }

	
    public int getSeconds() {
    	return seconds;
    }

	
    public void setSeconds(int seconds) {
    	this.seconds = seconds;
    }
    
    

}

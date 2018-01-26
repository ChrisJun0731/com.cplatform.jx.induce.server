package com.cplatform.jx.induce.server.protocol.message;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ����ֲ���������
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��10�� ����3:08:36
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class UpdateLocalScreenContentMessage extends AbstractMessage {

	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public UpdateLocalScreenContentMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(UPDATE_LOCAL_SCREEN);
        this.getHeader().setTotalLength(9);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 9) // ����Ϣ����Ϊ8���ֽ�
        {
            return null;
        }
        
        this.getHeader().setAddress(getAddress());
        //����ͷ�� �� ��ʼ����
        //ת��  0xAA �� 0xCC �� 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        int len =3+2;
        byte[] item =null;
        if(getItem() !=null&& (!getItem().isEmpty())){
        	try {
	            item =getItem().getBytes("utf-8");
            }
            catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
            }
        }
        if(getType() == 1&&item!= null){
        	len += item.length;
        }
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[3]=(byte)getType();
        conbytes[4]=(byte)getAreaIndex();
        
        if(getType() == 1&&item!= null){
        	System.arraycopy(item, 0, conbytes, 5, item.length);
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
	
	/**ʵʱ���²��� 0-�Ƴ�ʵʱ��������  1-����ʵʱ����������ʾ���� */
	private int type;
	/**������������  0-9�����֧�� 10 ��ʵʱ��������*/
	private int areaIndex;
	/**ʵʱ����������ʾ����  �������ݲ��ղ����б��� Item �Ķ��壬�Ƴ�ʵʱ��������ʱ����û�иò���*/
	private String item;
	
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

	
    public int getAreaIndex() {
    	return areaIndex;
    }

	
    public void setAreaIndex(int areaIndex) {
    	this.areaIndex = areaIndex;
    }

	
    public String getItem() {
    	return item;
    }

	
    public void setItem(String item) {
    	this.item = item;
    }

    
    

}

package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ���õ����� 
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��16�� ����4:44:44
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetDetectionPointMessage extends AbstractMessage {

	/**��ʾ����ɫ ��ɫ��0x01����ɫ��0x02����ɫ��0x04*/
	public static final int SCREEN_COLOR_RED = 0x01;
	/**��ʾ����ɫ ��ɫ��0x01����ɫ��0x02����ɫ��0x04*/
	public static final int SCREEN_COLOR_GREEN = 0x02;
	/**��ʾ����ɫ ��ɫ��0x01����ɫ��0x02����ɫ��0x04*/
	public static final int SCREEN_COLOR_BLUE = 0x04;
	
	/**�������  ��·��0����·��1�� */
	public static final int TYPE_OPEN = 0;
	/**�������  ��·��0����·��1�� */
	public static final int TYPE_SHORT = 1;
	
	/**�����ֵ  ���֣�ֵ�ֱ�Ϊ 1,2,3,4��ѡ��һ���·�*/
	public static final int CURRENT_ONE = 1;
	/**�����ֵ  ���֣�ֵ�ֱ�Ϊ 1,2,3,4��ѡ��һ���·�*/
	public static final int CURRENT_TWO = 2;
	/**�����ֵ  ���֣�ֵ�ֱ�Ϊ 1,2,3,4��ѡ��һ���·�*/
	public static final int CURRENT_THREE = 3;
	/**�����ֵ  ���֣�ֵ�ֱ�Ϊ 1,2,3,4��ѡ��һ���·�*/
	public static final int CURRENT_FOUR = 4;
	/**�Ƿ��õ������� �ǣ�1����0�� */
	public static final int OPEN_CURRENT_ADD =1;
	/**�Ƿ��õ������� �ǣ�1����0�� */
	public static final int CLOSE_CURRENT_ADD =0;
	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public SetDetectionPointMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_DETECTION_POINT_PARAM);
        this.getHeader().setTotalLength(15);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
    	 if (length < 15) // ����Ϣ����Ϊ7���ֽ�
         {
             return null;
         }
    	 this.getHeader().setAddress(getAddress());
         //����ͷ�� �� ��ʼ����
         //ת��  0xAA �� 0xCC �� 0xEE
         byte[] headbytes = getHeader().byteMessage(3);
         int len =3+8;
         byte[] conbytes =new byte[len];
         System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);
         conbytes[conbytes.length-1]=(byte)getLightColor();
         conbytes[conbytes.length-2]=(byte)getType();
         conbytes[conbytes.length-3]=(byte)getLimit();
         conbytes[conbytes.length-4]=(byte)getIsCurrentAdd();
         
         conbytes[conbytes.length-5]=(byte)getRed();
         conbytes[conbytes.length-6]=(byte)getGreen();
         conbytes[conbytes.length-7]=(byte)getBlue();
         
         conbytes[conbytes.length-8]=(byte)0;
         
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
	/**��ʾ����ɫ  ��ɫ��0x01����ɫ��0x02����ɫ��0x04�� */
	private int lightColor;
	/**������� ��·��0����·��1*/
	private int type;
	/**�����ֵ  ���֣�ֵ�ֱ�Ϊ 1,2,3,4��ѡ��һ���·�*/
	private int limit;
	/**�Ƿ��õ������� �ǣ�1����0*/
	private int IsCurrentAdd;
	/**�������� ���������ɫ����ɫ����ɫ���֣���ռһ���ֽڣ�ȡֵ��Χ��Ϊ 1-255*/
	private int red;
	/**�������� ���������ɫ����ɫ����ɫ���֣���ռһ���ֽڣ�ȡֵ��Χ��Ϊ 1-255*/
	private int green;
	/**�������� ���������ɫ����ɫ����ɫ���֣���ռһ���ֽڣ�ȡֵ��Χ��Ϊ 1-255*/
	private int blue;
	
	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getLightColor() {
    	return lightColor;
    }

	
    public void setLightColor(int lightColor) {
    	this.lightColor = lightColor;
    }

	
    public int getType() {
    	return type;
    }

	
    public void setType(int type) {
    	this.type = type;
    }

	
    public int getLimit() {
    	return limit;
    }

	
    public void setLimit(int limit) {
    	this.limit = limit;
    }

	
    

	
    
    public int getIsCurrentAdd() {
    	return IsCurrentAdd;
    }

	
    public void setIsCurrentAdd(int isCurrentAdd) {
    	IsCurrentAdd = isCurrentAdd;
    }

	public int getRed() {
    	return red;
    }

	
    public void setRed(int red) {
    	this.red = red;
    }

	
    public int getGreen() {
    	return green;
    }

	
    public void setGreen(int green) {
    	this.green = green;
    }

	
    public int getBlue() {
    	return blue;
    }

	
    public void setBlue(int blue) {
    	this.blue = blue;
    }

    
}

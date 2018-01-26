package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 *  �����֤��ͨ�Ű�ȫԼ�� 
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��7�� ����3:45:24
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetAuthSecurityParamMessage extends AbstractMessage {

	/**�Ƿ���� DES ���� 0-������  1-���� */
	public static final int DES_CLOSE= 0;
	/**�Ƿ���� DES ���� 0-������  1-���� */
	public static final int DES_OPEN= 1;
	
	/**�ļ�����ʱ�Ƿ���� MD5 У�� 0-������  1-���� */
	public static final int MD5_CLOSE= 0;
	/**�ļ�����ʱ�Ƿ���� MD5 У�� 0-������  1-���� */
	public static final int MD5_OPEN= 1;
	
	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public SetAuthSecurityParamMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_AUTH_SECURITY_PARAM);
        this.getHeader().setTotalLength(19);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 19) // ����Ϣ����Ϊ8���ֽ�
        {
            return null;
        }
        
        this.getHeader().setAddress(getAddress());
        //����ͷ�� �� ��ʼ����
        //ת��  0xAA �� 0xCC �� 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        
        int len =3+12;
        if(!getAuthorization().isEmpty()){
        	len+=getAuthorization().getBytes().length;
        }
       
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[3] = (byte)getIsDES();//�Ƿ����� DES ���� 
     
        conbytes[4] = (byte)getIsMD5();//�Ƿ�ʹ�� MD5 У�� 
        
        if(!getAuthorization().isEmpty()){//��֤���루��Ȩ����
        	byte[] bks =new byte[16];
        	bks =getAuthorization().getBytes();
        	System.arraycopy(bks, 0, conbytes, 5, bks.length);
        }
        conbytes[len-10] = (byte)0x91;//�ָ�� 1

        conbytes[len-9] = (byte)0x21;//�ָ�� 2
        byte[] bks =new byte[8];
        bks =getKey().getBytes();
        System.arraycopy(bks, 0, conbytes, len-8, bks.length);//DES �ӽ��� Key
             
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
	
	/**�Ƿ����� DES ����  0-������  1-����  */
	private int isDES;
	/**�Ƿ�ʹ�� MD5 У��  0-������  1-����*/
	private int isMD5;
    /**��֤���루��Ȩ���룩*/
	private String Authorization ;
	/**DES����key*/
	private String key;
	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getIsDES() {
    	return isDES;
    }

	
    public void setIsDES(int isDES) {
    	this.isDES = isDES;
    }

	
    public int getIsMD5() {
    	return isMD5;
    }

	
    public void setIsMD5(int isMD5) {
    	this.isMD5 = isMD5;
    }

	
    public String getKey() {
    	return key;
    }

	
    public void setKey(String key) {
    	this.key = key;
    }

	
    public String getAuthorization() {
    	return Authorization;
    }

	
    public void setAuthorization(String authorization) {
    	Authorization = authorization;
    }
    
    

}

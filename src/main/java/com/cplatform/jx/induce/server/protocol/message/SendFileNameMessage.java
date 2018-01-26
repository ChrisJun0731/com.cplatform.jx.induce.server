package com.cplatform.jx.induce.server.protocol.message;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 
 * �����ļ���. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��6�� ����3:42:57
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SendFileNameMessage extends AbstractMessage {

	public static final int MAX_BLOCK_SIZE= 65535;
	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public SendFileNameMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SEND_FILE_NAME);
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
        byte [] namebytes =null;    	

        try {
	          namebytes = getFileName().getBytes("utf-8");
          }
          catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
          }
        
         len+=namebytes.length;
        
       
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        //������
        byte[] temp = new byte[len-3];
        
        ToolKits.int2buf(getBlockSize(), temp, 0);
        
        System.arraycopy(namebytes, 0, temp, 2, namebytes.length);
        
        //des ����
        if(isDES()){
        	temp=  encryptDES(temp, getDesKey());
        	conbytes =new byte[3+temp.length];
        	System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);
        	System.arraycopy(temp, 0, conbytes, 3, temp.length);
        }
        else{
        	System.arraycopy(temp, 0, conbytes, 3, temp.length);
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
	
	/**���С */
	private int blockSize;
	/**�ļ���*/
	private String fileName;

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getBlockSize() {
    	return blockSize;
    }

	
    public void setBlockSize(int blockSize) {
    	this.blockSize = blockSize;
    }

	
    public String getFileName() {
    	return fileName;
    }

	
    public void setFileName(String fileName) {
    	this.fileName = fileName;
    }

	
   
}

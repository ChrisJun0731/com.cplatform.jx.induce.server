package com.cplatform.jx.induce.server.protocol.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.protocol.constants.ErrorCode;
import com.cplatform.jx.induce.server.protocol.tools.CRC16;
import com.cplatform.jx.induce.server.protocol.tools.DES;
import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

import net.sf.json.JSONObject;


/**
 * @Title: ��Ϣ������
 * @Description: ��Ϣ�����࣬ʵ��һЩ���÷���
 * @Copyright: Copyright (c) 2006-8-30
 * @Company: ��������ʮ�����ּ������޹�˾
 * @Author: chenwei
 * @Version: 1.0
 */
public abstract class AbstractMessage implements Message, CommandID, ErrorCode
{
    public AbstractMessage()
    {
        
    }
    
    public AbstractMessage(Header header)
    {
        setHeader(header);
    }
    
    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        return null;
    }
    
    /**
     * ���ӿ�
     */
    public int byteToMessage(ByteBuffer buffer)
    {
        return SUCCESS;
    }
    /**
     * ���ӿ�
     */
    public byte[] byteMessage(int length)
    {
        return byteBufferedMessage(length).array();
    }

    /**
     * ����CRC16��֤��
     * @param data
     * @return
     */
    public byte[] byteCRC16(byte[] data)
    {
    	
    	return ToolKits.shortToByte((short) CRC16.caluCRCs(data));
    }
    
    /**
     * des��������
     * @param datasource
     * @param password
     */
    public byte[] encryptDES(byte[] datasource, String password){
    	return DES.encrypt(datasource, password);
    }
    
    /**
     * des����
     * @param datasource
     * @param password
     * @return
     * @throws Exception
     */
    public byte[] decryptDES(byte[] datasource, String password) throws Exception{
    	return DES.decrypt(datasource, password);
    }
    /**
     * ���ӿ�
     */
    public int byteToMessage(byte[] bytes)
    {
        return byteToMessage(ByteBuffer.wrap(bytes));
    }

    /**
     * ���ӿ�
     */
    public void putMessageOut(OutputStream out) throws IOException
    {
        out.write(byteMessage(getHeader().getTotalLength())); // ��д��Ϣ��
    }
    
    /**
     * ���ӿ�
     */
    public int getMessageIn(InputStream in, int position,int length) throws IOException
    {
        if (in == null)
        {
            return ERROR;
        }
        
        //  ѭ����ȡֱ����ȡ��������
        int bodyLength = length;
        byte[] bytes = new byte[bodyLength]; 
        byte[] bytestemp = new byte[1]; 
        int count=0;
        while(true){
        	in.read(bytestemp, position, 1); 
        	bytes[count] = bytestemp[0];
        	position++;
        	if(bytes[count]==CommandID.MESSAGE_END){       		
        		//�ٴζ�ȡ2�ֽ� У���� CRC
        		byte[] bytescrc = new byte[2];
        		in.read(bytescrc, position, 2);
        		count++;
        		bytes[count] = bytescrc[0];
        		count++;
        		bytes[count] = bytescrc[1];
        		
        		break;
        	}
        	else
        	    count++;
        }

        byte[] byteall = new byte[count];
        System.arraycopy(bytes, 0, byteall, 0, count);
        header.setTotalLength(count+4);//��Ϣ�ܳ���
        //CRC16У�飿��
        
        return byteToMessage(byteall); // ��װ��Ϣ��
    }

  
    /**
     * ��ΪҪ�ж���Ϣ�����Ϣͷ��ҪԤ�ȶ�ȡ��
     * �ɴ˷���������Ϣʵ��
     * @param header, Header
     */
    public void setHeader(Header header)
    {
        this.header = header;
    }
    
    /**
     * ��ȡ��Ϣͷ
     * @return Header
     */
    public Header getHeader()
    {
        return header;
    }
    
    public boolean isMd5() {
    	return isMd5;
    }

	
    public void setMd5(boolean isMd5) {
    	this.isMd5 = isMd5;
    }

	
    public boolean isDES() {
    	return isDES;
    }

	
    public void setDES(boolean isDES) {
    	this.isDES = isDES;
    }

	
    public String getDesKey() {
    	return desKey;
    }

	
    public void setDesKey(String desKey) {
    	this.desKey = desKey;
    }

	
    public String getAuthorizationKey() {
    	return authorizationKey;
    }

	
    public void setAuthorizationKey(String authorizationKey) {
    	this.authorizationKey = authorizationKey;
    }



	private Header header;  // ��Ϣͷ
    
    private boolean isMd5 = false;
    
    private boolean isDES = false;
    
    private String desKey = "";
    
    private String authorizationKey = "";
    
    @Override
	public String toString() {
		try {
			return JSONObject.fromObject(this).toString();
		}
		catch (Exception ex) {
			return super.toString();
		}
	}
}

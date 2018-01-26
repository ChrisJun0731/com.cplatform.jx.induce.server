package com.cplatform.jx.induce.server.protocol.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.constants.ErrorCode;
import com.cplatform.jx.induce.server.protocol.tools.ToolKits;


/**
 * @Title: ��Ϣͷ
 * @Description: ������Ϣͷ
 * @Copyright: Copyright (c) 2006-8-30
 * @Company: ��������ʮ�����ּ������޹�˾
 * @Author: chenwei
 * @Version: 1.0
 */
public class Header implements Message, ErrorCode
{
    /**
     * ������
     *
     */
    public Header()
    {
        
    }
    
    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 3) 
        {
            return null;
        }
        
        ByteBuffer buffer = ByteBuffer.allocate(3);
//        buffer.put((byte)getBegin()); // ��ʼ
        byte[] address = new byte [2];
        buffer.put(ToolKits.int2buf(getAddress(), address, 0)); // �豸��ַ
        buffer.position(2);
        buffer.put((byte)getCommand()); // ָ��
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
        
//        setBegin(buffer.get());  
        byte[] bytes=new byte[2];
        buffer.get(bytes);
        buffer.position(2);
        int address = ToolKits.byteToShort(bytes);
        if(address<0)
        	address += 256;
        setAddress(address);
        int command = buffer.get();
        if(command <0)
        	command += 256;
        setCommand(command); // Command

        return MESSAGE_SUCCESS;
        
    }
    
    /**
     * ���ӿ�
     */
    public byte[] byteMessage(int length)
    {
        return byteBufferedMessage(length).array();
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
       out.write(byteMessage(4));
    }
    
    /**
     * ���ӿ�
     */
    public int getMessageIn(InputStream in, int position,int length) throws IOException
    {
        if (in == null)
        {
            return MESSAGE_ERROR;
        }
        
        byte[] temp = new byte[4];
        in.read(temp, position, 4);
        return byteToMessage(temp);
    }
   
    private int begin;
    
    private int command; // �������Ӧ����
                                           
    private int address; // �豸��ַ  
    
    private int totalLength;//�ܳ���

	/**
	 * @return ���� command��
	 */
	public int getCommand()
	{
		return command;
	}

	/**
	 * @param command Ҫ���õ� command��
	 */
	public void setCommand(int command)
	{
		this.command = command;
	}

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getBegin() {
    	return begin;
    }

	
    public void setBegin(int begin) {
    	this.begin = begin;
    }

	
    public int getTotalLength() {
    	return totalLength;
    }

	
    public void setTotalLength(int totalLength) {
    	this.totalLength = totalLength;
    }

	
    
}

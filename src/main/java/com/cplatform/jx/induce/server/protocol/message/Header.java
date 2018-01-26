package com.cplatform.jx.induce.server.protocol.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.constants.ErrorCode;
import com.cplatform.jx.induce.server.protocol.tools.ToolKits;


/**
 * @Title: 消息头
 * @Description: 定义消息头
 * @Copyright: Copyright (c) 2006-8-30
 * @Company: 北京宽连十方数字技术有限公司
 * @Author: chenwei
 * @Version: 1.0
 */
public class Header implements Message, ErrorCode
{
    /**
     * 构造器
     *
     */
    public Header()
    {
        
    }
    
    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 3) 
        {
            return null;
        }
        
        ByteBuffer buffer = ByteBuffer.allocate(3);
//        buffer.put((byte)getBegin()); // 开始
        byte[] address = new byte [2];
        buffer.put(ToolKits.int2buf(getAddress(), address, 0)); // 设备地址
        buffer.position(2);
        buffer.put((byte)getCommand()); // 指令
        buffer.flip();
        return buffer;
    }
    
    /**
     * 见接口
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
     * 见接口
     */
    public byte[] byteMessage(int length)
    {
        return byteBufferedMessage(length).array();
    }
    
    /**
     * 见接口
     */
    public int byteToMessage(byte[] bytes)
    {
        return byteToMessage(ByteBuffer.wrap(bytes));
    }
    
    /**
     * 见接口
     */
    public void putMessageOut(OutputStream out) throws IOException
    {
       out.write(byteMessage(4));
    }
    
    /**
     * 见接口
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
    
    private int command; // 命令或响应类型
                                           
    private int address; // 设备地址  
    
    private int totalLength;//总长度

	/**
	 * @return 返回 command。
	 */
	public int getCommand()
	{
		return command;
	}

	/**
	 * @param command 要设置的 command。
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

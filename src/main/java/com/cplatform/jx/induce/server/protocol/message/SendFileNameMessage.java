package com.cplatform.jx.induce.server.protocol.message;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 
 * 发送文件名. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月6日 下午3:42:57
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SendFileNameMessage extends AbstractMessage {

	public static final int MAX_BLOCK_SIZE= 65535;
	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public SendFileNameMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SEND_FILE_NAME);
        this.getHeader().setTotalLength(9);//有消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 9) // 此消息长度为8个字节
        {
            return null;
        }
        this.getHeader().setAddress(getAddress());
        //拷贝头部 除 开始符外
        //转义  0xAA 或 0xCC 或 0xEE
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

        //数据域
        byte[] temp = new byte[len-3];
        
        ToolKits.int2buf(getBlockSize(), temp, 0);
        
        System.arraycopy(namebytes, 0, temp, 2, namebytes.length);
        
        //des 加密
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
        //转义后 加上 开始结束符
        byte[] bytes = new byte[ecslength+2];
        bytes[0] =(byte)MESSAGE_BEGIN;
        System.arraycopy(ecsbytes, 0, bytes, 1, ecslength);
        bytes[ecslength+1] =(byte)MESSAGE_END;
        
        //计算CRC16验证码       
        ByteBuffer buffer = ByteBuffer.allocate(ecslength+2+2);
        buffer.put(bytes);
        buffer.put(byteCRC16(bytes));
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
        else
        {
        return MESSAGE_SUCCESS;
        }
    }
    
	/**设备地址*/
	private int address;
	
	/**块大小 */
	private int blockSize;
	/**文件名*/
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

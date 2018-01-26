package com.cplatform.jx.induce.server.protocol.message;
import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 发送文件内容
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月6日 下午4:10:46
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SendFileContentMessage extends AbstractMessage {

	public static final int FILE_BLOCK_MAX_SIZE=65535 ;
	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public SendFileContentMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SEND_FILE_CONTENT);
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
        byte [] namebytes =getFileContent();  
        if(namebytes != null)
        len+=namebytes.length;      
       
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        ToolKits.int2buf(getBlockNum(), conbytes, 3);
        
        if(namebytes != null)
        System.arraycopy(namebytes, 0, conbytes, 5, namebytes.length);
        
             
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
	
	/**块号 */
	private int blockNum;
	/**文件内额*/
	private byte[] fileContent;

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getBlockNum() {
    	return blockNum;
    }

	
    public void setBlockNum(int blockNum) {
    	this.blockNum = blockNum;
    }

	
    public byte[] getFileContent() {
    	return fileContent;
    }

	
    public void setFileContent(byte[] fileContent) {
    	this.fileContent = fileContent;
    }

	
    

}

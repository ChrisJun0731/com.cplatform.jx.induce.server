package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 *  身份验证和通信安全约定 
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月7日 下午3:45:24
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetAuthSecurityParamMessage extends AbstractMessage {

	/**是否进行 DES 加密 0-不启用  1-启用 */
	public static final int DES_CLOSE= 0;
	/**是否进行 DES 加密 0-不启用  1-启用 */
	public static final int DES_OPEN= 1;
	
	/**文件传输时是否采用 MD5 校验 0-不启用  1-启用 */
	public static final int MD5_CLOSE= 0;
	/**文件传输时是否采用 MD5 校验 0-不启用  1-启用 */
	public static final int MD5_OPEN= 1;
	
	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public SetAuthSecurityParamMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_AUTH_SECURITY_PARAM);
        this.getHeader().setTotalLength(19);//有消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 19) // 此消息长度为8个字节
        {
            return null;
        }
        
        this.getHeader().setAddress(getAddress());
        //拷贝头部 除 开始符外
        //转义  0xAA 或 0xCC 或 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        
        int len =3+12;
        if(!getAuthorization().isEmpty()){
        	len+=getAuthorization().getBytes().length;
        }
       
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[3] = (byte)getIsDES();//是否启用 DES 加密 
     
        conbytes[4] = (byte)getIsMD5();//是否使用 MD5 校验 
        
        if(!getAuthorization().isEmpty()){//认证密码（授权密码
        	byte[] bks =new byte[16];
        	bks =getAuthorization().getBytes();
        	System.arraycopy(bks, 0, conbytes, 5, bks.length);
        }
        conbytes[len-10] = (byte)0x91;//分割符 1

        conbytes[len-9] = (byte)0x21;//分割符 2
        byte[] bks =new byte[8];
        bks =getKey().getBytes();
        System.arraycopy(bks, 0, conbytes, len-8, bks.length);//DES 加解密 Key
             
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
	
	/**是否启用 DES 加密  0-不启用  1-启用  */
	private int isDES;
	/**是否使用 MD5 校验  0-不启用  1-启用*/
	private int isMD5;
    /**认证密码（授权密码）*/
	private String Authorization ;
	/**DES加密key*/
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

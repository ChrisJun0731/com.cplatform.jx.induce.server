package com.cplatform.jx.induce.server.protocol.message;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 控制卡名称操作
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月7日 下午4:29:09
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetControlCardNameMessage extends AbstractMessage {

	/**0-设置控制卡名称 ，1-获取控制卡名称 */
	public static final int SET_NAME =0;
	
	public static final int GET_NAME =1;
	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public SetControlCardNameMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_CONTROL_CARD_NAME);
        this.getHeader().setTotalLength(8);//有消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 8) // 此消息长度为8个字节
        {
            return null;
        }       
        
        this.getHeader().setAddress(getAddress());
        //拷贝头部 除 开始符外
        //转义  0xAA 或 0xCC 或 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        
        int len =3+1;
        byte [] namebytes =null;
        if(getType() == SET_NAME){       	
        	setName(replace(getName()));
        	try {
	            namebytes = getName().getBytes("utf-8");
            }
            catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
            }
        	len+=namebytes.length;
        }
       
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[3] = (byte)getType();
        
        if(getType() == SET_NAME){
        System.arraycopy(namebytes, 0, conbytes, 4, namebytes.length);
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
	
	/**操作方式  0-设置控制卡名称 1-获取控制卡名称 */
	private int type;
	/** 控制卡名称  设置时为控制卡名称，获取时为 0 字节*/
	private String name;

	
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

	
    public String getName() {
    	return name;
    }

	
    public void setName(String name) {
    	this.name = name;
    }

    /**
     * 在设置控制卡名称时不能包含 ’ # ’ 和 '→’ 字
     * @param name
     * @return
     */
	private String replace(String name){
		name = name.replaceAll("#", "");
		name = name.replaceAll("→", "");
		return name;
	}
 

}

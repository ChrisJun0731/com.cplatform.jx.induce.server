package com.cplatform.jx.induce.server.protocol.message;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 屏体局部更新内容
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月10日 下午3:08:36
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class UpdateLocalScreenContentMessage extends AbstractMessage {

	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public UpdateLocalScreenContentMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(UPDATE_LOCAL_SCREEN);
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
        byte[] item =null;
        if(getItem() !=null&& (!getItem().isEmpty())){
        	try {
	            item =getItem().getBytes("utf-8");
            }
            catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
            }
        }
        if(getType() == 1&&item!= null){
        	len += item.length;
        }
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[3]=(byte)getType();
        conbytes[4]=(byte)getAreaIndex();
        
        if(getType() == 1&&item!= null){
        	System.arraycopy(item, 0, conbytes, 5, item.length);
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
	
	/**实时更新操作 0-移除实时更新区域  1-更新实时更新区域显示内容 */
	private int type;
	/**更新区域索引  0-9（最多支持 10 个实时更新区域）*/
	private int areaIndex;
	/**实时更新区域显示内容  更新内容参照播放列表中 Item 的定义，移除实时更新区域时可以没有该部分*/
	private String item;
	
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

	
    public int getAreaIndex() {
    	return areaIndex;
    }

	
    public void setAreaIndex(int areaIndex) {
    	this.areaIndex = areaIndex;
    }

	
    public String getItem() {
    	return item;
    }

	
    public void setItem(String item) {
    	this.item = item;
    }

    
    

}

package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 设置终端自动亮度控制参数
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月6日 下午4:35:57
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetControlLightMessage extends AbstractMessage {

	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public SetControlLightMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_LIGHT_CONTROL_PARAM);
        this.getHeader().setTotalLength(31);//有消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 31) // 此消息长度为31个字节
        {
            return null;
        }

        int param = 0;
        this.getHeader().setAddress(getAddress());
        //拷贝头部 除 开始符外
        //转义  0xAA 或 0xCC 或 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        int len =3+24;
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[conbytes.length-24]=(byte)getLight1();
        conbytes[conbytes.length-23]=(byte)getWidth1();
        conbytes[conbytes.length-22]=(byte)param;
        
        conbytes[conbytes.length-21]=(byte)getLight2();
        conbytes[conbytes.length-20]=(byte)getWidth2();
        conbytes[conbytes.length-19]=(byte)param;
        
        conbytes[conbytes.length-18]=(byte)getLight3();
        conbytes[conbytes.length-17]=(byte)getWidth3();
        conbytes[conbytes.length-16]=(byte)param;
        
        conbytes[conbytes.length-15]=(byte)getLight4();
        conbytes[conbytes.length-14]=(byte)getWidth4();
        conbytes[conbytes.length-13]=(byte)param;
        
        conbytes[conbytes.length-12]=(byte)getLight5();
        conbytes[conbytes.length-11]=(byte)getWidth5();
        conbytes[conbytes.length-10]=(byte)param;
        
        conbytes[conbytes.length-9]=(byte)getLight6();
        conbytes[conbytes.length-8]=(byte)getWidth6();
        conbytes[conbytes.length-7]=(byte)param;
        
        conbytes[conbytes.length-6]=(byte)getLight7();
        conbytes[conbytes.length-5]=(byte)getWidth7();
        conbytes[conbytes.length-4]=(byte)param;
        
        conbytes[conbytes.length-3]=(byte)getLight8();
        conbytes[conbytes.length-2]=(byte)getWidth8();
        conbytes[conbytes.length-1]=(byte)param;
        
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
	
	/**1 级环境亮度 */
	private int light1;
	/**1 级屏体度*/
	private int width1;

	/**1 级环境亮度 */
	private int light2;
	/**1 级屏体度*/
	private int width2;
	
	/**1 级环境亮度 */
	private int light3;
	/**1 级屏体度*/
	private int width3;
	
	/**1 级环境亮度 */
	private int light4;
	/**1 级屏体度*/
	private int width4;
	
	/**1 级环境亮度 */
	private int light5;
	/**1 级屏体度*/
	private int width5;
	
	/**1 级环境亮度 */
	private int light6;
	/**1 级屏体度*/
	private int width6;
	
	/**1 级环境亮度 */
	private int light7;
	/**1 级屏体度*/
	private int width7;
	
	/**1 级环境亮度 */
	private int light8;
	/**1 级屏体度*/
	private int width8;

	
	
	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getLight1() {
    	return light1;
    }

	
    public void setLight1(int light1) {
    	this.light1 = light1;
    }

	
    public int getWidth1() {
    	return width1;
    }

	
    public void setWidth1(int width1) {
    	this.width1 = width1;
    }

	
    public int getLight2() {
    	return light2;
    }

	
    public void setLight2(int light2) {
    	this.light2 = light2;
    }

	
    public int getWidth2() {
    	return width2;
    }

	
    public void setWidth2(int width2) {
    	this.width2 = width2;
    }

	
    public int getLight3() {
    	return light3;
    }

	
    public void setLight3(int light3) {
    	this.light3 = light3;
    }

	
    public int getWidth3() {
    	return width3;
    }

	
    public void setWidth3(int width3) {
    	this.width3 = width3;
    }

	
    public int getLight4() {
    	return light4;
    }

	
    public void setLight4(int light4) {
    	this.light4 = light4;
    }

	
    public int getWidth4() {
    	return width4;
    }

	
    public void setWidth4(int width4) {
    	this.width4 = width4;
    }

	
    public int getLight5() {
    	return light5;
    }

	
    public void setLight5(int light5) {
    	this.light5 = light5;
    }

	
    public int getWidth5() {
    	return width5;
    }

	
    public void setWidth5(int width5) {
    	this.width5 = width5;
    }

	
    public int getLight6() {
    	return light6;
    }

	
    public void setLight6(int light6) {
    	this.light6 = light6;
    }

	
    public int getWidth6() {
    	return width6;
    }

	
    public void setWidth6(int width6) {
    	this.width6 = width6;
    }

	
    public int getLight7() {
    	return light7;
    }

	
    public void setLight7(int light7) {
    	this.light7 = light7;
    }

	
    public int getWidth7() {
    	return width7;
    }

	
    public void setWidth7(int width7) {
    	this.width7 = width7;
    }

	
    public int getLight8() {
    	return light8;
    }

	
    public void setLight8(int light8) {
    	this.light8 = light8;
    }

	
    public int getWidth8() {
    	return width8;
    }

	
    public void setWidth8(int width8) {
    	this.width8 = width8;
    }

	
    

}

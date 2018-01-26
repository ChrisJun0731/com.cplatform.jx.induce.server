package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 
 * 设置基本参数. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月6日 下午4:49:33
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetBaseParamMessage extends AbstractMessage {
	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public SetBaseParamMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_BASE_PARAM);
        this.getHeader().setTotalLength(33);//有消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 33) // 此消息长度为33个字节
        {
            return null;
        }
        
        this.getHeader().setAddress(getAddress());
        //拷贝头部 除 开始符外
        //转义  0xAA 或 0xCC 或 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        int len =3+26;
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        ToolKits.int2buf(getScreenNum(), conbytes, 3);//屏号
     
        ToolKits.ip2byte(getIp(),conbytes,5);//异步卡 IP 地址
               
        ToolKits.int2buf(getPort(), conbytes, 9);//异步卡端口

        ToolKits.ip2byte(getMask(), conbytes,11);//异步卡子网掩 码

        ToolKits.ip2byte(getGateway(), conbytes,15);//异步卡网关

        ToolKits.ip2byte(getCmsip(), conbytes,19);//上位机 IP 地址
        
        conbytes[conbytes.length-6]=(byte)0;
        conbytes[conbytes.length-5]=(byte)0;
        conbytes[conbytes.length-4]=(byte)getReport();//上报设置
        
        conbytes[conbytes.length-3]=(byte)0;//预留
        conbytes[conbytes.length-2]=(byte)1;//预留
        conbytes[conbytes.length-1]=(byte)1;//预留     
        
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
	
	/**屏编号 */
	private int screenNum;
	/**异步卡 IP 地址*/
	private String ip;
	/**异步卡端口*/
	private int port;
	/**异步卡子网掩 码*/
	private String mask;
	/**异步卡网关*/
	private String gateway;
	/**上位机 IP 地址*/
	private String cmsip;
	/**预留*/
	/**上报设置 0-不主动上报  1-主动上报*/
	private int report;

    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getScreenNum() {
    	return screenNum;
    }

	
    public void setScreenNum(int screenNum) {
    	this.screenNum = screenNum;
    }

	
    public String getIp() {
    	return ip;
    }

	
    public void setIp(String ip) {
    	this.ip = ip;
    }

	
    public int getPort() {
    	return port;
    }

	
    public void setPort(int port) {
    	this.port = port;
    }

	
    public String getMask() {
    	return mask;
    }

	
    public void setMask(String mask) {
    	this.mask = mask;
    }

	
    public String getGateway() {
    	return gateway;
    }

	
    public void setGateway(String gateway) {
    	this.gateway = gateway;
    }

	
    public String getCmsip() {
    	return cmsip;
    }

	
    public void setCmsip(String cmsip) {
    	this.cmsip = cmsip;
    }

	
    public int getReport() {
    	return report;
    }

	
    public void setReport(int report) {
    	this.report = report;
    }

	
    
}

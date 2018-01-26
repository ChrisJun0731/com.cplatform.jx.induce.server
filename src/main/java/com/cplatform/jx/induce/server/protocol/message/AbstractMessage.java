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
 * @Title: 信息抽象类
 * @Description: 信息抽象类，实现一些共用方法
 * @Copyright: Copyright (c) 2006-8-30
 * @Company: 北京宽连十方数字技术有限公司
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
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        return null;
    }
    
    /**
     * 见接口
     */
    public int byteToMessage(ByteBuffer buffer)
    {
        return SUCCESS;
    }
    /**
     * 见接口
     */
    public byte[] byteMessage(int length)
    {
        return byteBufferedMessage(length).array();
    }

    /**
     * 生成CRC16验证码
     * @param data
     * @return
     */
    public byte[] byteCRC16(byte[] data)
    {
    	
    	return ToolKits.shortToByte((short) CRC16.caluCRCs(data));
    }
    
    /**
     * des加密数据
     * @param datasource
     * @param password
     */
    public byte[] encryptDES(byte[] datasource, String password){
    	return DES.encrypt(datasource, password);
    }
    
    /**
     * des解密
     * @param datasource
     * @param password
     * @return
     * @throws Exception
     */
    public byte[] decryptDES(byte[] datasource, String password) throws Exception{
    	return DES.decrypt(datasource, password);
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
        out.write(byteMessage(getHeader().getTotalLength())); // 再写消息体
    }
    
    /**
     * 见接口
     */
    public int getMessageIn(InputStream in, int position,int length) throws IOException
    {
        if (in == null)
        {
            return ERROR;
        }
        
        //  循环读取直到读取到结束符
        int bodyLength = length;
        byte[] bytes = new byte[bodyLength]; 
        byte[] bytestemp = new byte[1]; 
        int count=0;
        while(true){
        	in.read(bytestemp, position, 1); 
        	bytes[count] = bytestemp[0];
        	position++;
        	if(bytes[count]==CommandID.MESSAGE_END){       		
        		//再次读取2字节 校验码 CRC
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
        header.setTotalLength(count+4);//消息总长度
        //CRC16校验？？
        
        return byteToMessage(byteall); // 封装消息体
    }

  
    /**
     * 因为要判断信息类别，信息头需要预先读取，
     * 由此方法传入消息实例
     * @param header, Header
     */
    public void setHeader(Header header)
    {
        this.header = header;
    }
    
    /**
     * 获取信息头
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



	private Header header;  // 消息头
    
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

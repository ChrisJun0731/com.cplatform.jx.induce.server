package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.message.info.SwitchTimeInfo;
import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 设置终端定时开关屏的控制参数。 
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月17日 上午10:08:16
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetScreenTimeSwitchParamMessage extends AbstractMessage {

	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public SetScreenTimeSwitchParamMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_SCREEN_TIME_SWITCH_PARAM);
        this.getHeader().setTotalLength(8);//有消息内容
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 8) // 此消息长度为11个字节
        {
            return null;
        }
        int p= 14;
        this.getHeader().setAddress(getAddress());
        //拷贝头部 除 开始符外
        //转义  0xAA 或 0xCC 或 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        int len =3+1+14*getPtimeNum();
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[3]=(byte)getPtimeNum();
        
        SwitchTimeInfo [] pt = getStimeInfo();
        if(getPtimeNum()>0&&pt != null&&pt.length>0 ){
        for(int i=0;i<pt.length;i++){       	
        	conbytes[4+i*p] = (byte)pt[i].getBeginTime().getDay();//day
        	conbytes[4+i*p+1] = (byte)pt[i].getBeginTime().getMonth();//month
        	ToolKits.int2buf(pt[i].getBeginTime().getYear(), conbytes, 4+i*p+1+1);//year
        	conbytes[4+i*p+1+2+1] = (byte)pt[i].getBeginTime().getSeconds();//sec
        	conbytes[4+i*p+1+2+1+1] = (byte)pt[i].getBeginTime().getMinutes();//min
        	conbytes[4+i*p+1+2+1+1+1] = (byte)pt[i].getBeginTime().getHours();//hours
        	
        	conbytes[4+i*p+1+2+1+1+1+1] = (byte)pt[i].getEndTime().getDay();//day
        	conbytes[4+i*p+1+2+1+1+1+1+1] = (byte)pt[i].getEndTime().getMonth();//month
        	ToolKits.int2buf(pt[i].getEndTime().getYear(), conbytes, 4+i*p+1+2+1+1+1+1+1+1);//year
        	conbytes[4+i*p+1+2+1+1+1+1+1+2+1] = (byte)pt[i].getEndTime().getSeconds();//sec
        	conbytes[4+i*p+1+2+1+1+1+1+1+2+1+1] = (byte)pt[i].getEndTime().getMinutes();//min
        	conbytes[4+i*p+1+2+1+1+1+1+1+2+1+1+1] = (byte)pt[i].getEndTime().getHours();//hours
        }
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
	
	/**时间段个数 记录下发多少个时间段控制参数  如果下发时间段个数为零，则会清空所有设置的时间段。  */
	private int ptimeNum;
	/**时间段  */
	private SwitchTimeInfo [] stimeInfo;
	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getPtimeNum() {
    	return ptimeNum;
    }

	
    public void setPtimeNum(int ptimeNum) {
    	this.ptimeNum = ptimeNum;
    }

	
    public SwitchTimeInfo[] getStimeInfo() {
    	return stimeInfo;
    }

	
    public void setStimeInfo(SwitchTimeInfo[] stimeInfo) {
    	this.stimeInfo = stimeInfo;
    }

	
  
    
}

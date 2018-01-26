package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;
import java.util.List;

import com.cplatform.jx.induce.server.protocol.message.info.PlayTimeInfo;
import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 设备收到上位机时间段控制播放列表命令 
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月7日 下午2:34:46
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetCmsDisplayTimeMessage extends AbstractMessage {

	 /**
     * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
     */
    public SetCmsDisplayTimeMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_CMS_DISPLAY_TIME);
        this.getHeader().setTotalLength(20);//有消息内容
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
        
        int p =16;
        this.getHeader().setAddress(getAddress());
        //拷贝头部 除 开始符外
        //转义  0xAA 或 0xCC 或 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        int len =3+1+16*getTimeNum();
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        conbytes[3]=(byte)getTimeNum();
        
        List<PlayTimeInfo> pt = getPlaytimeInfo();
        if(getTimeNum()>0&&pt != null&&pt.size()>0 ){
        for(int i=0;i<pt.size();i++){       	
        	conbytes[4+i*p] = (byte)pt.get(i).getBeginTime().getDay();//day
        	conbytes[4+i*p+1] = (byte)pt.get(i).getBeginTime().getMonth();//month
        	ToolKits.int2buf(pt.get(i).getBeginTime().getYear(), conbytes, 4+i*p+1+1);//year
        	conbytes[4+i*p+1+2+1] = (byte)pt.get(i).getBeginTime().getSeconds();//sec
        	conbytes[4+i*p+1+2+1+1] = (byte)pt.get(i).getBeginTime().getMinutes();//min
        	conbytes[4+i*p+1+2+1+1+1] = (byte)pt.get(i).getBeginTime().getHours();//hours
        	
        	conbytes[4+i*p+1+2+1+1+1+1] = (byte)pt.get(i).getEndTime().getDay();//day
        	conbytes[4+i*p+1+2+1+1+1+1+1] = (byte)pt.get(i).getEndTime().getMonth();//month
        	ToolKits.int2buf(pt.get(i).getEndTime().getYear(), conbytes, 4+i*p+1+2+1+1+1+1+1+1);//year
        	conbytes[4+i*p+1+2+1+1+1+1+1+2+1] = (byte)pt.get(i).getEndTime().getSeconds();//sec
        	conbytes[4+i*p+1+2+1+1+1+1+1+2+1+1] = (byte)pt.get(i).getEndTime().getMinutes();//min
        	conbytes[4+i*p+1+2+1+1+1+1+1+2+1+1+1] = (byte)pt.get(i).getEndTime().getHours();//hours
        	
        	conbytes[4+i*p+1+2+1+1+1+1+1+2+1+1+1+1] = (byte)pt.get(i).getPlaylistnum();//hours
        	conbytes[4+i*p+1+2+1+1+1+1+1+2+1+1+1+1+1] = (byte)pt.get(i).getPlaynum();//hours
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
	
	/**时间段个数 */
	private int timeNum;
	/**时间*/
	private List<PlayTimeInfo> playtimeInfo;
	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getTimeNum() {
    	return timeNum;
    }

	
    public void setTimeNum(int timeNum) {
    	this.timeNum = timeNum;
    }

	
    public List<PlayTimeInfo> getPlaytimeInfo() {
    	return playtimeInfo;
    }

	
    public void setPlaytimeInfo(List<PlayTimeInfo> playtimeInfo) {
    	this.playtimeInfo = playtimeInfo;
    }

    @Override
   	public String toString(){		
       	StringBuffer buf = new StringBuffer(100);
       	buf.append("address=").append(address).append(", ");
    	buf.append("timeNum=").append(timeNum).append(", ");
    	buf.append("playtimeInfo=").append(playtimeInfo.toString());

       	return buf.toString();
   	}
}

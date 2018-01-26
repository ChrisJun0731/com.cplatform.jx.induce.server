package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;
import java.util.List;

import com.cplatform.jx.induce.server.protocol.message.info.PlayTimeInfo;
import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * �豸�յ���λ��ʱ��ο��Ʋ����б����� 
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��7�� ����2:34:46
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetCmsDisplayTimeMessage extends AbstractMessage {

	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public SetCmsDisplayTimeMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_CMS_DISPLAY_TIME);
        this.getHeader().setTotalLength(20);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {        
        if (length < 8) // ����Ϣ����Ϊ11���ֽ�
        {
            return null;
        }
        
        int p =16;
        this.getHeader().setAddress(getAddress());
        //����ͷ�� �� ��ʼ����
        //ת��  0xAA �� 0xCC �� 0xEE
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
        //ת��� ���� ��ʼ������
        byte[] bytes = new byte[ecslength+2];
        bytes[0] =(byte)MESSAGE_BEGIN;
        System.arraycopy(ecsbytes, 0, bytes, 1, ecslength);
        bytes[ecslength+1] =(byte)MESSAGE_END;
        
        //����CRC16��֤��       
        ByteBuffer buffer = ByteBuffer.allocate(ecslength+2+2);
        buffer.put(bytes);
        buffer.put(byteCRC16(bytes));
        buffer.flip();
        return buffer;
    }

    /**
     * ���ӿ�
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
    
	/**�豸��ַ*/
	private int address;
	
	/**ʱ��θ��� */
	private int timeNum;
	/**ʱ��*/
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

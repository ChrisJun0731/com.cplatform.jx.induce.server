package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.message.info.SwitchTimeInfo;
import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * �����ն˶�ʱ�������Ŀ��Ʋ����� 
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��17�� ����10:08:16
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetScreenTimeSwitchParamMessage extends AbstractMessage {

	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public SetScreenTimeSwitchParamMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_SCREEN_TIME_SWITCH_PARAM);
        this.getHeader().setTotalLength(8);//����Ϣ����
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
        int p= 14;
        this.getHeader().setAddress(getAddress());
        //����ͷ�� �� ��ʼ����
        //ת��  0xAA �� 0xCC �� 0xEE
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
	
	/**ʱ��θ��� ��¼�·����ٸ�ʱ��ο��Ʋ���  ����·�ʱ��θ���Ϊ�㣬�������������õ�ʱ��Ρ�  */
	private int ptimeNum;
	/**ʱ���  */
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

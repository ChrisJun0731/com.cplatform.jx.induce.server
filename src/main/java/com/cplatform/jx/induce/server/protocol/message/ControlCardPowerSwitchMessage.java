package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ���ƶ๦�ܿ���Դ���� 
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��10�� ����2:59:39
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ControlCardPowerSwitchMessage extends AbstractMessage {

	/**1-������Դ ��2-�رյ�Դ ����ֵ��Ч���������� */
	public static final int POWER_OPEN = 1;
	/**1-������Դ ��2-�رյ�Դ ����ֵ��Ч����������*/
	public static final int POWER_CLOSE = 2;
	
	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public ControlCardPowerSwitchMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(CONTROL_CARD_POWER_SWITCH);
        this.getHeader().setTotalLength(10);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 10) // ����Ϣ����Ϊ8���ֽ�
        {
            return null;
        }
        
        this.getHeader().setAddress(getAddress());
        //����ͷ�� �� ��ʼ����
        //ת��  0xAA �� 0xCC �� 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        byte[] conbytes =new byte[3+3];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);
        conbytes[conbytes.length-3]=(byte)getLocIndex();
        conbytes[conbytes.length-2]=(byte)getPowerIndex();
        conbytes[conbytes.length-1]=(byte)getStatus();
        
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
	
	/**�๦�ܿ�λ������  0-255*/
	private int locIndex;
	/**��Դ·������  0-7*/
	private int powerIndex;
	/**��Դ״̬  1-������Դ  2-�رյ�Դ*/
	private int status;

	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

	
    public int getLocIndex() {
    	return locIndex;
    }

	
    public void setLocIndex(int locIndex) {
    	this.locIndex = locIndex;
    }

	
    public int getPowerIndex() {
    	return powerIndex;
    }

	
    public void setPowerIndex(int powerIndex) {
    	this.powerIndex = powerIndex;
    }

	
    public int getStatus() {
    	return status;
    }

	
    public void setStatus(int status) {
    	this.status = status;
    }

    

}

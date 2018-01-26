package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 
 * ���û�������. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��6�� ����4:49:33
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SetBaseParamMessage extends AbstractMessage {
	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public SetBaseParamMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(SET_BASE_PARAM);
        this.getHeader().setTotalLength(33);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
        if (length < 33) // ����Ϣ����Ϊ33���ֽ�
        {
            return null;
        }
        
        this.getHeader().setAddress(getAddress());
        //����ͷ�� �� ��ʼ����
        //ת��  0xAA �� 0xCC �� 0xEE
        byte[] headbytes = getHeader().byteMessage(3);
        int len =3+26;
        byte[] conbytes =new byte[len];
        System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

        ToolKits.int2buf(getScreenNum(), conbytes, 3);//����
     
        ToolKits.ip2byte(getIp(),conbytes,5);//�첽�� IP ��ַ
               
        ToolKits.int2buf(getPort(), conbytes, 9);//�첽���˿�

        ToolKits.ip2byte(getMask(), conbytes,11);//�첽�������� ��

        ToolKits.ip2byte(getGateway(), conbytes,15);//�첽������

        ToolKits.ip2byte(getCmsip(), conbytes,19);//��λ�� IP ��ַ
        
        conbytes[conbytes.length-6]=(byte)0;
        conbytes[conbytes.length-5]=(byte)0;
        conbytes[conbytes.length-4]=(byte)getReport();//�ϱ�����
        
        conbytes[conbytes.length-3]=(byte)0;//Ԥ��
        conbytes[conbytes.length-2]=(byte)1;//Ԥ��
        conbytes[conbytes.length-1]=(byte)1;//Ԥ��     
        
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
	
	/**����� */
	private int screenNum;
	/**�첽�� IP ��ַ*/
	private String ip;
	/**�첽���˿�*/
	private int port;
	/**�첽�������� ��*/
	private String mask;
	/**�첽������*/
	private String gateway;
	/**��λ�� IP ��ַ*/
	private String cmsip;
	/**Ԥ��*/
	/**�ϱ����� 0-�������ϱ�  1-�����ϱ�*/
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

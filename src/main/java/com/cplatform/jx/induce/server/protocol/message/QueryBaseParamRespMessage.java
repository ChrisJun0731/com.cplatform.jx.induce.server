package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;


/**
 *  ��ѯ�������� 
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��7�� ����11:28:06
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class QueryBaseParamRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(QueryBaseParamRespMessage.class);
	
	public QueryBaseParamRespMessage(Header header)
    {
        super(header);
        getHeader().setTotalLength(8);
    }
    
    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {

         return null;
        
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
		try
		{	
			byte[] bytes =new byte[2];
			buffer.get(bytes);		
			setScreenNum(ToolKits.byteToShort(bytes));
			
			setIp(ToolKits.ip2Str(buffer.getInt()));
			
			bytes =new byte[2];
			buffer.get(bytes);
			setPort(ToolKits.byteToShort(bytes));		
			setMask(ToolKits.ip2Str(buffer.getInt()));
			setGateway(ToolKits.ip2Str(buffer.getInt()));
			setCmsip(ToolKits.ip2Str(buffer.getInt()));			
			bytes = new byte[2];
			buffer.get(bytes);
			
			setReport(buffer.get());
		}
		catch (Exception e)
		{
			log.error("���������쳣",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }

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
    

    @Override
   	public String toString(){
   		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
   		buf.append("screenNum=").append(screenNum).append(", ");
   		buf.append("ip=").append(ip).append(", ");
   		buf.append("port=").append(port).append(", ");
   		buf.append("mask=").append(mask).append(", ");
   		buf.append("gateway=").append(gateway).append(", ");
   		buf.append("cmsip=").append(cmsip).append(", ");
   		buf.append("report=").append(report);
       	return buf.toString();
   	}

}

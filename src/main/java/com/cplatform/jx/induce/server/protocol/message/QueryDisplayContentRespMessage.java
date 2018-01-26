package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 *  ��ѯ��ǰ�������� 
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��7�� ����1:59:45
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class QueryDisplayContentRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(QueryDisplayContentRespMessage.class);
	
	public QueryDisplayContentRespMessage(Header header)
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
			setSwitchState(buffer.get());//
			setPlayType(buffer.get());
			setListNum(buffer.get());
			byte[] data = new byte[8];
			buffer.get(data);
			setTitle(new String(data,"utf-8").trim());
			//����?
			
			int count = buffer.limit();
			byte[] bytes = new byte[count-buffer.position()];
			buffer.get(bytes);
			setContent(new String(bytes,"utf-8").trim());
		}
		catch (Exception e)
		{
			log.error("���������쳣",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**��������־ 1-��ʾ����  2-��ʾ����������ʱ����������Ч*/
    private int switchState;

    /**�������ͱ�־  1-�б��� 2-��������  3-����*/
    private int playType;
    
    /**�����б�� ��ǰ���ŵ��б��Ż���Ա��*/
    private int listNum;
    
    /**����ͷ*/
    private String title;
    
    /**��ǰ��������*/
    private String content;

	
    public int getSwitchState() {
    	return switchState;
    }

	
    public void setSwitchState(int switchState) {
    	this.switchState = switchState;
    }

	
    public int getPlayType() {
    	return playType;
    }

	
    public void setPlayType(int playType) {
    	this.playType = playType;
    }

	
    public int getListNum() {
    	return listNum;
    }

	
    public void setListNum(int listNum) {
    	this.listNum = listNum;
    }

	
    public String getTitle() {
    	return title;
    }

	
    public void setTitle(String title) {
    	this.title = title;
    }

	
    public String getContent() {
    	return content;
    }

	
    public void setContent(String content) {
    	this.content = content;
    }
	
    @Override
   	public String toString(){		
       	StringBuffer buf = new StringBuffer(500);
       	buf.append("address=").append(getHeader().getAddress()).append(", ");
       	buf.append("switchState=").append(switchState).append(", ");
       	buf.append("playType=").append(playType).append(", ");
       	buf.append("listNum=").append(listNum).append(", ");
       	buf.append("title=").append(title).append(", ");
       	buf.append("content=").append(content);
       	return buf.toString();
   	}
}

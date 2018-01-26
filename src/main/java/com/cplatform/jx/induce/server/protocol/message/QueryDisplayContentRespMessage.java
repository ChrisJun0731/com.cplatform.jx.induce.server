package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 *  查询当前播放内容 
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月7日 下午1:59:45
 * <p>
 * Company: 北京宽连十方数字技术有限公司
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
			//内容?
			
			int count = buffer.limit();
			byte[] bytes = new byte[count-buffer.position()];
			buffer.get(bytes);
			setContent(new String(bytes,"utf-8").trim());
		}
		catch (Exception e)
		{
			log.error("解析数据异常",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
    /**开关屏标志 1-表示开屏  2-表示关屏，关屏时以下内容无效*/
    private int switchState;

    /**播放类型标志  1-列表播放 2-紧急播放  3-测试*/
    private int playType;
    
    /**播放列表号 当前播放的列表编号或测试编号*/
    private int listNum;
    
    /**内容头*/
    private String title;
    
    /**当前播放内容*/
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

package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 读取坏点信息 2 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月8日 上午9:55:18
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ReadBadPointTwoMessage extends AbstractMessage {

	static Logger log = Logger.getLogger(ReadBadPointTwoMessage.class);

	/**
	 * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
	 */
	public ReadBadPointTwoMessage(Header header) {
		super(header);
		this.getHeader().setCommand(READ_BAD_POINT_TWO);
//		 this.getHeader().setTotalLength(9);//有消息内容
		// this.getHeader().setAddress(getAddress());
		// this.getHeader().setBegin(MESSAGE_BEGIN);
	}

	/**
	 * 见接口
	 */
	public ByteBuffer byteBufferedMessage(int length) {
		return null;
	}

	/**
	 * 见接口
	 */
	public int byteToMessage(ByteBuffer buffer) {
		if (buffer == null) {
			return MESSAGE_ERROR;
		} else {
			try {
				byte[] bytes =new byte[2];
				buffer.get(bytes);
				setBlockNum(ToolKits.byteToShort(bytes));
				int num = buffer.limit() - buffer.position();
				if(num>0){
				  bytes = new byte[num];
				  buffer.get(bytes);
				  setBadPoint(bytes);
				}
			}
			catch (Exception e) {
				log.error("解析数据异常", e);
				return MESSAGE_ERROR;
			}

			return MESSAGE_SUCCESS;
		}
	}

	/** 块号 */
	private int blockNum;

	/** 坏点信息 */
	private byte[] badPoint;

	public int getBlockNum() {
		return blockNum;
	}

	public void setBlockNum(int blockNum) {
		this.blockNum = blockNum;
	}

	public byte[] getBadPoint() {
		return badPoint;
	}

	public void setBadPoint(byte[] badPoint) {
		this.badPoint = badPoint;
	}

	  @Override
	   	public String toString(){
	   		
	       	StringBuffer buf = new StringBuffer(500);
	       	buf.append("address=").append(getHeader().getAddress()).append(", ");
	   		buf.append("blockNum=").append(blockNum).append(", ");
	   		buf.append("badPoint=").append(badPoint);
	       	return buf.toString();
	   	}
}

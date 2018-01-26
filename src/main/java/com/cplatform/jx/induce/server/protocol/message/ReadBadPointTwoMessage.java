package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ��ȡ������Ϣ 2 ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��8�� ����9:55:18
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ReadBadPointTwoMessage extends AbstractMessage {

	static Logger log = Logger.getLogger(ReadBadPointTwoMessage.class);

	/**
	 * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
	 */
	public ReadBadPointTwoMessage(Header header) {
		super(header);
		this.getHeader().setCommand(READ_BAD_POINT_TWO);
//		 this.getHeader().setTotalLength(9);//����Ϣ����
		// this.getHeader().setAddress(getAddress());
		// this.getHeader().setBegin(MESSAGE_BEGIN);
	}

	/**
	 * ���ӿ�
	 */
	public ByteBuffer byteBufferedMessage(int length) {
		return null;
	}

	/**
	 * ���ӿ�
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
				log.error("���������쳣", e);
				return MESSAGE_ERROR;
			}

			return MESSAGE_SUCCESS;
		}
	}

	/** ��� */
	private int blockNum;

	/** ������Ϣ */
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

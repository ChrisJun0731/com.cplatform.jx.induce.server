package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 读取坏点信息 2 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月8日 上午9:55:02
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class ReadBadPointTwoRespMessage extends AbstractMessage {

	static Logger log = Logger.getLogger(ReadBadPointTwoRespMessage.class);

	public ReadBadPointTwoRespMessage(Header header) {
		super(header);
		this.getHeader().setCommand(READ_BAD_POINT_RESP_TWO);
		this.getHeader().setTotalLength(10);
		this.getHeader().setAddress(getAddress());
		this.getHeader().setBegin(MESSAGE_BEGIN);
	}

	/**
	 * 见接口
	 */
	public ByteBuffer byteBufferedMessage(int length) {

		if (length < 10) // 此消息长度为7个字节
		{
			return null;
		}

		this.getHeader().setAddress(getAddress());
		// 拷贝头部 除 开始符外
		// 转义 0xAA 或 0xCC 或 0xEE
		byte[] headbytes = getHeader().byteMessage(3);
		int len = 3 + 3;
		byte[] conbytes = new byte[len];
		System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

		ToolKits.int2buf(getBlockSize(), conbytes, 3);
		conbytes[conbytes.length - 1] = (byte) getResult();

		byte[] ecsbytes = ToolKits.covertChar(conbytes);
		int ecslength = ecsbytes.length;
		// 转义后 加上 开始结束符
		byte[] bytes = new byte[ecslength + 2];
		bytes[0] = (byte) MESSAGE_BEGIN;
		System.arraycopy(ecsbytes, 0, bytes, 1, ecslength);
		bytes[ecslength + 1] = (byte) MESSAGE_END;

		// 计算CRC16验证码
		ByteBuffer buffer = ByteBuffer.allocate(ecslength + 2 + 2);
		buffer.put(bytes);
		buffer.put(byteCRC16(bytes));
		buffer.flip();
		return buffer;

	}

	/**
	 * 见接口
	 */
	public int byteToMessage(ByteBuffer buffer) {
		if (buffer == null) {
			return MESSAGE_ERROR;
		}
		return MESSAGE_SUCCESS;
	}

	/** 设备地址 */
	private int address;

	/** 块号*/
	private int blockSize;

	/** 回应标志 */
	private int result;

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Override
	public String toString() {

		StringBuffer buf = new StringBuffer(500);
		buf.append("address=").append(getHeader().getAddress()).append(", ");
		buf.append("blockSize=").append(blockSize).append(", ");
		buf.append("result=").append(result);
		return buf.toString();
	}
}

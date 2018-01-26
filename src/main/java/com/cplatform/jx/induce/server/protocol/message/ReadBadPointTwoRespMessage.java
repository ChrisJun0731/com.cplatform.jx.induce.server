package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ��ȡ������Ϣ 2 ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��8�� ����9:55:02
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
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
	 * ���ӿ�
	 */
	public ByteBuffer byteBufferedMessage(int length) {

		if (length < 10) // ����Ϣ����Ϊ7���ֽ�
		{
			return null;
		}

		this.getHeader().setAddress(getAddress());
		// ����ͷ�� �� ��ʼ����
		// ת�� 0xAA �� 0xCC �� 0xEE
		byte[] headbytes = getHeader().byteMessage(3);
		int len = 3 + 3;
		byte[] conbytes = new byte[len];
		System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

		ToolKits.int2buf(getBlockSize(), conbytes, 3);
		conbytes[conbytes.length - 1] = (byte) getResult();

		byte[] ecsbytes = ToolKits.covertChar(conbytes);
		int ecslength = ecsbytes.length;
		// ת��� ���� ��ʼ������
		byte[] bytes = new byte[ecslength + 2];
		bytes[0] = (byte) MESSAGE_BEGIN;
		System.arraycopy(ecsbytes, 0, bytes, 1, ecslength);
		bytes[ecslength + 1] = (byte) MESSAGE_END;

		// ����CRC16��֤��
		ByteBuffer buffer = ByteBuffer.allocate(ecslength + 2 + 2);
		buffer.put(bytes);
		buffer.put(byteCRC16(bytes));
		buffer.flip();
		return buffer;

	}

	/**
	 * ���ӿ�
	 */
	public int byteToMessage(ByteBuffer buffer) {
		if (buffer == null) {
			return MESSAGE_ERROR;
		}
		return MESSAGE_SUCCESS;
	}

	/** �豸��ַ */
	private int address;

	/** ���*/
	private int blockSize;

	/** ��Ӧ��־ */
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

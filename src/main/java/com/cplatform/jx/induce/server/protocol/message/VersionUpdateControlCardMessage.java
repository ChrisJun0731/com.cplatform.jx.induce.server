package com.cplatform.jx.induce.server.protocol.message;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * �Կ��ƿ��ϵĳ���������� ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��16�� ����3:39:06
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class VersionUpdateControlCardMessage extends AbstractMessage {

	/**
	 * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
	 */
	public VersionUpdateControlCardMessage(Header header) {
		super(header);
		this.getHeader().setCommand(UPGRADE_CONTROL_CARD);
		this.getHeader().setTotalLength(11);// ����Ϣ����
		this.getHeader().setAddress(getAddress());
		this.getHeader().setBegin(MESSAGE_BEGIN);
	}

	/**
	 * ���ӿ�
	 */
	public ByteBuffer byteBufferedMessage(int length) {
		if (length < 11) // ����Ϣ����Ϊ11���ֽ�
		{
			return null;
		}

		this.getHeader().setAddress(getAddress());
		// ����ͷ�� �� ��ʼ����
		// ת�� 0xAA �� 0xCC �� 0xEE
		byte[] headbytes = getHeader().byteMessage(3);
		try {
			// �����ļ���
			byte[] filenamebytes = getFileName().getBytes("utf-8");
			int len = 3 + filenamebytes.length;
			byte[] conbytes = new byte[len];
			System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

			System.arraycopy(filenamebytes, 0, conbytes, headbytes.length, filenamebytes.length);

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
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���ӿ�
	 */
	public int byteToMessage(ByteBuffer buffer) {
		if (buffer == null) {
			return MESSAGE_ERROR;
		} else {
			return MESSAGE_SUCCESS;
		}
	}

	/** �豸��ַ */
	private int address;

	/** �����ļ����� */
	private String fileName;

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}

package com.cplatform.jx.induce.server.protocol.message;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * 对控制卡上的程序进行升级 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月16日 下午3:39:06
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class VersionUpdateControlCardMessage extends AbstractMessage {

	/**
	 * 构造器，初始化各变量 初始化各字符串变量目的是防止空指针异常
	 */
	public VersionUpdateControlCardMessage(Header header) {
		super(header);
		this.getHeader().setCommand(UPGRADE_CONTROL_CARD);
		this.getHeader().setTotalLength(11);// 有消息内容
		this.getHeader().setAddress(getAddress());
		this.getHeader().setBegin(MESSAGE_BEGIN);
	}

	/**
	 * 见接口
	 */
	public ByteBuffer byteBufferedMessage(int length) {
		if (length < 11) // 此消息长度为11个字节
		{
			return null;
		}

		this.getHeader().setAddress(getAddress());
		// 拷贝头部 除 开始符外
		// 转义 0xAA 或 0xCC 或 0xEE
		byte[] headbytes = getHeader().byteMessage(3);
		try {
			// 升级文件名
			byte[] filenamebytes = getFileName().getBytes("utf-8");
			int len = 3 + filenamebytes.length;
			byte[] conbytes = new byte[len];
			System.arraycopy(headbytes, 0, conbytes, 0, headbytes.length);

			System.arraycopy(filenamebytes, 0, conbytes, headbytes.length, filenamebytes.length);

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
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 见接口
	 */
	public int byteToMessage(ByteBuffer buffer) {
		if (buffer == null) {
			return MESSAGE_ERROR;
		} else {
			return MESSAGE_SUCCESS;
		}
	}

	/** 设备地址 */
	private int address;

	/** 升级文件名称 */
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

package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import com.cplatform.jx.induce.server.protocol.tools.ToolKits;

/**
 * ��ѯ�汾��Ϣ
 * ���⡢��Ҫ˵��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��2��7�� ����11:04:59
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class QueryVersionMessage extends AbstractMessage {
	 /**
     * ����������ʼ�������� ��ʼ�����ַ�������Ŀ���Ƿ�ֹ��ָ���쳣
     */
    public QueryVersionMessage(Header header)
    {
        super(header);
        this.getHeader().setCommand(QUERY_VERSION);
        this.getHeader().setTotalLength(7);//����Ϣ����
        this.getHeader().setAddress(getAddress());
        this.getHeader().setBegin(MESSAGE_BEGIN);
    }

    /**
     * ���ӿ�
     */
    public ByteBuffer byteBufferedMessage(int length)
    {
    	 if (length < 7) // ����Ϣ����Ϊ7���ֽ�
         {
             return null;
         }
         this.getHeader().setAddress(getAddress());
         //����ͷ�� �� ��ʼ����
         //ת��  0xAA �� 0xCC �� 0xEE
         byte[] headbytes = getHeader().byteMessage(3);
         byte[] ecsbytes= ToolKits.covertChar(headbytes);
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
	
	
	
    public int getAddress() {
    	return address;
    }

	
    public void setAddress(int address) {
    	this.address = address;
    }

}

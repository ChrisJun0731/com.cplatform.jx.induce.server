package com.cplatform.jx.induce.server.protocol.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @Title: ��Ϣ���ӿ�
 * @Description: ������Ϣ���ӿ�
 * @Copyright: Copyright (c) 2006-8-30
 * @Company: ��������ʮ�����ּ������޹�˾
 * @Author: chenwei
 * @Version: 1.0
 */
public interface Message
{
    /**
     * ��ByteBuffer��װ��Ϣ���ݴ˿���ֱ�Ӷ�SocketChannel����
     * @param length, Buffer�Ĵ�С
     * @return ByteBuffer
     */
    public ByteBuffer byteBufferedMessage(int length);
    
    /**
     * ������Ϣ�壨����Ϣͷ��Ϊjava���ݸ�ʽ������ֱ�Ӷ�SocketChannel����
     * @param buffer
     * @return Integer, ErrorCode
     */
    public int byteToMessage(ByteBuffer buffer);
    
    /**
     * ��byte�����װ��Ϣ
     * @param length, ��Ϣ����
     * @return
     */
    public byte[] byteMessage(int length);
    
    /**
     * ������Ϣ�壨����Ϣͷ��Ϊjava���ݸ�ʽ
     * @param bytes, byte���飬��socket���õ�����Ϣ��
     * @return Integer, ErrorCode
     */
    public int byteToMessage(byte[] bytes);
    
    /**
     * �����Ϣ��socket��
     * @param out, socket�����
     * @throws IOException
     */
    public void putMessageOut(OutputStream out) throws IOException;
    
    /**
     * ��socket����ȡ��Ϣ�壨����Ϣͷ��
     * @param in, socket������
     * @return Integer, ErrorCoode
     * @throws IOException
     */
    public int getMessageIn(InputStream in, int position,int length) throws IOException;
}

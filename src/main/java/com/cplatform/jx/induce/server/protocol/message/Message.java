package com.cplatform.jx.induce.server.protocol.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @Title: 消息包接口
 * @Description: 定义消息包接口
 * @Copyright: Copyright (c) 2006-8-30
 * @Company: 北京宽连十方数字技术有限公司
 * @Author: chenwei
 * @Version: 1.0
 */
public interface Message
{
    /**
     * 用ByteBuffer封装信息，据此可以直接对SocketChannel操作
     * @param length, Buffer的大小
     * @return ByteBuffer
     */
    public ByteBuffer byteBufferedMessage(int length);
    
    /**
     * 解析信息体（或信息头）为java数据格式，可以直接对SocketChannel操作
     * @param buffer
     * @return Integer, ErrorCode
     */
    public int byteToMessage(ByteBuffer buffer);
    
    /**
     * 用byte数组封装信息
     * @param length, 信息长度
     * @return
     */
    public byte[] byteMessage(int length);
    
    /**
     * 解析信息体（或信息头）为java数据格式
     * @param bytes, byte数组，从socket流得到的消息体
     * @return Integer, ErrorCode
     */
    public int byteToMessage(byte[] bytes);
    
    /**
     * 输出信息到socket流
     * @param out, socket输出流
     * @throws IOException
     */
    public void putMessageOut(OutputStream out) throws IOException;
    
    /**
     * 从socket流获取消息体（或消息头）
     * @param in, socket输入流
     * @return Integer, ErrorCoode
     * @throws IOException
     */
    public int getMessageIn(InputStream in, int position,int length) throws IOException;
}

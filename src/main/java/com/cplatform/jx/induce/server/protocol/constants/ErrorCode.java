package com.cplatform.jx.induce.server.protocol.constants;

/**
 * 
 * 错误码常量. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年10月31日 下午4:44:24
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public interface ErrorCode
{  
    public static final int SUCCESS = 1; // 成功
    public static final int ERROR = 0; // 失败
    
    public static final int MESSAGE_SUCCESS = 201; // 成功
    public static final int MESSAGE_ERROR = 202; // 失败
    public static final int MESSAGE_BEGIN_ERROR =203;//消息起始错误
    
    public static final int NO_RESP = 100; // 没有收到回包 
    public static final int NO_CONNECTION = 500; // 无法建立连接或连接不存在
    
    public static final int FATAL_TERMINAL_ID = -1000; // 非法终端号码
    public static final int BLACK_TERMINAL_ID = -1001; // 黑终端号码
    public static final int SPCODE_DONT_EXIST= -1002; // 特服号不存在
    public static final int MSG_ISNULL= -1003; // 消息内容为空
}
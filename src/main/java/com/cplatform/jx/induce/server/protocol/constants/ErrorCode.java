package com.cplatform.jx.induce.server.protocol.constants;

/**
 * 
 * �����볣��. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2016��10��31�� ����4:44:24
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public interface ErrorCode
{  
    public static final int SUCCESS = 1; // �ɹ�
    public static final int ERROR = 0; // ʧ��
    
    public static final int MESSAGE_SUCCESS = 201; // �ɹ�
    public static final int MESSAGE_ERROR = 202; // ʧ��
    public static final int MESSAGE_BEGIN_ERROR =203;//��Ϣ��ʼ����
    
    public static final int NO_RESP = 100; // û���յ��ذ� 
    public static final int NO_CONNECTION = 500; // �޷��������ӻ����Ӳ�����
    
    public static final int FATAL_TERMINAL_ID = -1000; // �Ƿ��ն˺���
    public static final int BLACK_TERMINAL_ID = -1001; // ���ն˺���
    public static final int SPCODE_DONT_EXIST= -1002; // �ط��Ų�����
    public static final int MSG_ISNULL= -1003; // ��Ϣ����Ϊ��
}
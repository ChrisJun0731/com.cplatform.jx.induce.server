package com.cplatform.jx.induce.server.protocol.constants;

/**
 * �����Ӧֵ. <br>
 * ����ϸ˵��.CREE
 * <p>
 * Copyright: Copyright (c) 2016��10��31�� ����4:24:23
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public interface CommandID
{
	/* ƽ̨ʹ�õľ����¼��� */
	/**��Ϣת���*/
	public static final int MESSAGE_ESCAPE = 0xEE;
	/**��Ϣ��ʼ��*/
	public static final int MESSAGE_BEGIN = 0xAA;
	/**��Ϣ������*/
	public static final int MESSAGE_END = 0xCC;
	/**����*/
    public static final int ACTIVE_TEST = 0x00;
	/**��ѯ�豸״̬*/
    public static final int QUERY_SCREEN_STATE = 0x01; 
    /**��ѯ�豸״̬Ӧ��*/
    public static final int QUERY_SCREEN_STATE_RESP = 0x02; 
    /**���Կ���*/
    public static final int TEST_SCREEN = 0x03; 
    /**���Կ���Ӧ��*/
    public static final int TEST_SCREEN_RESP = 0x04; 
    /**���ƿ�����*/
    public static final int CONTROL_SCREEN = 0x05; 
    /**���ƿ�����Ӧ��*/
    public static final int CONTROL_SCREEN_RESP = 0x06; 
    /**���ȿ���*/
    public static final int CONTROL_LIGHT = 0x07; 
    /**���ȿ���Ӧ��*/
    public static final int CONTROL_LIGHT_RESP = 0x08; 
    /**��������ʱ�� */
    public static final int SET_DATETIME = 0x09; 
    /**��������ʱ�� Ӧ��*/
    public static final int SET_DATETIME_RESP = 0x0A; 
    /**�豸��λ*/
    public static final int RESET_EQUIPMENT = 0x0D;
    /**�豸��λӦ��*/
    public static final int RESET_EQUIPMENT_RESP = 0x0E;
    /**�ļ�������*/
    public static final int SEND_FILE_NAME = 0x11;
    /**�ļ�������Ӧ��*/
    public static final int SEND_FILE_NAME_RESP = 0x12;
    /**�ļ����ݷ���*/
    public static final int SEND_FILE_CONTENT = 0x13;
    /**�ļ����ݷ���Ӧ��*/
    public static final int SEND_FILE_CONTENT_RESP = 0x14;
    /**�ļ��������*/
    public static final int SEND_FILE_COMPLETE = 0xF9;

    /**���û��������澯���� */
    public static final int SET_CONTROL_PARAM = 0x15;
    /**���û��������澯���� Ӧ��*/
    public static final int SET_CONTROL_PARAM_RESP = 0x16;
    
    /**�������ȿ��Ʋ���*/
    public static final int SET_LIGHT_CONTROL_PARAM  = 0x17;
    /**�������ȿ��Ʋ���Ӧ��*/
    public static final int SET_LIGHT_CONTROL_PARAM_RESP = 0x18;
    
    /**���û������� */
    public static final int SET_BASE_PARAM  = 0x19;
    /**���û������� Ӧ��*/
    public static final int SET_BASE_PARAM_RESP = 0x1A;
    
    /**ָ����ʾ�б�  */
    public static final int SET_DISPLAY_LIST  = 0x1B;
    /**ָ����ʾ�б� Ӧ��*/
    public static final int SET_DISPLAY_LIST_RESP = 0x1C;
    
    /**��������Դ����   */
    public static final int SET_IN_SOURCE_PARAM  = 0x1D;
    /**��������Դ����  Ӧ��*/
    public static final int SET_IN_SOURCE_PARAM_RESP = 0x1E;
    
    /**�ָ��������� */
    public static final int SET_REST_FACTORY  = 0x21;
    /**�ָ���������  Ӧ��*/
    public static final int SET_REST_FACTORY_RESP = 0x22;
    
    /**��ѯ�汾��Ϣ  */
    public static final int QUERY_VERSION = 0x23;
    /**��ѯ�汾��Ϣ   Ӧ��*/
    public static final int QUERY_VERSION_RESP = 0x24;
    
    /**���������Դ����  */
    public static final int CONTROL_SCREEN_POWER = 0x25;
    /**���������Դ����    Ӧ��*/
    public static final int CONTROL_SCREEN_POWER_RESP = 0x26;
    
    /**��ѯ��������   */
    public static final int QUERY_BASE_PARAM = 0x27;
    /**��ѯ��������    Ӧ��*/
    public static final int QUERY_BASE_PARAM_RESP = 0x28;
    
    /**��ѯ��������   */
    public static final int QUERY_CONTROL_PARAM = 0x29;
    /**��ѯ��������    Ӧ��*/
    public static final int QUERY_CONTROL_PARAM_RESP = 0x2A;
    
    /**��ѯ���Ȳ���   */
    public static final int QUERY_LIGHT_PARAM = 0x2B;
    /**��ѯ���Ȳ���    Ӧ��*/
    public static final int QUERY_LIGHT_PARAM_RESP = 0x2C;
    
    /**��ѯ��ǰ��������  */
    public static final int QUERY_DISPLAY_CONTENT = 0x2D;
    /**��ѯ��ǰ��������    Ӧ��*/
    public static final int QUERY_DISPLAY_CONTENT_RESP = 0x2E;
    
    /**�豸�յ���λ��ʱ��ο��Ʋ����б�����   */
    public static final int SET_CMS_DISPLAY_TIME = 0x41;
    /**�豸�յ���λ��ʱ��ο��Ʋ����б�����     Ӧ��*/
    public static final int SET_CMS_DISPLAY_TIME_RESP = 0x42;
    
    /** �豸�յ���λ��ʱ��ο������Ȳ�������    */
    public static final int SET_CMS_LIGHT_TIME = 0x43;
    /** �豸�յ���λ��ʱ��ο������Ȳ�������      Ӧ��*/
    public static final int SET_CMS_LIGHT_TIME_RESP = 0x44;
    
    /**���������Ӽ����Ϣ   */
    public static final int SET_VIRTUAL_CONN_TEST = 0xF3;
    /**���������Ӽ����Ϣ     Ӧ��*/
    public static final int SET_VIRTUAL_CONN_TEST_RESP = 0xF4;
    
    /** �����¶ȹ��߲�������    */
    public static final int SET_HIGH_TEMPERATURE_PARAM = 0xF5;
    /** �����¶ȹ��߲�������      Ӧ��*/
    public static final int SET_HIGH_TEMPERATURE_PARAM_RESP = 0xF6;
    
    /** ���õ�Դ�쳣ʱ��������    */
    public static final int SET_POWER_EXCEPTION_PARAM = 0xF7;
    /** ���õ�Դ�쳣ʱ��������      Ӧ��*/
    public static final int SET_POWER_EXCEPTION_PARAM_RESP = 0xF8;

    /** �����֤��ͨ�Ű�ȫԼ��    */
    public static final int SET_AUTH_SECURITY_PARAM = 0x70;
    /** �����֤��ͨ�Ű�ȫԼ��      Ӧ��*/
    public static final int SET_AUTH_SECURITY_PARAM_RESP = 0x71;

    /**  �ļ�����     */
    public static final int CLEAR_FILE = 0x7C;
    /**  �ļ�����       Ӧ��*/
    public static final int CLEAR_FILE_RESP = 0x7D;
    
    /**  ���ƿ����Ʋ���     */
    public static final int SET_CONTROL_CARD_NAME = 0x7E;
    /**  ���ƿ����Ʋ���      Ӧ��*/
    public static final int SET_CONTROL_CARD_NAME_RESP = 0x7F;

    /**  �����     */
    public static final int DETECTION_POINT = 0x0B;
    /**  �����      Ӧ��*/
    public static final int DETECTION_POINT_RESP = 0x0C;
    
    /** ���õ�����     */
    public static final int SET_DETECTION_POINT_PARAM = 0xE8;
    /** ���õ�����       Ӧ��*/
    public static final int SET_DETECTION_POINT_PARAM_RESP = 0xE9;
    /**��ѯ���õĵ�����*/
    public static final int QUERY_DETECTION_POINT_PARAM = 0xEA;
    /** ��ѯ���õ�����       Ӧ��*/
    public static final int QUERY_DETECTION_POINT_PARAM_RESP = 0xEB;
    
    /**  ��ȡ������Ϣ      */
    public static final int READ_BAD_POINT = 0x36;
    /**  ��ȡ������Ϣ       Ӧ��*/
    public static final int READ_BAD_POINT_RESP = 0x37;
    /**  ��ȡ������Ϣ       Ӧ��*/
    public static final int READ_BAD_POINT_TWO = 0x38;
    /**  ��ȡ������Ϣ       Ӧ��*/
    public static final int READ_BAD_POINT_RESP_TWO = 0x39;
    
    /**  ��ȡ��Ļ��ǰ��ʾ����    */
    public static final int GET_SCREEN_IMG = 0x80;
    /**  ��ȡ��Ļ��ǰ��ʾ����       Ӧ��*/
    public static final int GET_SCREEN_IMG_RESP = 0x81;
    
    /**  ��ȡ��Ļ��������     */
    public static final int GET_SCREEN_LATTICE = 0x82;
    /**  ��ȡ��Ļ��������        Ӧ��*/
    public static final int GET_SCREEN_LATTICE_RESP = 0x83;
    
    /**  ���Ʊ����Դ����     */
    public static final int CONTROL_LOCAL_POWER_SWITCH = 0x84;
    /**  ���Ʊ����Դ����        Ӧ��*/
    public static final int CONTROL_LOCAL_POWER_SWITCH_RESP = 0x85;
    
    /**  ���ƶ๦�ܿ���Դ����     */
    public static final int CONTROL_CARD_POWER_SWITCH = 0x86;
    /**  ���ƶ๦�ܿ���Դ����        Ӧ��*/
    public static final int CONTROL_CARD_POWER_SWITCH_RESP = 0x87;
    
    /**  ����ֲ���������      */
    public static final int UPDATE_LOCAL_SCREEN = 0x88;
    /**  ����ֲ���������        Ӧ��*/
    public static final int UPDATE_LOCAL_SCREEN_RESP = 0x89;
    
    /**�Կ��ƿ��ϵĳ����������*/
    public static final int UPGRADE_CONTROL_CARD = 0x90;
    /**�Կ��ƿ��ϵĳ����������   Ӧ��*/
    public static final int UPGRADE_CONTROL_CARD_RESP = 0x91;
    
    
    /**�����ն˶�ʱ�������Ŀ��Ʋ���      */
    public static final int SET_SCREEN_TIME_SWITCH_PARAM = 0x8A;
    /**�����ն˶�ʱ�������Ŀ��Ʋ��� Ӧ��*/
    public static final int SET_SCREEN_TIME_SWITCH_PARAM_RESP = 0x8B;
    
    
    
}

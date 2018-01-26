package com.cplatform.jx.induce.server.protocol.constants;

/**
 * 命令对应值. <br>
 * 类详细说明.CREE
 * <p>
 * Copyright: Copyright (c) 2016年10月31日 下午4:24:23
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public interface CommandID
{
	/* 平台使用的就以下几类 */
	/**消息转义符*/
	public static final int MESSAGE_ESCAPE = 0xEE;
	/**消息开始符*/
	public static final int MESSAGE_BEGIN = 0xAA;
	/**消息结束符*/
	public static final int MESSAGE_END = 0xCC;
	/**心跳*/
    public static final int ACTIVE_TEST = 0x00;
	/**查询设备状态*/
    public static final int QUERY_SCREEN_STATE = 0x01; 
    /**查询设备状态应答*/
    public static final int QUERY_SCREEN_STATE_RESP = 0x02; 
    /**测试控制*/
    public static final int TEST_SCREEN = 0x03; 
    /**测试控制应答*/
    public static final int TEST_SCREEN_RESP = 0x04; 
    /**控制开关屏*/
    public static final int CONTROL_SCREEN = 0x05; 
    /**控制开关屏应答*/
    public static final int CONTROL_SCREEN_RESP = 0x06; 
    /**亮度控制*/
    public static final int CONTROL_LIGHT = 0x07; 
    /**亮度控制应答*/
    public static final int CONTROL_LIGHT_RESP = 0x08; 
    /**设置日期时间 */
    public static final int SET_DATETIME = 0x09; 
    /**设置日期时间 应答*/
    public static final int SET_DATETIME_RESP = 0x0A; 
    /**设备复位*/
    public static final int RESET_EQUIPMENT = 0x0D;
    /**设备复位应答*/
    public static final int RESET_EQUIPMENT_RESP = 0x0E;
    /**文件名发送*/
    public static final int SEND_FILE_NAME = 0x11;
    /**文件名发送应答*/
    public static final int SEND_FILE_NAME_RESP = 0x12;
    /**文件内容发送*/
    public static final int SEND_FILE_CONTENT = 0x13;
    /**文件内容发送应答*/
    public static final int SEND_FILE_CONTENT_RESP = 0x14;
    /**文件发送完毕*/
    public static final int SEND_FILE_COMPLETE = 0xF9;

    /**设置环境环境告警参数 */
    public static final int SET_CONTROL_PARAM = 0x15;
    /**设置环境环境告警参数 应答*/
    public static final int SET_CONTROL_PARAM_RESP = 0x16;
    
    /**设置亮度控制参数*/
    public static final int SET_LIGHT_CONTROL_PARAM  = 0x17;
    /**设置亮度控制参数应答*/
    public static final int SET_LIGHT_CONTROL_PARAM_RESP = 0x18;
    
    /**设置基本参数 */
    public static final int SET_BASE_PARAM  = 0x19;
    /**设置基本参数 应答*/
    public static final int SET_BASE_PARAM_RESP = 0x1A;
    
    /**指定显示列表  */
    public static final int SET_DISPLAY_LIST  = 0x1B;
    /**指定显示列表 应答*/
    public static final int SET_DISPLAY_LIST_RESP = 0x1C;
    
    /**设置输入源参数   */
    public static final int SET_IN_SOURCE_PARAM  = 0x1D;
    /**设置输入源参数  应答*/
    public static final int SET_IN_SOURCE_PARAM_RESP = 0x1E;
    
    /**恢复出厂设置 */
    public static final int SET_REST_FACTORY  = 0x21;
    /**恢复出厂设置  应答*/
    public static final int SET_REST_FACTORY_RESP = 0x22;
    
    /**查询版本信息  */
    public static final int QUERY_VERSION = 0x23;
    /**查询版本信息   应答*/
    public static final int QUERY_VERSION_RESP = 0x24;
    
    /**控制屏体电源开关  */
    public static final int CONTROL_SCREEN_POWER = 0x25;
    /**控制屏体电源开关    应答*/
    public static final int CONTROL_SCREEN_POWER_RESP = 0x26;
    
    /**查询基本参数   */
    public static final int QUERY_BASE_PARAM = 0x27;
    /**查询基本参数    应答*/
    public static final int QUERY_BASE_PARAM_RESP = 0x28;
    
    /**查询环境参数   */
    public static final int QUERY_CONTROL_PARAM = 0x29;
    /**查询环境参数    应答*/
    public static final int QUERY_CONTROL_PARAM_RESP = 0x2A;
    
    /**查询亮度参数   */
    public static final int QUERY_LIGHT_PARAM = 0x2B;
    /**查询亮度参数    应答*/
    public static final int QUERY_LIGHT_PARAM_RESP = 0x2C;
    
    /**查询当前播放内容  */
    public static final int QUERY_DISPLAY_CONTENT = 0x2D;
    /**查询当前播放内容    应答*/
    public static final int QUERY_DISPLAY_CONTENT_RESP = 0x2E;
    
    /**设备收到上位机时间段控制播放列表命令   */
    public static final int SET_CMS_DISPLAY_TIME = 0x41;
    /**设备收到上位机时间段控制播放列表命令     应答*/
    public static final int SET_CMS_DISPLAY_TIME_RESP = 0x42;
    
    /** 设备收到上位机时间段控制亮度参数命令    */
    public static final int SET_CMS_LIGHT_TIME = 0x43;
    /** 设备收到上位机时间段控制亮度参数命令      应答*/
    public static final int SET_CMS_LIGHT_TIME_RESP = 0x44;
    
    /**设置虚连接检测信息   */
    public static final int SET_VIRTUAL_CONN_TEST = 0xF3;
    /**设置虚连接检测信息     应答*/
    public static final int SET_VIRTUAL_CONN_TEST_RESP = 0xF4;
    
    /** 设置温度过高操作参数    */
    public static final int SET_HIGH_TEMPERATURE_PARAM = 0xF5;
    /** 设置温度过高操作参数      应答*/
    public static final int SET_HIGH_TEMPERATURE_PARAM_RESP = 0xF6;
    
    /** 设置电源异常时操作参数    */
    public static final int SET_POWER_EXCEPTION_PARAM = 0xF7;
    /** 设置电源异常时操作参数      应答*/
    public static final int SET_POWER_EXCEPTION_PARAM_RESP = 0xF8;

    /** 身份验证和通信安全约定    */
    public static final int SET_AUTH_SECURITY_PARAM = 0x70;
    /** 身份验证和通信安全约定      应答*/
    public static final int SET_AUTH_SECURITY_PARAM_RESP = 0x71;

    /**  文件清理     */
    public static final int CLEAR_FILE = 0x7C;
    /**  文件清理       应答*/
    public static final int CLEAR_FILE_RESP = 0x7D;
    
    /**  控制卡名称操作     */
    public static final int SET_CONTROL_CARD_NAME = 0x7E;
    /**  控制卡名称操作      应答*/
    public static final int SET_CONTROL_CARD_NAME_RESP = 0x7F;

    /**  点检检测     */
    public static final int DETECTION_POINT = 0x0B;
    /**  点检检测      应答*/
    public static final int DETECTION_POINT_RESP = 0x0C;
    
    /** 设置点检参数     */
    public static final int SET_DETECTION_POINT_PARAM = 0xE8;
    /** 设置点检参数       应答*/
    public static final int SET_DETECTION_POINT_PARAM_RESP = 0xE9;
    /**查询设置的点检参数*/
    public static final int QUERY_DETECTION_POINT_PARAM = 0xEA;
    /** 查询设置点检参数       应答*/
    public static final int QUERY_DETECTION_POINT_PARAM_RESP = 0xEB;
    
    /**  读取坏点信息      */
    public static final int READ_BAD_POINT = 0x36;
    /**  读取坏点信息       应答*/
    public static final int READ_BAD_POINT_RESP = 0x37;
    /**  读取坏点信息       应答*/
    public static final int READ_BAD_POINT_TWO = 0x38;
    /**  读取坏点信息       应答*/
    public static final int READ_BAD_POINT_RESP_TWO = 0x39;
    
    /**  获取屏幕当前显示内容    */
    public static final int GET_SCREEN_IMG = 0x80;
    /**  获取屏幕当前显示内容       应答*/
    public static final int GET_SCREEN_IMG_RESP = 0x81;
    
    /**  获取屏幕点阵数据     */
    public static final int GET_SCREEN_LATTICE = 0x82;
    /**  获取屏幕点阵数据        应答*/
    public static final int GET_SCREEN_LATTICE_RESP = 0x83;
    
    /**  控制本板电源开关     */
    public static final int CONTROL_LOCAL_POWER_SWITCH = 0x84;
    /**  控制本板电源开关        应答*/
    public static final int CONTROL_LOCAL_POWER_SWITCH_RESP = 0x85;
    
    /**  控制多功能卡电源开关     */
    public static final int CONTROL_CARD_POWER_SWITCH = 0x86;
    /**  控制多功能卡电源开关        应答*/
    public static final int CONTROL_CARD_POWER_SWITCH_RESP = 0x87;
    
    /**  屏体局部更新内容      */
    public static final int UPDATE_LOCAL_SCREEN = 0x88;
    /**  屏体局部更新内容        应答*/
    public static final int UPDATE_LOCAL_SCREEN_RESP = 0x89;
    
    /**对控制卡上的程序进行升级*/
    public static final int UPGRADE_CONTROL_CARD = 0x90;
    /**对控制卡上的程序进行升级   应答*/
    public static final int UPGRADE_CONTROL_CARD_RESP = 0x91;
    
    
    /**设置终端定时开关屏的控制参数      */
    public static final int SET_SCREEN_TIME_SWITCH_PARAM = 0x8A;
    /**设置终端定时开关屏的控制参数 应答*/
    public static final int SET_SCREEN_TIME_SWITCH_PARAM_RESP = 0x8B;
    
    
    
}

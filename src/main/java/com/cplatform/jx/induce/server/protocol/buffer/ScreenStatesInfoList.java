package com.cplatform.jx.induce.server.protocol.buffer;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.message.info.ScreenStatesInfo;



/**
 * @Title: 上行信息队列
 * @Description: 为单例模式，方便各线程间共享
 * @Copyright: Copyright (c) 2006-8-30
 * @Company: 北京宽连十方数字技术有限公司
 * @Author: chenwei
 * @Version: 1.0
 */
public class ScreenStatesInfoList
{
	static Logger log = Logger.getLogger(ScreenStatesInfoList.class);
    /**
     * 线程同步控制确保模块仅有一个实例
     * @return MoInfoList
     */
    static synchronized public ScreenStatesInfoList getInstance()
    {
        if (instance == null)
        {
            instance = new ScreenStatesInfoList();
        }
        return instance;
    }

    /**
     * 构造器，默认上行短信信息列表长度为200
     *
     */
    @SuppressWarnings("rawtypes")
    private ScreenStatesInfoList()
    {
    	vList = new Vector(200);
    }

    /**
     * 向状态报告信息列表添加消息
     * @param info
     */
	@SuppressWarnings("unchecked")
    public synchronized void add(ScreenStatesInfo info)
	{
		vList.addElement(info);
	}

	/**
	 * 返回并删除状态报告信息列表起始处消息，若消息队列为空，返回空
	 * 
	 * @return MoMessage
	 */
	public synchronized ScreenStatesInfo remove()
	{
		if (vList.size() == 0)
		{
			return null;
		}
		return (ScreenStatesInfo) vList.remove(0);
	}

	/**
	 * 返回状态报告信息列表长度
	 * 
	 * @return Integer
	 */
	public int getSize()
	{
		return vList.size();
	}

    static private ScreenStatesInfoList instance; // 上行短信信息列表实例
    
    @SuppressWarnings("rawtypes")
    private Vector vList;//集合对象
}

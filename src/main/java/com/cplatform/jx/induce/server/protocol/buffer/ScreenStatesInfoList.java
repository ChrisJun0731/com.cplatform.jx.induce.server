package com.cplatform.jx.induce.server.protocol.buffer;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.cplatform.jx.induce.server.protocol.message.info.ScreenStatesInfo;



/**
 * @Title: ������Ϣ����
 * @Description: Ϊ����ģʽ��������̼߳乲��
 * @Copyright: Copyright (c) 2006-8-30
 * @Company: ��������ʮ�����ּ������޹�˾
 * @Author: chenwei
 * @Version: 1.0
 */
public class ScreenStatesInfoList
{
	static Logger log = Logger.getLogger(ScreenStatesInfoList.class);
    /**
     * �߳�ͬ������ȷ��ģ�����һ��ʵ��
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
     * ��������Ĭ�����ж�����Ϣ�б���Ϊ200
     *
     */
    @SuppressWarnings("rawtypes")
    private ScreenStatesInfoList()
    {
    	vList = new Vector(200);
    }

    /**
     * ��״̬������Ϣ�б������Ϣ
     * @param info
     */
	@SuppressWarnings("unchecked")
    public synchronized void add(ScreenStatesInfo info)
	{
		vList.addElement(info);
	}

	/**
	 * ���ز�ɾ��״̬������Ϣ�б���ʼ����Ϣ������Ϣ����Ϊ�գ����ؿ�
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
	 * ����״̬������Ϣ�б���
	 * 
	 * @return Integer
	 */
	public int getSize()
	{
		return vList.size();
	}

    static private ScreenStatesInfoList instance; // ���ж�����Ϣ�б�ʵ��
    
    @SuppressWarnings("rawtypes")
    private Vector vList;//���϶���
}

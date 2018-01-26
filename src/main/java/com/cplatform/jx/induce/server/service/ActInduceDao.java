package com.cplatform.jx.induce.server.service;

import java.util.List;

import com.cplatform.jx.induce.DeviceInfo;
import com.cplatform.jx.induce.DevicePresetInfo;
import com.cplatform.jx.induce.PlayListTask;
import com.cplatform.jx.induce.server.protocol.dirty_words_filter.DirtyWordsBuffer;

/**
 * 
 * ���ݿ�����ӿ�. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��3��3�� ����11:01:15
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public interface ActInduceDao {

	/**
	 * ����������
	 * @return
	 * @throws Exception
	 */
	DirtyWordsBuffer loadDirtyWordsBuffer() throws Exception;
	
	/**
	 * ��ȡ������ �����б�����ʵ��
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	PlayListTask getPushPlayListTask(long taskId) throws Exception;
	
	/**
	 * ��ѯtaskId�Ƿ�ʧЧ
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	 Boolean isInvalid(int taskId) throws Exception;
	
	/***
	 *  ��ȡԤ������
	 * @param devicecode
	 * @return
	 * @throws Exception
	 */
	PlayListTask getDevicePresetInfo(String devicecode) throws Exception;
	
	/**
	 * ��ȡ������������Ĳ����б�����
	 * @return
	 * @throws Exception
	 */
	List<PlayListTask> getPushPlayListTaskId(String deviceCode) throws Exception;
	
	
	/**
	 * ������������״̬
	 * @param taskId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	Boolean updatePushPlayListTask(long taskId,int status) throws Exception;
	
	/**
	 * ͨ����Ż�ȡ�豸��Ϣ
	 * @param code
	 * @return
	 * @throws Exception
	 */
	DeviceInfo getDeviceInfo(String code) throws Exception;
	
	/**
	 * ��ȡ��������״̬����
	 * @return
	 * @throws Exception
	 */
	List<DeviceInfo> getDeviceList() throws Exception;
}

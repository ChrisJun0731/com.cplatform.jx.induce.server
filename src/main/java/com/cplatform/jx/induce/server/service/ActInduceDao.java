package com.cplatform.jx.induce.server.service;

import java.util.List;

import com.cplatform.jx.induce.DeviceInfo;
import com.cplatform.jx.induce.DevicePresetInfo;
import com.cplatform.jx.induce.PlayListTask;
import com.cplatform.jx.induce.server.protocol.dirty_words_filter.DirtyWordsBuffer;

/**
 * 
 * 数据库操作接口. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月3日 上午11:01:15
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public interface ActInduceDao {

	/**
	 * 加载敏感字
	 * @return
	 * @throws Exception
	 */
	DirtyWordsBuffer loadDirtyWordsBuffer() throws Exception;
	
	/**
	 * 获取待处理 播放列表任务实体
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	PlayListTask getPushPlayListTask(long taskId) throws Exception;
	
	/**
	 * 查询taskId是否失效
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	 Boolean isInvalid(int taskId) throws Exception;
	
	/***
	 *  获取预置内容
	 * @param devicecode
	 * @return
	 * @throws Exception
	 */
	PlayListTask getDevicePresetInfo(String devicecode) throws Exception;
	
	/**
	 * 获取符合条件处理的播放列表任务
	 * @return
	 * @throws Exception
	 */
	List<PlayListTask> getPushPlayListTaskId(String deviceCode) throws Exception;
	
	
	/**
	 * 更新任务处理结果状态
	 * @param taskId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	Boolean updatePushPlayListTask(long taskId,int status) throws Exception;
	
	/**
	 * 通过编号获取设备信息
	 * @param code
	 * @return
	 * @throws Exception
	 */
	DeviceInfo getDeviceInfo(String code) throws Exception;
	
	/**
	 * 获取所有连接状态的屏
	 * @return
	 * @throws Exception
	 */
	List<DeviceInfo> getDeviceList() throws Exception;
}

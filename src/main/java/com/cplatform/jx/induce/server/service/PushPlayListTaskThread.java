package com.cplatform.jx.induce.server.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cplatform.jx.induce.DeviceInfo;
/**
 * 
 * 定时下发播放内容. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月16日 上午9:42:49
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Component
public class PushPlayListTaskThread extends Thread {

	/** THREAD_SLEEP_TIME */
	private static final long THREAD_SLEEP_TIME = 100 * 2;

	@Autowired
	private PushPlayListTaskService pushPlayListTaskService;
	
	@Autowired
	private ActInduceDao dao;

	/** lastScanTime */
	private long lastScanTime;

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	/** 线程停止标记 */
	private boolean shutdownTag = false;

	/** sysConfig */
	@Resource(name = "sysConfig")
	private PropertiesConfiguration sysConfig;

	/**
	 * 
	 */
	@PreDestroy
	private void destory() {
		shutdownTag = true;
	}

	@PostConstruct
	private void init() throws Exception {
		logger.info("定时推送线程");
		this.setName("PushPlayListTaskThread");
		this.start();
	}

	@Override
	public void run() {
		boolean needSleep = true;
		while (shutdownTag == false) {
			try {
				if (needSleep) {
					sleep(THREAD_SLEEP_TIME);
					needSleep = false;
					continue;
				}
				// 判断是否需要启动超时检测
				boolean enable = sysConfig.getBoolean("push_monitor.enable", false);
				if (enable == false) {
					needSleep = true;
					continue;
				}
				// 判断超时检测时间
				long period = sysConfig.getLong("push_monitor.period", 1000 * 60 * 1);
				if (lastScanTime + period > System.currentTimeMillis()) {
					needSleep = true;
					continue;
				}
				// 查询所有设备未删除，已连接的屏
				List<DeviceInfo> actOrderIds = dao.getDeviceList();
				//
				if (actOrderIds==null||actOrderIds.isEmpty()) {
					lastScanTime = System.currentTimeMillis();
					// 设置下一次检查时间
					needSleep = true;
					continue;
				} else {
					// 处理超时订单
					for (DeviceInfo actOrderId : actOrderIds) {
						logger.info("遍历设备"+actOrderId.toString());
						if(StringUtils.equals(actOrderId.getStatus(), "1")){
							
							pushPlayListTaskService.process(actOrderId);
						}else{
							logger.info("设备:"+actOrderId.getDeviceCode()+"不在线，不处理消息");
						}
					}
					
					lastScanTime = System.currentTimeMillis();
					// 设置下一次检查时间
					needSleep = true;
				}
			}
			catch (Exception ex) {
				logger.error("定时推送内容异常", ex);
				needSleep = true;
			}
		}
	}

}
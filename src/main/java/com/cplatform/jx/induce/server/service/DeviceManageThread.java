package com.cplatform.jx.induce.server.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cplatform.jx.induce.server.protocol.net.SocketConnectionPool;



/**
 * 链接活动管理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月3日 下午1:44:16
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Component
public class DeviceManageThread extends Thread{

	/** Logger. */
	private Logger logger = Logger.getLogger("DeviceManageThread");
	
	@Autowired
	private SocketConnectionPool socketConnPool;
	
	/** sysConfig */
	@Resource(name = "sysConfig")
	private PropertiesConfiguration sysConfig;
	
	/**
	 * 构造器
	 */
	public DeviceManageThread() {
		setName("DeviceManageThread");
	}
	
	/**
	 * 初始化
	 * 
	 * @throws Exception
	 *             异常
	 */
	@PostConstruct
	private void init() throws Exception {
		start();
	}
	
	@Override
	public void run() {
		logger.info(getName() + "启动");
        int ActiveTime = sysConfig.getInt("active.time",30000);
		while (true) {
			try {
				 socketConnPool.refreshConnections();

				 Thread.sleep(ActiveTime);
				
			}
			catch (Exception ex) {
				logger.error("处理异常", ex);
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {

				}
			}
		}
	}
}

package com.cplatform.jx.induce.server.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cplatform.jx.induce.server.protocol.net.SocketConnectionPool;



/**
 * ���ӻ����. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��3��3�� ����1:44:16
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
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
	 * ������
	 */
	public DeviceManageThread() {
		setName("DeviceManageThread");
	}
	
	/**
	 * ��ʼ��
	 * 
	 * @throws Exception
	 *             �쳣
	 */
	@PostConstruct
	private void init() throws Exception {
		start();
	}
	
	@Override
	public void run() {
		logger.info(getName() + "����");
        int ActiveTime = sysConfig.getInt("active.time",30000);
		while (true) {
			try {
				 socketConnPool.refreshConnections();

				 Thread.sleep(ActiveTime);
				
			}
			catch (Exception ex) {
				logger.error("�����쳣", ex);
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {

				}
			}
		}
	}
}

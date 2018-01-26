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
 * ��ʱ�·���������. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��3��16�� ����9:42:49
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
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

	/** ��־��¼�� */
	private Logger logger = Logger.getLogger(getClass());

	/** �߳�ֹͣ��� */
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
		logger.info("��ʱ�����߳�");
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
				// �ж��Ƿ���Ҫ������ʱ���
				boolean enable = sysConfig.getBoolean("push_monitor.enable", false);
				if (enable == false) {
					needSleep = true;
					continue;
				}
				// �жϳ�ʱ���ʱ��
				long period = sysConfig.getLong("push_monitor.period", 1000 * 60 * 1);
				if (lastScanTime + period > System.currentTimeMillis()) {
					needSleep = true;
					continue;
				}
				// ��ѯ�����豸δɾ���������ӵ���
				List<DeviceInfo> actOrderIds = dao.getDeviceList();
				//
				if (actOrderIds==null||actOrderIds.isEmpty()) {
					lastScanTime = System.currentTimeMillis();
					// ������һ�μ��ʱ��
					needSleep = true;
					continue;
				} else {
					// ����ʱ����
					for (DeviceInfo actOrderId : actOrderIds) {
						logger.info("�����豸"+actOrderId.toString());
						if(StringUtils.equals(actOrderId.getStatus(), "1")){
							
							pushPlayListTaskService.process(actOrderId);
						}else{
							logger.info("�豸:"+actOrderId.getDeviceCode()+"�����ߣ���������Ϣ");
						}
					}
					
					lastScanTime = System.currentTimeMillis();
					// ������һ�μ��ʱ��
					needSleep = true;
				}
			}
			catch (Exception ex) {
				logger.error("��ʱ���������쳣", ex);
				needSleep = true;
			}
		}
	}

}
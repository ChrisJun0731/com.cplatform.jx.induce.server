package com.cplatform.jx.induce.server;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.jetty.server.Server;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * �¼�����������.
 * <p>
 * Copyright: Copyright (c) 2013-1-4 ����3:16:43
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * 
 * @author chengfan@c-platform.com
 * @version 1.0.0
 */
public class MainEntry {

	/** LOG4J_RELOAD_DELAY */
	private static final long LOG4J_RELOAD_DELAY = 1000 * 5;

	/**
	 * main����.
	 * 
	 * @param args
	 *            ��������
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String... args) throws Exception {
		Logger logger = null;
		try {
			// ��ʼ��Logger
			DOMConfigurator.configureAndWatch("./config/log4j.xml", LOG4J_RELOAD_DELAY);
			logger = Logger.getLogger(MainEntry.class);

			// ��ʼ��ApplicationContext
			Resource resource = new FileSystemResource("./config/application.xml");
			@SuppressWarnings("resource")
			AbstractApplicationContext appContext = new GenericXmlApplicationContext(resource);
			appContext.registerShutdownHook();

			// ����JettyURL����ΪGB18030
			System.setProperty("org.eclipse.jetty.util.URI.charset", "GB18030");
			logger.info("��ʼ��������");
			// ��ʼ��HttpServer
			Server server = appContext.getBean("server", Server.class);
			logger.info("�����ʼ���ɹ�");
			// ����HttpServer
			server.start();
			logger.info("���������ɹ�");

			// �����ɹ�
			logger.info("���������ɹ�...");
		}
		catch (Exception ex) {
			// CHECKSTYLE:OFF
			if (logger != null) {
				logger.error("��������ʧ��..." + ex, ex);
				LogManager.shutdown();
			} else {
				System.out.println("��������ʧ��");
				ex.printStackTrace(System.out);
			}
			System.exit(-1);
			// CHECKSTYLE:ON
			throw ex;
		}

	}

	/** ���캯�������أ� */
	private MainEntry() {
	}
}

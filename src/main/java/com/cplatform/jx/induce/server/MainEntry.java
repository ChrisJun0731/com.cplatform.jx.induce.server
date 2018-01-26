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
 * 事件中心启动类.
 * <p>
 * Copyright: Copyright (c) 2013-1-4 下午3:16:43
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author chengfan@c-platform.com
 * @version 1.0.0
 */
public class MainEntry {

	/** LOG4J_RELOAD_DELAY */
	private static final long LOG4J_RELOAD_DELAY = 1000 * 5;

	/**
	 * main函数.
	 * 
	 * @param args
	 *            启动参数
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String... args) throws Exception {
		Logger logger = null;
		try {
			// 初始化Logger
			DOMConfigurator.configureAndWatch("./config/log4j.xml", LOG4J_RELOAD_DELAY);
			logger = Logger.getLogger(MainEntry.class);

			// 初始化ApplicationContext
			Resource resource = new FileSystemResource("./config/application.xml");
			@SuppressWarnings("resource")
			AbstractApplicationContext appContext = new GenericXmlApplicationContext(resource);
			appContext.registerShutdownHook();

			// 设置JettyURL编码为GB18030
			System.setProperty("org.eclipse.jetty.util.URI.charset", "GB18030");
			logger.info("开始启动服务");
			// 初始化HttpServer
			Server server = appContext.getBean("server", Server.class);
			logger.info("服务初始化成功");
			// 启动HttpServer
			server.start();
			logger.info("服务启动成功");

			// 启动成功
			logger.info("程序启动成功...");
		}
		catch (Exception ex) {
			// CHECKSTYLE:OFF
			if (logger != null) {
				logger.error("程序启动失败..." + ex, ex);
				LogManager.shutdown();
			} else {
				System.out.println("程序启动失败");
				ex.printStackTrace(System.out);
			}
			System.exit(-1);
			// CHECKSTYLE:ON
			throw ex;
		}

	}

	/** 构造函数（隐藏） */
	private MainEntry() {
	}
}

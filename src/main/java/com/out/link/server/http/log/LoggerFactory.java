package com.out.link.server.http.log;

import org.apache.log4j.Logger;

/**
 * 全局日志工厂
 */
public class LoggerFactory {

	private static final String SERVER_INFO = "serverInfo";
	private static final String SERVER_ERROR = "serverError";

	public static Logger getServerInfoLogger(Class<?> clazz) {
		return Logger.getLogger(SERVER_INFO);
	}

	public static Logger getServerErrorLogger(Class<?> clazz) {
		return Logger.getLogger(SERVER_ERROR);
	}
	
}

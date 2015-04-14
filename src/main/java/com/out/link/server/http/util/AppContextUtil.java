package com.out.link.server.http.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppContextUtil implements ApplicationContextAware {

	private static ApplicationContext ctx = null;
	
	public static Object getBean(String beanName) {
		return ctx.getBean(beanName);
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		ctx = arg0;
	}
}

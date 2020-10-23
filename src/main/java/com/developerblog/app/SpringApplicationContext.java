package com.developerblog.app;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.developerblog.app.services.UserService;

public class SpringApplicationContext implements ApplicationContextAware{
	
	private static ApplicationContext CONTEXT;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CONTEXT = applicationContext; 		
	} 
	
	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);	
	}

}

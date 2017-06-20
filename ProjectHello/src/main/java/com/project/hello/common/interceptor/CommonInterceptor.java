package com.project.hello.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CommonInterceptor extends HandlerInterceptorAdapter{
	private static final Logger Logger = LoggerFactory.getLogger(CommonInterceptor.class);
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Logger.info("============= START =============");
		System.out.println(request.getRequestURI()); 
		System.out.println(request.getRemoteHost());
		return super.preHandle(request, response, handler);
	}
}
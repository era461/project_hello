package com.project.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class Test {
	@Autowired DefaultListableBeanFactory df;
	
	@org.junit.Test
	public void beans(){
		for(String name : df.getBeanDefinitionNames()){
			System.out.println(name);
		}
	}
}
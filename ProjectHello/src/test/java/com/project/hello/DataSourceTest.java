package com.project.hello;

import java.sql.Connection;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/resources/spring/context-*.xml"})
public class DataSourceTest {
	@Inject
	private DataSource ds;
	
	@Test
	public void testConnection() throws Exception{
		try{
			Connection con = ds.getConnection();
			System.out.println("연결 성공[" + con + "]");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
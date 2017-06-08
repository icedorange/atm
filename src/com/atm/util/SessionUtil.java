package com.atm.util;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SessionUtil {

	// 定义构造函数为private，防止此对象多次创建
	private SessionUtil() {

	}
	
	//饿汉式
	private static final SessionUtil se = new SessionUtil();  
	
	public static SessionUtil getInstance(){
		return se;
		
	}
	public static SqlSession getSession(boolean autoCommit) {
		SqlSessionFactory sqlSessionFactory = null;
		// 从 XML 中构建 SqlSessionFactory
		InputStream inputStream;
		try {
			String resource = "com/atm/mybatis-config.xml";
			inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			// 从 SqlSessionFactory 中获取 SqlSession
			return sqlSessionFactory.openSession(autoCommit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

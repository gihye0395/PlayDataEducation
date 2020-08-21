package com.test.utility;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;



import com.test.domain.MyUser;

@Repository
public class DBUtility {
	@Autowired
	public static MongoOperations mongoOperations;
	public static MongoOperations getCollection() {
		MongoTemplate mt = null;
		try {
			mt = new SpringMongoConfig().mongoTemplate();
			//mt.createCollection(MyUser.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mt;
		
		
		
	}//getCollection()
	
	
	private static Connection connection = null;
	public static Connection getConnection() {
		if(connection != null)	return connection;
		else {
			try {
				Properties prop = new Properties();
				InputStream inputStream = DBUtility.class.getClassLoader().getResourceAsStream("/config.properties");
				prop.load(inputStream);
				String driver = prop.getProperty("driver");
				String url = prop.getProperty("url");
				String user = prop.getProperty("user");
				String password = prop.getProperty("password");
				Class.forName(driver);
				connection = DriverManager.getConnection(url,user, password);
				System.out.println("[ Connection OK ]");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return connection;
		}
	}//getConnection()
	
}

package com.revature.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConnectionUtil {
	private static Logger logBot=Logger.getLogger(ConnectionUtil.class);	
	private static Connection conn=null;
	private ConnectionUtil() {}
	
	public static String getSchema() {	
		try {
			Properties prop=new Properties();		
			prop.load(new FileReader( "src/main/resources/config.properties"));			
			String schema=prop.getProperty("schema");
			return schema;
		} catch (FileNotFoundException e) {
			logBot.error("No file exists for the properties");
			e.printStackTrace();
		} catch (IOException e) {
			logBot.error("Can't read from the properties file");
			e.printStackTrace();
		}
		return "project_onedead";
	}
	
	
	public static Connection getConnection() {		
		try {
			if(conn!=null && !conn.isClosed()) {
				return conn;
			}
		} catch (SQLException e) {
			logBot.error("The connection has an error");
			e.printStackTrace();
		}		
		try {
			Properties prop=new Properties();		
			prop.load(new FileReader( "src/main/resources/config.properties"));			
			String url=prop.getProperty("url");
			String userName=prop.getProperty("username");
			String password=prop.getProperty("password");	
			conn = DriverManager.getConnection(url, userName, password);
		} catch (FileNotFoundException e) {
			logBot.error("No file exists for the properties");
			e.printStackTrace();
		} catch (IOException e) {
			logBot.error("Can't read from the properties file");
			e.printStackTrace();
		} catch (SQLException e) {
			logBot.error("Unable to connect to the database");
			e.printStackTrace();
		}		
		return conn;
	}

	public static void main(String[] args) {
		System.out.println(getSchema());		
		Connection conn =getConnection();
		System.out.println("Done");
		
		
	}
	
}

package com.revature.util;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionUtil {
	private static Logger logBot=Logger.getLogger(ConnectionUtil.class);
	private static ComboPooledDataSource cpds = new ComboPooledDataSource();
	private static boolean connected = false;
	private static Connection conn=null;
	private ConnectionUtil() {}

	/**
	 * This gets the schema set in the config.properties
	 *
	 * <pre>
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public static String getSchema() {
		try {
			Properties prop=new Properties();
			prop.load(new FileReader( "src/main/resources/config.properties"));
			String schema = prop.getProperty("schema");
			return (schema != null) ? schema : "public";
		} catch (FileNotFoundException e) {
			logBot.error("No file exists for the properties");
			e.printStackTrace();
		} catch (IOException e) {
			logBot.error("Can't read from the properties file");
			e.printStackTrace();
		}
		return "public";
	}

	/**
	 * This gets the management set in the config.properties
	 *
	 * <pre>
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public static String getManagement() {
		try {
			Properties prop=new Properties();
			prop.load(new FileReader( "src/main/resources/config.properties"));
			return prop.getProperty("SchemaManagement") != null ? prop.getProperty("SchemaManagement") : "update";
		} catch (FileNotFoundException e) {
			logBot.error("No file exists for the properties");
			e.printStackTrace();
		} catch (IOException e) {
			logBot.error("Can't read from the properties file");
			e.printStackTrace();
		}
		return "update";
	}

	static {
		try {
			Properties prop=new Properties();
			prop.load(new FileReader( "src/main/resources/config.properties"));
			String url=prop.getProperty("url");
			String userName=prop.getProperty("username");
			String password=prop.getProperty("password");

			cpds.setDriverClass("org.postgresql.Driver"); // loads the jdbc driver
			cpds.setJdbcUrl(url);
			cpds.setUser(userName);
			cpds.setPassword(password);
			cpds.setMaxPoolSize(10);
			cpds.setMinPoolSize(1);
		} catch (FileNotFoundException e) {
			logBot.error("No file exists for the properties");
			e.printStackTrace();
		} catch (IOException e) {
			logBot.error("Can't read from the properties file");
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			logBot.error("PropertyVetoException e");
			e.printStackTrace();
		}
	}

	/**
	 * This changes the max pool size
	 *
	 * <pre>
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public static void changeMaxPoolSize(int size) {
		cpds.setMaxPoolSize(size);
	}

	/**
	 * This changes the min pool size
	 *
	 * <pre>
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public static void changeMinPoolSize(int size) {
		cpds.setMinPoolSize(size);
	}

	/**
	 * This changes the driver class
	 *
	 * <pre>
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public static void changeDriverClass(String driver) throws PropertyVetoException {
		cpds.setDriverClass(driver);
	}

	/**
	 * This gets a connection from a connection pool
	 *
	 * <pre>
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public static Connection getConnection() {
		/*
		 * try { if ((conn != null) && !conn.isClosed()) { return conn; } } catch
		 * (SQLException e) { logBot.error("The connection has an error");
		 * e.printStackTrace(); }
		 */
		try {
			conn = cpds.getConnection();
		} catch (SQLException e) {
			logBot.error("Unable to connect to the database");
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * This gets only a single connection
	 *
	 * <pre>
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public static Connection getSingleConnection() {
		try {
			if ((conn != null) && !conn.isClosed()) {
				return conn;
			}
		} catch (SQLException e) {
			logBot.error("The connection has an error");
			e.printStackTrace();
		}
		try {
			Properties prop = new Properties();
			prop.load(new FileReader("src/main/resources/config.properties"));
			String url = prop.getProperty("url");
			String userName = prop.getProperty("username");
			String password = prop.getProperty("password");
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

}

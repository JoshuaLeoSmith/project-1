package com.revature.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.revature.util.ConnectionUtil;


public class TestDao {
	
	private static Logger logger = Logger.getLogger(TestDao.class);
	public Connection conn;
	
	public TestDao() {
		this.conn = ConnectionUtil.getConnection();
	}
	
	public int insert(LinkedHashMap<String, Object> colNameToValue, String tableName, String pkName, boolean save) {
		
		try{
			
			String schema = ConnectionUtil.getSchema();
			
			
			String values = "(";
			String colNames = "(";
			
			
			for(String s : colNameToValue.keySet()) {
				
				colNames = colNames + s + ",";
				values = values + "'" + String.valueOf(colNameToValue.get(s)) + "',";
				
			}
			
			colNames = colNames.substring(0, colNames.length()-1) + ")";
			values = values.substring(0, values.length()-1) + ")";
			
			
			
			
			String sql = "INSERT INTO " + schema + "." + tableName + " " + colNames + " VALUES " + values + " RETURNING " + pkName;
			
			
			
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			
		
			
			ResultSet rs;
			
			int id = -1;
			if((rs = stmt.executeQuery()) != null) {
				rs.next();
				
				id = rs.getInt(pkName);
			}
			
			if(save) {
				sql = "COMMIT";
				stmt = conn.prepareStatement(sql);
				stmt.execute();
			}
			
			return id;
		
		} catch(SQLException e) {
			e.printStackTrace();
			logger.info("SQLException thrown...");
			return -1;	
		}
	}
	
	public int remove(String tableName, int id, String pkName, boolean save) {
		
		
		try{
			String schema = ConnectionUtil.getSchema();
			
			String sql = "DELETE FROM " + schema + "." + tableName + " WHERE " + pkName + "=" + "?";
			
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			
			stmt.setInt(1, id);
			
			stmt.execute();
			
			if(save) {
				sql = "COMMIT";
				stmt = conn.prepareStatement(sql);
				stmt.execute();
				logger.info("Successfully removed row with id" + id + "... Committed.");
			} else {
				logger.info("Successfully removed row with id" + id + "... NOT committed.");
			}
			return id;
			
		} catch(SQLException e) {
			logger.info("SQLException thrown...");
			e.printStackTrace();
			return -1;
		}
	}
	
	
	public ArrayList<Integer> remove(String tableName, String where, String pkName, boolean save) {
		
		
		try{
			
			String schema = ConnectionUtil.getSchema();
			
			String sql = "DELETE FROM " + schema + "." + tableName + " WHERE " + where + " RETURNING " + pkName;
			
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			
			
			ResultSet rs;
			
			ArrayList<Integer> removed = new ArrayList<Integer>();
			if((rs = stmt.executeQuery()) != null) {
				while(rs.next()) {
					rs.next();
				
					removed.add(rs.getInt(pkName));
				}
			}
			
			
			if(save) {
				sql = "COMMIT";
				stmt = conn.prepareStatement(sql);
				stmt.execute();
				logger.info("Successfully removed row(s) from " + tableName + " where " + where + "... Committed");
			} else {
				logger.info("Successfully removed row(s) from " + tableName + " where " + where + "... NOT Committed");
			}
			return removed;
			
		} catch(SQLException e) {
			logger.info("SQLException thrown...");
			e.printStackTrace();
			return null;
		}
	}
}

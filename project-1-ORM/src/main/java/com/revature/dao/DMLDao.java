package com.revature.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.revature.annotations.Entity;
import com.revature.util.ConnectionUtil;


public class DMLDao {
	
	private static Logger logger = Logger.getLogger(DMLDao.class);
	public Connection conn;
	
	public DMLDao() {
		this.conn = ConnectionUtil.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			logger.error("SQLException thrown... cannot access the database...");
			e.printStackTrace();
		}
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
				logger.info("Successfully inserted data with id" + id + "... Committed.");
			}
			logger.info("Successfully inserted data with id" + id + "... NOT Committed.");
			return id;
		
		} catch(SQLException e) {
			e.printStackTrace();
			
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
			logger.error("SQLException thrown... cannot access the database...");
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
			logger.error("SQLException thrown... cannot access the database...");
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Object> find(Class<?> clazz, String where){
		
		try{
			
			String schema = ConnectionUtil.getSchema();
			String tableName = clazz.getAnnotation(Entity.class).tableName();
			
			
			String sql = "SELECT * FROM " + schema + "." + tableName + " WHERE " + where;
			
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			
			
			
			ResultSet rs;
			ArrayList<Object> found = new ArrayList<Object>();
			if((rs = stmt.executeQuery()) != null) {
				while(rs.next()) {
					rs.next();
				
					found.add(rs.getInt(3));
				}
			} 
			
		return null;
			
		} catch(SQLException e) {
			logger.error("SQLException thrown... cannot access the database...");
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	
	
	public Object findByPk(String tableName, String where) {
		
		return null;
	}
	
	
	
	
	
	
}

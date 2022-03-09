package com.revature.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import com.revature.util.ConnectionUtil;


public class TestDao {
	
	public int insert(LinkedHashMap<String, Object> colNameToValue, String tableName, String pkName, boolean save) {
		
		try(Connection conn = ConnectionUtil.getConnection()){
			
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
			
			
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
		
			
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
			return -1;	
		}
	}
	
	public int remove(String tableName, int id, boolean save) {
		
		try(Connection conn = ConnectionUtil.getConnection()){
		
			String sql = "DELETE FROM joshuas.users WHERE joshuas.users.id = ?;";
			return -1;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
		
		
	}
	
	
}

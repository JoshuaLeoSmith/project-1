package com.revature.service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import com.revature.util.ConnectionUtil;


public class TestDao {
	
	public int insert(LinkedHashMap<String, Object> colNameToValue, String tableName, boolean save) {
		
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
			
			
			
			
			String sql = "INSERT INTO " + schema + "." + tableName + " " + colNames + " VALUES " + values;
			
			
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.execute();
			
			
			
			
		
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
		return -1;
		
	}
	
}

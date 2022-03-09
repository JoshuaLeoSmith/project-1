package com.revature.service;
import java.sql.Connection;
import java.util.List;

import com.revature.util.ConnectionUtil;


public class TestDao {
	
	public int insert(List<Object> fieldValues, boolean save) {
		
		Connection conn = ConnectionUtil.getConnection();
		
		return -1;
		
	}
	
}

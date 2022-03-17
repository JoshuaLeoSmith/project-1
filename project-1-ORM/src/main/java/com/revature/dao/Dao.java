package com.revature.dao;

import java.sql.Connection;

import com.revature.util.ConnectionUtil;

public interface Dao {
	final static Connection conn = ConnectionUtil.getConnection();
}
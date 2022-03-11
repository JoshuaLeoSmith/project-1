package com.revature.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.util.ConnectionUtil;

public class TransactionDao {
	private static Connection conn = ConnectionUtil.getConnection();
	private static Logger logger = Logger.getLogger(TransactionDao.class);
	
	public void begin() {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Unable to start transaction");
		}
	}
	
	public void end() {
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Unable to end transaction");
		}
	}

	public void rollback() throws RuntimeException {
		try {
			conn.rollback();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Unable to rollback the transaction");
		}
	}
	
	public void rollback(Savepoint savepoint) {
		try {
			conn.rollback(savepoint);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Unable to rollback the transaction to given savepoint");
		}
	}
	
	public Savepoint setSavepoint(String name) {
		try {
			return conn.setSavepoint(name);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Unable to create savepoint \"" + name + "\"");
		}
	}
	
	public void releaseSavepoint(String name, Savepoint savepoint) {
		try {
			conn.releaseSavepoint(savepoint);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Unable to destroy savepoint \"" + name + "\"");
		}
	}
	
	public void commit() {
		try {
			conn.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Unable to commit the transaction");
		}
	}
	
	public int getTransactionIsolation() {
		try {
			return conn.getTransactionIsolation();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Unable to get the transaction level of the current transaction");
		}
	}
	
	public void setTransactionIsolation(int transactionIsolation) {
		try {
			conn.setTransactionIsolation(transactionIsolation);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new RuntimeException("Unable to set the transaction level of the current transaction");
		}
	}
}

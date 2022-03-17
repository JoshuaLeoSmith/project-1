package com.revature.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.apache.log4j.Logger;

import com.revature.util.ConnectionUtil;
import com.revature.exceptions.TransactionException;

public class TransactionDao {
	private static Connection conn = ConnectionUtil.getConnection();
	private static Logger logger = Logger.getLogger(TransactionDao.class);
	
	public void begin() {
		try {
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(conn.TRANSACTION_READ_COMMITTED);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new TransactionException("Unable to start the transaction");
		}
	}
	
	public void end() {
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new TransactionException("Unable to end transaction");
		}
	}

	public void rollback() {
		try {
			conn.rollback();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new TransactionException("Unable to rollback the transaction");
		}
	}
	
	public void rollback(Savepoint savepoint) {
		try {
			conn.rollback(savepoint);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new TransactionException("Unable to rollback the transaction to given savepoint");
		}
	}
	
	public Savepoint setSavepoint(String name) {
		try {
			return conn.setSavepoint(name);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new TransactionException("Unable to create savepoint \"" + name + "\"");
		}
	}
	
	public void releaseSavepoint(String name, Savepoint savepoint) {
		try {
			conn.releaseSavepoint(savepoint);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new TransactionException("Unable to destroy savepoint \"" + name + "\"");
		}
	}
	
	public void commit() {
		try {
			conn.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new TransactionException("Unable to commit the transaction");
		}
	}
	
	public int getTransactionIsolation() {
		try {
			return conn.getTransactionIsolation();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new TransactionException("Unable to get the transaction level of the current transaction");
		}
	}
	
	public void setTransactionIsolation(int transactionIsolation) {
		try {
			conn.setTransactionIsolation(transactionIsolation);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new TransactionException("Unable to set the transaction level of the current transaction");
		}
	}
}

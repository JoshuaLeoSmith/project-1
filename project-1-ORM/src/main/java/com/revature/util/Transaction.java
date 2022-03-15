package com.revature.util;

import java.sql.Connection;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.dao.TransactionDao;

public class Transaction {
	private static Logger logger = Logger.getLogger(Transaction.class);
	private static TransactionDao tdao = new TransactionDao();
	
	// copied from Connection so that the user doesn't have to worry about importing 
	// anything from the sql library when using this api
	public static final int READ_COMMITED = Connection.TRANSACTION_READ_COMMITTED;
	public static final int READ_UNCOMMITED = Connection.TRANSACTION_READ_UNCOMMITTED;
	public static final int REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;
	public static final int SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;

	private Map<String, Savepoint> savepoints;
	private boolean closed;
	
	public Transaction() throws RuntimeException {
		super();
		savepoints = new HashMap<>();
		closed = false;
		
		tdao.begin();
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public Set<String> getSavepoints() {
		if (isClosed()) {
			return new HashSet<>();
		}
		
		return savepoints.keySet();
	}

	public int getTransactionIsolation() throws RuntimeException {
		return tdao.getTransactionIsolation();
	}

	public void setTransactionIsolation(int transactionIsolation) throws RuntimeException {
		tdao.setTransactionIsolation(transactionIsolation);
	}

	public void commit() throws RuntimeException {
		if (isClosed()) {
			logger.error("Transaction is already closed, cannot execute commit()");
			throw new RuntimeException("Unable to commit a closed transaction");
		}

		tdao.commit();
		closed = true;
	}

	public void rollback() throws RuntimeException {
		if (isClosed()) {
			logger.error("Transaction is already closed, cannot execute rollback()");
			throw new RuntimeException("Error. Unable to rollback a closed transation");
		}
		
		tdao.rollback();
		closed = true;
	}

	public void rollback(String name) throws RuntimeException {
		if (isClosed()) {
			logger.error("Transaction is already closed, cannot execute rollback()");
			throw new RuntimeException("Error. Unable to use rollback a closed transaction");
		}

		Savepoint savepoint = savepoints.get(name);
		
		if (savepoint != null) {
			tdao.rollback(savepoint);
		} else {
			logger.error("The savepoint \"" + name + "\" does not exist");
			throw new RuntimeException("Unable to find savepoint \"" + name + "\"");
		}
	}

	public String createSavepoint(String name) throws RuntimeException {
		if (isClosed()) {
			logger.error("Transaction is already closed, cannot execute createSavepoint()");
			throw new RuntimeException("Error. Unable to create a savepoint on a closed transaction");
		}
		
		Savepoint savepoint = tdao.setSavepoint(name);
		savepoints.put(name, savepoint);
		
		return name;
	}
	
	public void destroySavepoint(String name) throws RuntimeException {
		if (isClosed()) {
			logger.error("Transaction is already closed, cannot execute destroySavepoint()");
			throw new RuntimeException("Error. Unable to destroy a savepoint of a closed transaction");
		}
		
		Savepoint savepoint = savepoints.get(name);
		
		if (savepoint != null) {
			tdao.releaseSavepoint(name, savepoint);
		} else {
			logger.error("The savepoint \"" + name + "\" does not exist");
			throw new RuntimeException("Unable to find savepoint \"" + name + "\"");
		}
	}
}
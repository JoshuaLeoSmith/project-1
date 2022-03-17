package com.revature.service;

import java.sql.Connection;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.dao.TransactionDao;
import com.revature.exceptions.SavePointNotFoundException;
import com.revature.exceptions.TransactionClosedException;
import com.revature.exceptions.TransactionException;

public class Transaction {
	private static Logger logger = Logger.getLogger(Transaction.class);
	private TransactionDao tdao = new TransactionDao();

	// copied from Connection so that the user doesn't have to worry about importing
	// anything from the sql library when using this api
	public static final int READ_COMMITED = Connection.TRANSACTION_READ_COMMITTED;
	public static final int READ_UNCOMMITED = Connection.TRANSACTION_READ_UNCOMMITTED;
	public static final int REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;
	public static final int SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;

	private Map<String, Savepoint> savepoints;
	private boolean closed = true;

	/**
	 * The constructor for creating a new transaction. Starts a transaction on the
	 * current connection by calling TransactionDao.begin().
	 * 
	 * @throws TransactionException
	 * @throws TransactionException 
	 * 		if an SQLException is thrown on the TransactionDao level, a
	 * 		TransactionException is thrown
	 * @see TransactionDao
	 */
	public Transaction() throws TransactionException {
		super();
		savepoints = new HashMap<>();

		try {
			tdao.begin();
		} catch (TransactionException e) {
			e.printStackTrace();
		}

		closed = false;
	}

	/**
	 * The constructor for testing purposes only, allow for mocking of
	 * TransactionDao. Other than that, creates and starts a transaction in the same
	 * way as the no args constructor.
	 * 
	 * @param tdao the TransactionDao to use in-place for the default TransactionDao
	 * @throws TransactionException 
	 * 		if an SQLException is thrown on the TransactionDao level, a
	 * 		TransactionException is thrown
	 * @see TransactionDao
	 */
	public Transaction(TransactionDao tdao) throws TransactionException {
		super();
		this.tdao = tdao;
		savepoints = new HashMap<>();

		tdao.begin();

		closed = false;
	}

	/**
	 * Gets the current state of the 
	 * 
	 * @return	if the current transaction is closed
	 */
	public boolean isClosed() {
		return closed;
	}

	public Set<String> getSavepoints() {
		if (isClosed()) {
			return new HashSet<>();
		}

		return savepoints.keySet();
	}

	public int getTransactionIsolation() throws TransactionException {
		if (isClosed()) {
			logger.error("Transaction is closed, cannot execute getTransactionIsolation()");
			throw new TransactionClosedException("Unable to get a transaction isolation level on a closed transaction");
		}

		return tdao.getTransactionIsolation();
	}

	public void setTransactionIsolation(int transactionIsolation) throws TransactionException {
		if (isClosed()) {
			logger.error("Transaction is closed, cannot execute setTransactionIsolation()");
			throw new TransactionClosedException("Unable to get a transaction isolation level on a closed transaction");
		}

		tdao.setTransactionIsolation(transactionIsolation);
	}

	public void commit() throws TransactionException {
		if (isClosed()) {
			logger.error("Transaction is closed, cannot execute commit()");
			throw new TransactionClosedException("Unable to commit a closed transaction");
		}

		tdao.commit();
		tdao.end();
		closed = true;
	}

	public void rollback() throws TransactionException {
		if (isClosed()) {
			logger.error("Transaction is closed, cannot execute rollback()");
			throw new TransactionClosedException("Error. Unable to rollback a closed transation");
		}

		tdao.rollback();
		tdao.end();
		closed = true;
	}

	public void rollback(String name) throws TransactionException {
		if (isClosed()) {
			logger.error("Transaction is closed, cannot execute rollback(String)");
			throw new TransactionClosedException("Error. Unable to use rollback a closed transaction");
		}

		Savepoint savepoint = savepoints.get(name);

		if (savepoint != null) {
			tdao.rollback(savepoint);
		} else {
			logger.error("The savepoint \"" + name + "\" does not exist");
			throw new SavePointNotFoundException("Unable to find savepoint \"" + name + "\"");
		}
	}

	public String createSavepoint(String name) throws TransactionException {
		if (isClosed()) {
			logger.error("Transaction is closed, cannot execute createSavepoint()");
			throw new TransactionClosedException("Unable to create a savepoint on a closed transaction");
		}

		Savepoint savepoint = tdao.setSavepoint(name);
		savepoints.put(name, savepoint);

		return name;
	}

	public void destroySavepoint(String name) throws TransactionException {
		if (isClosed()) {
			logger.error("Transaction is closed, cannot execute destroySavepoint()");
			throw new TransactionClosedException("Unable to destroy a savepoint of a closed transaction");
		}

		Savepoint savepoint = savepoints.get(name);

		if (savepoint != null) {
			tdao.releaseSavepoint(name, savepoint);
		} else {
			logger.error("The savepoint \"" + name + "\" does not exist");
			throw new SavePointNotFoundException("Unable to find savepoint \"" + name + "\"");
		}
	}
}
package com.revature.service;

import java.sql.Savepoint;
import java.util.ArrayList;

import com.revature.util.MetaModel;

public interface IServices {

	/**
	 * Inserts object as a row into the table that pertains to the class of the object o.
	 * @param o The object of a class whose table will be inserted into.
	 * @param save Committed if true, not committed if false.
	 * @return The id of the new row inserted.
	 */
	public int insert(Object o, boolean save);
	
	
	/**
	 * Removes row from table pertaining to clazz based on id.
	 * @param clazz The class whose table is to be removed from.
	 * @param id The primary key whose row will be removed from the table.
	 * @param save Committed if true, not committed if false.
	 * @return The id of the row removed.
	 */
	public int removeByPk(Class<?> clazz, int id, boolean save);
	
	
	/**
	 * Removes rows from table given a specific condition that the user manually inputs.
	 * @param clazz The class whose table is to be removed from.
	 * @param where The condition written in pseudo-SQL that will determine what rows are removed.
	 * @param save Committed if true, not committed if false.
	 * @return A list of id's whose rows were removed.
	 */
	public ArrayList<Integer> remove(Class<?> clazz, String where, boolean save);
	
	
	/**
	 * Persists the changes to the database.
	 */
	public void commit();
	
	
	/**
	 * Creates new table from clazz with proper annotations.
	 * @param clazz The class whose table is to be created.
	 * @return 1 if successful, -1 if failed.
	 */
	int create(Class<?> clazz);

	
	/**
	 * Locates rows in tables given a specific condition.
	 * @param clazz The class whose table is to be searched.
	 * @param where The condition written in pseudo-SQL that will determine what rows are removed
	 * @return A list of Objects pertaining to rows in the table.
	 */
	ArrayList<Object> find(Class<?> clazz, String where);

	
	/**
	 * Locates rows in tables based on id.
	 * @param clazz The class annotated with @Entity whose table is to be searched.
	 * @param id The primary key that will be searched for in the table. 
	 * @return An object with data pertaining to its rows in the table.
	 */
	Object findByPk(Class<?> clazz, int id);
	
	
	/**
	 * Alters existing table based on properties of clazz parameters inserted.
	 * @param clazz Class pertaining to table that will be altered.
	 */
	void alter(Class<?> clazz);
	
	
	/**
	 * Removes all data from the table that pertains to clazz while keeping the structure of
	 * the table intact.
	 * @param clazz The class whose table is to be truncated.
	 */
	void truncate(Class<?> clazz);
	
	
	/**
	 * Deletes table and all data in table pertaining to clazz.
	 * @param clazz The class whose table is to be dropped.
	 */
	void drop(Class<?> clazz);
	
	
	/**
	 * Renames a table pertaining to clazz (will change to the name of
	 * the class found in the .java file).
	 * @param clazz The class whose table name will be changed.
	 * @param oldName The original name of the table.
	 */
	void renameTable(Class<?> clazz, String oldName);
	
	
	/**
	 * Renames a column that is a part of the table pertaining to clazz.
	 * @param clazz The class whose column name will be changed.
	 * @param oldName The original name of the column.
	 * @param newName The new name of the column.
	 */
	void renameColumn(Class<?> clazz, String oldName, String newName);

	
	/**
	 * Updates a row with id o.getId() and changes the values to those found in Object o.
	 * @param o The object whose data will overwrite old data in the table.
	 * @return The id of the row that was updated int the table.
	 */
	int updateRow(Object o);

	
	/**
	 * Begins transaction.
	 */
	void beginTransaction();

	
	/**
	 * Sets the transaction isolation level.
	 * @param t The transaction isolation level to be changed to.
	 */
	void setTransactionIsolation(int t);

	
	/**
	 * Gets the current transaction isolation level.
	 * @return The current transaction isolation level as an integer.
	 */
	int getTransactionIsolation();

	
	/**
	 * Destroys a savepoint previously defined in the current transaction.
	 * @param name The name of the savepoint to be released.
	 * @param savepoint The savepoint to be released.
	 */
	void releaseSavepoint(String name, Savepoint savepoint);

	
	/**
	 * Creates a savepoint.
	 * @param name The name of the savepoint created.
	 * @return The savepoint object pertaining to the name inputted by the user.
	 */
	Savepoint setSavepoint(String name);

	
	/**
	 * Rolls back to previous commit in a transaction.
	 */
	public void rollback();
	
	
	/**
	 * Rolls back to previous savepoint in a transaction.
	 * @param savepoint The savepoint object to be rolled back to.
	 */
	void rollback(Savepoint savepoint);

	
	/**
	 * Ends the current transaction.
	 */
	void endTransaction();
}

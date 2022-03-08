package com.revature.service;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.revature.util.ColumnField;
import com.revature.util.MetaModel;


public class ServicesImpl implements IServices {

	@Override
	public int insert(Object o, boolean save) {
		
		// Object o has fields that will be added to the table
		// boolean save indicates whether the changes will be committed or not
		
		MetaModel m = new MetaModel(o.getClass());
		
		LinkedList<Object> fieldValues = (LinkedList<Object>) m.getAllValsOfField(o);
		
		System.out.println(fieldValues);
		
		
		
		return -1;
		// return TableDao.insertIntoTable(fieldValues, save) 
		// this should return the id of the new row created, or -1 if failed.
		
	}
	
	//public static void main(String[] args) {
		
	//	IServices is = new ServicesImpl();
	//	TesterClass t = new TesterClass("jls", "123", 21, 3);
		
//		is.insert(t, false);
//		
	//}
	//
	@Override
	public int remove(int id, boolean save) {
		
		// int id is the primary key of the column that will be removed
		// boolean save indicates whether the changes will be committed or not
		
		
		
		return -1;
		// return TableDao.removeFromTable(id) 
		// this should return the id of the row deleted, or -1 if failed.
		
	}
	
	@Override
	public int remove(String where, boolean save) {
		
		// String where is the condition that will be used to determine what will
		// 		be removed. (ex. age > 4) .
		// boolean save indicates whether the changes will be committed or not
		
		
		return -1;
		// return TableDao.removeFromTable(where) 
		// this should return the id of the row deleted, or -1 if failed.
	}
	     
	
	@Override 
	public int rollback() {
		// will rollback to last commit/savepoint/rollback
		// basically just call the SQL rollback command
		
		
		return -1;
		// return TableDao.roll()
		// this should return 1 if successful, -1 if unsuccessful
		
	}
	
	@Override
	public int commit() {
		// will commit saved changes
		// basically just the SQL commit command
		
		
		return -1;
		// return tableDao.commit()
		// this should return 1 if successful, -1 if unsuccessful
	}
		
}







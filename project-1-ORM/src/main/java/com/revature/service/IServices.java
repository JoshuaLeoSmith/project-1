package com.revature.service;

public interface IServices {

	public int insert(Object o, boolean save);
	
	public int remove(String tableName, int id, boolean save);
	public int remove(String tableName, String where, boolean save);
	
	
	public int commit();
	
	public int rollback();

	int create(String tableName, Class<?> clazz);

	
	
	
	
	///public int create();
	
	//public int select();
	
	
}

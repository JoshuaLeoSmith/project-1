package com.revature.service;

import java.util.ArrayList;

public interface IServices {

	public int insert(Object o, boolean save);
	
	public int remove(Class<?> clazz, int id, boolean save);
	public ArrayList<Integer> remove(Class<?> clazz, String where, boolean save);
	
	
	public int commit();
	
	public int rollback();

	//int create(String tableName, Class<?> clazz);

	ArrayList<Object> find(Class<?> clazz, String where);

	Object findByPk(Class<?> clazz, int id);

	
	
	
	
	///public int create();
	
	//public int select();
	
	
}

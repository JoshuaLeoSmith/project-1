package com.revature.service;

import java.util.ArrayList;

import com.revature.util.MetaModel;

public interface IServices {

	public int insert(Object o, boolean save);
	
	public int removeByPk(Class<?> clazz, int id, boolean save);
	public ArrayList<Integer> remove(Class<?> clazz, String where, boolean save);
	
	
	public int commit();
	
	public int rollback();

	int create(Class<?> clazz);

	ArrayList<Object> find(Class<?> clazz, String where);

	Object findByPk(Class<?> clazz, int id);
	
	void alter(Class<?> clazz);
	
	void truncate(Class<?> clazz);
	
	void drop(Class<?> clazz);
	
	void renameTable(Class<?> clazz, String oldName);
	
	void renameColumn(Class<?> clazz, String oldName, String newName);

	
	
	
	
	///public int create();
	
	//public int select();
	
	
}

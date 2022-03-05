package com.revature.service;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.revature.inspection.ClassInspector;


public class ServicesImpl implements IServices {

	@Override
	public int insert(Object o, boolean save) {
		
		//o.getClass().getDeclaredFields();
		
		List<Field> fields = new LinkedList<Field>();
		fields = ClassInspector.getColumns(o.getClass());
		List<Object> fieldValues = new LinkedList<Object>();
		
		
		for(Field f : fields) {
			f.setAccessible(true);
			Object val = null;
			try {
				val = f.get(o);
			} catch(IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			fieldValues.add(val);
		}
		
		System.out.println(fieldValues);

		
		return -1;
		// return TableDao.insertIntoTable(fieldValues, save) 
		// this should return the id of the new row created, or -1 if failed.
		
	}
	
	
	@Override
	public int remove(int id, boolean save) {
		
		return -1;
		// return TableDao.removeFromTable(id) 
		// this should return the id of the row deleted, or -1 if failed.
		
	}
	
	@Override
	public int remove(String where, boolean save) {
		
		return -1;
		// return TableDao.removeFromTable(where) 
		// this should return the id of the row deleted, or -1 if failed.
	}
	     
	
	@Override 
	public int roll() {
		
		return -1;
		// return TableDao.roll()
		// this should return 1 if successful, -1 if unsuccessful
		
	}
	
	@Override
	public int commit() {
		
		return -1;
		// return tableDao.commit()
		// this should return 1 if successful, -1 if unsuccessful
	}
	
	public static void main(String[] args) {
		IServices s = new ServicesImpl();
		
		TesterClass t = new TesterClass("jls", "pwd", 12.5, 2);
		
		s.insert(t, true);
		
		
	}	
}







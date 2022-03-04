package com.revature.service;

import java.lang.reflect.Field;


public class ServicesImpl implements IServices {

	
	@Override
	public int insert(Object o) {
		
		//o.getClass().getDeclaredFields();
		
	     
		Class<?> clazz = o.getClass();
	    Field field;
		try {
			field = clazz.getDeclaredField("username");
			field.setAccessible(true);
			
			try {
				Object fieldValue = field.get(o);
				System.out.println(fieldValue);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		} //Note, this can throw an exception if the field doesn't exist.
	    
	
		return -1; // return what the dao returns
	}
	
	public static void main(String[] args) {
		IServices s = new ServicesImpl();
		
		TesterClass t = new TesterClass("jls", "pwd", 12.5, 2);
		
		
		
		s.insert(t);
		
		
	}	
}







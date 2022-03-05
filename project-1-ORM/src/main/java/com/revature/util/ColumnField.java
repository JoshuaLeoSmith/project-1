package com.revature.util;

import java.lang.reflect.Field;

import com.revature.annotations.Column;
import com.revature.annotations.Exclude;

/**
 * The whole purpose of this class is to extract the
 * fields of class marked with the @Column annotation
 * which we've designed in the com.revature.annotations package.
 * 
 * What is the info we need about a field of a user's class in order
 * to generate the appropriate column?
 * 
 * 
 * - Type? 
 * - Field name?
 * - column Name (which they indicated in the column annotation meta data)
 */
public class ColumnField {
	private Field field;
	
	public ColumnField(Field field) {
		if (field.getAnnotation(Column.class) == null && field.getAnnotation(Exclude.class) != null) {
			throw new IllegalStateException("Cannot create ColumnField object! Provided field " + getName() + " is not Annotated");
		}
		
		this.field = field;
		
	}
	
	public String getName() {
		return field.getName();
	}
	

	public Class<?> getType() {
		return field.getType();
	}


	public String getColumnName() {
		if (field.getAnnotation(Column.class) == null)
			return "";
		
		return field.getAnnotation(Column.class).columnName();
	}
	
	public boolean isUnique() {
		if (field.getAnnotation(Column.class) == null)
			return false;
		
		return field.getAnnotation(Column.class).unique();
	}
	
	public boolean isNullable() {
		if (field.getAnnotation(Column.class) == null)
			return true;
		
		return field.getAnnotation(Column.class).nullable();
	}
	
	public int getPercision() {
		if (field.getAnnotation(Column.class) == null)
			return 0;
		
		return field.getAnnotation(Column.class).precision();
	}
	
	public int getLength() {
		if (field.getAnnotation(Column.class) == null)
			return 255;
		
		return field.getAnnotation(Column.class).length();
	}
}
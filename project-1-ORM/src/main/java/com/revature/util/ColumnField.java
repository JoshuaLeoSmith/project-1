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
public class ColumnField implements GenericField {
	private Field field;
	
	public ColumnField(Field field) {
		if (field.getAnnotation(Column.class) == null && field.getAnnotation(Exclude.class) != null) {
			throw new IllegalStateException("Cannot create ColumnField object! Provided field " + getName() + " is not Annotated with @Column or is Annotated with @Exclude");
		}
		
		this.field = field;
	}
	
	public String getName() {
		return field.getName();
	}
	

	public Class<?> getType() {
		return field.getType();
	}
	
	public boolean isExcluded() {
		return field.getAnnotation(Column.class) == null;
	}

	public String getColumnName() {
		if (this.isExcluded())
			return "";
		
		return field.getAnnotation(Column.class).columnName();
	}
	
	public boolean isSerial() {
		if (this.isExcluded())
			return false;
		
		return field.getAnnotation(Column.class).serial();
	}
	
	public boolean isUnique() {
		if (this.isExcluded())
			return false;
		
		return field.getAnnotation(Column.class).unique();
	}
	
	public boolean isNullable() {
		if (this.isExcluded())
			return true;
		
		return field.getAnnotation(Column.class).nullable();
	}
	
	public String getDefaultValue() {
		if (this.isExcluded())
			return "";
		
		return field.getAnnotation(Column.class).default_value();
	}
	
	public int getPercision() {
		if (this.isExcluded())
			return 6;
		
		return field.getAnnotation(Column.class).precision();
	}
	
	public int getScale() {
		if (this.isExcluded())
			return 0;
		
		return field.getAnnotation(Column.class).scale();
	}
	
	public int getLength() {
		if (this.isExcluded())
			return 255;
		
		return field.getAnnotation(Column.class).length();
	}
	
}
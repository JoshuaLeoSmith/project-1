package com.revature.util;

import java.lang.reflect.Field;
import java.util.Objects;

import com.revature.annotations.JoinColumn;

public class ForeignKeyField implements GenericField {
	
	
	private Field field; // from java.lang.reflect
	
	// constructor
	public ForeignKeyField(Field field) {
		
		// check if it has the annotation we're looking for 
		if (field.getAnnotation(JoinColumn.class) == null) { // if it's NOT equal to @Column...
			throw new IllegalStateException("Cannot create ColumnField object! Provided field " + getName() + 
					" is not annotated with @JoinColumn");
		}
		
		this.field = field;
	}
	
	public String getName() {
		return field.getName();
	}
	
	// return the TYPE of the field that's annotated
	public Class<?> getType() {
		return field.getType(); // think about how we could this to our advantage when we (as the ORM framework developers
							    // are crafting a way in which we can  set up a way to determine the RDBMS type for the column
	}

	// getColumnName() --> extract the column name that the user sets for that field
	public String getColumnName() {
		return field.getAnnotation(JoinColumn.class).columnName(); // extract the columnName() property that the user sets
	}
	
	public String getMappedByColumn() {
		return field.getAnnotation(JoinColumn.class).mappedByColumn();
	}
	
	public String getMappedByTable() {
		return field.getAnnotation(JoinColumn.class).mappedByTable();
	}

	@Override
	public int hashCode() {
		return Objects.hash(field);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ForeignKeyField other = (ForeignKeyField) obj;
		return Objects.equals(field, other.field);
	}

	@Override
	public String toString() {
		return "ForeignKeyField [field=" + field + "]";
	}
}
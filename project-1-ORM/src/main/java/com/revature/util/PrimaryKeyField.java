package com.revature.util;

import java.lang.reflect.Field;
import java.util.Objects;

import com.revature.Relation;
import com.revature.annotations.Column;
import com.revature.annotations.Id;

public class PrimaryKeyField implements GenericField {
	
	private Field field; // from java.lang.reflect
	private Relation relation;
	
	// constructor to build an object that represents data about the field that has been annotated with @Id
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PrimaryKeyField(Field field) {
		
		// check if it has the annotation we're looking for 
		if (field.getAnnotation(Id.class) == null) { // if it's NOT equal to @Column...
			throw new IllegalStateException("Cannot create ColumnField object! Provided field " + getName() + 
					" is not Annotated with @Id");
		}
		
		this.relation = Relation.None;
		for (Class relation : relations) {
			if (field.getAnnotation(relation) != null) {
				if (this.relation != Relation.None) {
					throw new IllegalStateException(
							"Provided field " + field.getName() + " can only have one relationship type");
				} else {
					this.relation = Relation.valueOf(relation.getSimpleName());
				}
			}
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
		return field.getAnnotation(Id.class).columnName(); // extract the columnName() property that the user sets
	}
	
	public boolean isSerial() {
		return field.getAnnotation(Id.class).serial();
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
		PrimaryKeyField other = (PrimaryKeyField) obj;
		return Objects.equals(field, other.field);
	}

	@Override
	public String toString() {
		return "PrimaryKeyField [field=" + field + "]";
	}

	@Override
	public Relation getRelation() {
		return relation;
	}
}
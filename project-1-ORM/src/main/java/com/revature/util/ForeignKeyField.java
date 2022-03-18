package com.revature.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

import com.revature.Relation;
import com.revature.annotations.JoinColumn;

public class ForeignKeyField implements GenericField {
	private Field field;
	private Relation relation;

	// constructor
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ForeignKeyField(Field field) {
		JoinColumn anno = field.getAnnotation(JoinColumn.class);

		if (anno == null) {
			throw new IllegalStateException("Cannot create ColumnField object! Provided field " + field.getName()
					+ " is not annotated with @JoinColumn");
		} else if (anno.mappedByColumn().equals("") || anno.mappedByTable().equals("")) {
			throw new IllegalStateException(
					"Provided field " + field.getName() + " needs to have a mappedbyColumn and mappedby");
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
		
		if (this.relation == Relation.None || this.relation == Relation.OneToMany) {
			throw new IllegalStateException(
					"Provided field " + field.getName() + " must have one declared relationship type to not OneToMany");
		}
		
		this.field = field;
	}

	public String getName() {
		return field.getName();
	}

	// return the TYPE of the field that's annotated
	public Class<?> getType() {
		return field.getType(); // think about how we could this to our advantage when we (as the ORM framework
								// developers
								// are crafting a way in which we can set up a way to determine the RDBMS type
								// for the column
	}
	
	public Field getField() {
		return field;
	}

	// getColumnName() --> extract the column name that the user sets for that field
	public String getColumnName() {
		return field.getAnnotation(JoinColumn.class).columnName(); // extract the columnName() property that the user
																	// sets
	}

	public String getMappedByColumn() {
		return field.getAnnotation(JoinColumn.class).mappedByColumn();
	}

	public String getMappedByTable() {
		return field.getAnnotation(JoinColumn.class).mappedByTable();
	}
	
	public boolean isCascade() {
		return field.getAnnotation(JoinColumn.class).cascade();
	}
	
	public Relation getRelation() {
		return relation;
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

	@Override
	public String getSubType() {
		Type type = field.getGenericType();
		
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			
			try {
				return pt.getActualTypeArguments()[0].getTypeName();
			} catch (IndexOutOfBoundsException e) {
				return null;
			}
		}
		
		return null;
	}
}
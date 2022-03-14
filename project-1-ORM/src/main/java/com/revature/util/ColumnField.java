package com.revature.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.revature.Relation;
import com.revature.annotations.Check;
import com.revature.annotations.Checks;
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
	private Relation relation;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ColumnField(Field field) {
		if (field.getAnnotation(Column.class) == null && field.getAnnotation(Exclude.class) != null) {
			throw new IllegalStateException("Cannot create ColumnField object! Provided field " + getName() + " is not Annotated with @Column or is Annotated with @Exclude");
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
		return field.getName().toLowerCase();
	}
	

	public Class<?> getType() {
		return field.getType();
	}
	
	public boolean isExcluded() {
		return field.getAnnotation(Column.class) == null;
	}

	public String getColumnName() {
		if (this.isExcluded() || field.getAnnotation(Column.class).columnName().equals(""))
			return field.getName();
		
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
	
	public List<String> getChecks() {
		List<String> constriants = new ArrayList<>();
		Checks checks = field.getAnnotation(Checks.class);
		
		if (this.isExcluded() || checks == null)
			return new ArrayList<>();
		
		Check[] checkList = checks.value();
		
		for (Check check: checkList) {
			constriants.add(check.value());
		}
		
		return constriants;
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
		ColumnField other = (ColumnField) obj;
		return Objects.equals(field, other.field);
	}

	@Override
	public String toString() {
		return "ColumnField [field=" + field + "]";
	}

	@Override
	public Relation getRelation() {
		return relation;
	}
}
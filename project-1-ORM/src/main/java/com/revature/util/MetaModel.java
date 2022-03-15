package com.revature.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BinaryOperator;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Exclude;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;

/**
 * The purpose of this class is to gather as much information as possible about
 * the class we want to transpose into a DB entity (table)
 * 
 * This class' job is to model data about another class.
 */
public class MetaModel<T> {

	private Class<?> clazz;
	private PrimaryKeyField primaryKeyField;
	private List<ColumnField> columnFields;
	private List<ForeignKeyField> foreignKeyFields;

	// create a method in which we pass a class through and generate a meta model OF
	// the class
	public static MetaModel<Class<?>> of(Class<?> clazz) {
		// check that the class we're attempting to transpose is annotated with @Entity
		if (clazz.getAnnotation(Entity.class) == null) {
			throw new IllegalStateException("Cannot create MetaModel object from this class! Provided class "
					+ clazz.getName() + " is not annotated with @Entity");
		}
		// if it IS annotated with @Entity, generate a MetaModel object of it.
		return new MetaModel<Class<?>>(clazz);
	}

	// constructor to build a metamodel
	public MetaModel(Class<?> clazz) {
		this.clazz = clazz; // since we've set the class as equal to the original class, we still intel on
							// its fields
		this.columnFields = new LinkedList<ColumnField>();
		this.foreignKeyFields = new LinkedList<ForeignKeyField>();
	}

	// getColumns() - returns a list of ColumnField
	public List<ColumnField> getColumns() {
		if (!columnFields.isEmpty())
			return columnFields;

		Field[] fields = clazz.getDeclaredFields();

		// check if each field has a @Column annotation
		// if it does, add it to the metamodel's columnField's list
		for (Field field : fields) {
			if (!field.isSynthetic()) {
				// the column reference variable will NOT be null if the field is indeed
				// annotated with @Column
				Column column = field.getAnnotation(Column.class);
				Exclude exclude = field.getAnnotation(Exclude.class);
				Id id = field.getAnnotation(Id.class);
				JoinColumn jColumn = field.getAnnotation(JoinColumn.class);

				if (column != null || (exclude == null && id == null && jColumn == null)) {
					// if the column is indeed marked with @Colum, instantiate a new ColumnField
					// object with its data
					columnFields.add(new ColumnField(field));
					// now that we've transposed the field to a column Field object, we can capture
					// data like Type, columnName, etc...
				}
			}
		}
		if (columnFields.isEmpty()) {
			throw new RuntimeException("No columns found in: " + clazz.getName());
		}
		return columnFields;
	}

	// getPrimaryKey() - return a PrimaryKeyField or throw a runtime exception if we
	// can't find one
	public PrimaryKeyField getPrimaryKey() {
		if (primaryKeyField != null)
			return primaryKeyField;

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {

			Id primaryKey = field.getAnnotation(Id.class);

			if (primaryKey != null) { // we se the primaryKeyField of this MetaModel to the field we find
				this.primaryKeyField = new PrimaryKeyField(field);
				return new PrimaryKeyField(field); // if there is a PK found, we end the method here and return it
			}
		}

		throw new RuntimeException("Did not find a field annotated with @Id in " + clazz.getName());
	}

	// getForeignKey - returns a list of foreignKeyFields
	public List<ForeignKeyField> getForeignKeys() {
		if (!foreignKeyFields.isEmpty())
			return foreignKeyFields;

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {

			JoinColumn foreignKey = field.getAnnotation(JoinColumn.class);

			if (foreignKey != null) {
				foreignKeyFields.add(new ForeignKeyField(field));
			}
		}
		if (foreignKeyFields.isEmpty()) {
			throw new RuntimeException("No foreign keys found in: " + clazz.getName());
		}
		return foreignKeyFields;
	}

	public String getSimpleClassName() {
		return clazz.getSimpleName();
	}

	public String getClassName() {
		return clazz.getName(); // returns the package of where the class came from as well
	}

	public String getTableName() {
		return clazz.getAnnotation(Entity.class).tableName();
	}

	// to use this function, you must first call all of the get methods of MetaModel
	// for each instance before calling this function so that all of the field have
	// had at least one chance to be initialized
	public GenericField getFieldByName(String name) {
		if (primaryKeyField != null) {
			if (primaryKeyField.getName().equals(name) || primaryKeyField.getColumnName().equals(name)) {
				return primaryKeyField;
			}
		}

		for (ForeignKeyField fkField : foreignKeyFields) {
			if (fkField.getName().equals(name) || fkField.getColumnName().equals(name)) {
				return fkField;
			}
		}

		for (ColumnField cField : columnFields) {
			if (cField.getName().equals(name) || cField.getColumnName().equals(name)) {
				return cField;
			}
		}

		throw new RuntimeException("Unable to find field \"" + name + "\" in class \"" + this.getClassName() + "\"");
	}

	// to use this function, you must first call all of the get methods of MetaModel
	// for each instance before calling this function so that all of the field have
	// had at least one chance to be initialized
	public Set<GenericField> getAllFields() {
		Set<GenericField> allFields = new HashSet<>();
		if (primaryKeyField != null)
			allFields.add(primaryKeyField);

		allFields.addAll(foreignKeyFields);
		allFields.addAll(columnFields);

		return allFields;
	}

	public List<Object> getAllValsOfField(Object o) {

		Field[] fields = clazz.getDeclaredFields();

		List<Object> fieldValues = new LinkedList<Object>();

		for (Field f : fields) {

			try {
				f.setAccessible(true);
				fieldValues.add(f.get(o));
				f.setAccessible(false);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return fieldValues;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clazz, columnFields, foreignKeyFields, primaryKeyField);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaModel other = (MetaModel) obj;
		return Objects.equals(clazz, other.clazz) && Objects.equals(columnFields, other.columnFields)
				&& Objects.equals(foreignKeyFields, other.foreignKeyFields)
				&& Objects.equals(primaryKeyField, other.primaryKeyField);
	}
}
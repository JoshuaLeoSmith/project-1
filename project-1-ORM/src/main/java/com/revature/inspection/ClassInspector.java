package com.revature.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.revature.annotations.Entity;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;

public class ClassInspector {
	public static boolean isEntity(Class<?> clazz) {
		Annotation[] classAnnotations = clazz.getAnnotations();

		for (Annotation a : classAnnotations) {
			if (a.annotationType() == Entity.class) {
				return true;
			}
		}

		return false;
	}

	public static List<Field> getColumns(Class<?> clazz) {
		Field[] classFields = clazz.getDeclaredFields();

		if (classFields.length == 0) {
			throw new IllegalStateException("Class does not have any defined Fields");
		}

		return Arrays.asList(classFields);
	}

	public static Field getPrimaryKey(Class<?> clazz) {
		Field[] classFields = clazz.getDeclaredFields();

		if (classFields.length == 0) {
			throw new IllegalStateException("Class does not have any defined Fields");
		}

		for (Field f : classFields) {
			if (f.getAnnotation(Id.class)!= null) {
				return f;
			}
		}

		throw new IllegalStateException("No Primary Key specified (@Id)");
	}

	public static List<Field> getForeignKey(Class<?> clazz) {
		Field[] classFields = clazz.getDeclaredFields();
		List<Field> foreignKeys = new LinkedList<>();

		if (classFields.length == 0) {
			throw new IllegalStateException("Class does not have any defined Fields");
		}

		for (Field f : classFields) {
			if (f.getAnnotation(JoinColumn.class) != null) {
				foreignKeys.add(f);
			}
		}

		return foreignKeys;
	}
}
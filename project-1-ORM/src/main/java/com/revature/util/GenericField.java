package com.revature.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.revature.Relation;
import com.revature.annotations.ManyToMany;
import com.revature.annotations.ManyToOne;
import com.revature.annotations.OneToMany;
import com.revature.annotations.OneToOne;

public interface GenericField {
	static final Set<Class<?>> relations = new HashSet<>(
			Arrays.asList(OneToOne.class, OneToMany.class, ManyToOne.class, ManyToMany.class));
	
	String getName();
	Class<?> getType();
	String getColumnName();
	Relation getRelation();
	String getSubType();
	Field getField();
}
package com.revature.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // this dictates that we can only apply this annotation to a field
@Retention(RetentionPolicy.RUNTIME)
public @interface Column { // technically an annotation is referred to as a marker interface

	// adding a property of an annotation
	String columnName(); // this would allow the user to use it like so @Column(columnName="first_name")
	
}
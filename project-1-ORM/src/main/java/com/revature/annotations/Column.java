package com.revature.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String columnName() default "";
	boolean serial() default false;
	boolean unique() default false;
	boolean nullable() default true;
	String default_value() default "";
	int precision() default 6;
	int scale() default 0;
	int length() default 255;
}
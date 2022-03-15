package com.revature.util;

import java.util.Set;

import org.reflections.Reflections;

import com.revature.annotations.Entity;
import com.revature.service.ServicesImpl;

public class AutoScan {
	static ServicesImpl Simpl = new ServicesImpl();
	public static void main(String[] args) {


		Reflections reflections = new Reflections("com.revature");
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Entity.class);

		annotated.forEach(x -> Simpl.create(x));
		System.out.println(annotated);


	}
}

package com.revature.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.revature.Relation;
import com.revature.annotations.Entity;
import com.revature.service.ServicesImpl;

public class AutoScan {
	static ServicesImpl Simpl = new ServicesImpl();

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		getAllClasses();
		/*
		 * MetaModel<?> personM = MetaModel.of(person.class);
		 * 
		 * TableDao TD = new TableDao(); TD.addForeignKeys(personM);
		 */

		//		Configuration config;
		//
		//		try {
		//			config = new Configuration();
		//			config.addTables(person.class, Account.class);
		//
		//			config.validate();
		//		} catch (FileNotFoundException e) {
		//			e.printStackTrace();
		//			System.exit(1);
		//		}
		System.out.println("Done");

	}

	public static void getAllClasses() {

		Reflections reflections = new Reflections("com.revature");
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Entity.class);
		System.out.println("Starting");
		List<Class<?>> allClasses=new ArrayList<>();
		annotated.forEach(x -> allClasses.add(x));
		List<Class<?>> laterClasses=new ArrayList<>();
		for(int i=0; i<allClasses.size(); i++) {
			MetaModel<Class<?>> bob= MetaModel.of(allClasses.get(i));
			boolean badClass=false;
			try {
				for(ForeignKeyField fk : bob.getForeignKeys()) {
					if((fk.getRelation()==Relation.ManyToOne) && !laterClasses.contains(allClasses.get(i))) {
						laterClasses.add(allClasses.get(i));
						badClass=true;
					}
				}
			} catch (Exception e) {
			}
			if (!badClass) {
				Simpl.create(allClasses.get(i));
			}

		}
		for (Class<?> element : laterClasses) {
			Simpl.create(element);
		}

		System.out.println("Finished");
	}

}

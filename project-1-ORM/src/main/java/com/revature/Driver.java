package com.revature;

import java.io.FileNotFoundException;

import com.revature.util.Configuration;

public class Driver {
	public static void main(String[] args) {
		Configuration config;
		
		try {
			config = new Configuration();
			config.addTables(person.class, Account.class);
			
			config.validate();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}

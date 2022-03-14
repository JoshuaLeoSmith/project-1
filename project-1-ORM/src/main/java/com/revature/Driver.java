package com.revature;

import com.revature.service.IServices;
import com.revature.service.ServicesImpl;

public class Driver {

	public static void main(String[] args) {
		
		IServices i = new ServicesImpl();

		//i.insert(new TesterClass("uname", "pwd", 12.22), true);
		
		i.create(TesterClass.class);
		
		i.insert(new TesterClass(1,"uname1", "pwd", 12.22), true);
		i.insert(new TesterClass(1, "uname3", "pwd", 12.22), true);

		//i.insert(new TesterClass("uname1", "pwd1", 12.55), true);
	
		
	}
}

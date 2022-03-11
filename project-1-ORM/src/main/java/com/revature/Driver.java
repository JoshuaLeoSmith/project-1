package com.revature;

import com.revature.service.IServices;
import com.revature.service.ServicesImpl;

public class Driver {

	public static void main(String[] args) {
		
		IServices i = new ServicesImpl();

		//i.insert(new TesterClass("uname", "pwd", 12.22), true);
		
		TesterClass t = (TesterClass) i.findByPk(TesterClass.class, 1);
		
		
		
	}

}

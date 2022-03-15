package com.revature;

import java.time.LocalDate;

import com.revature.service.IServices;
import com.revature.service.ServicesImpl;

public class Driver {

	static ServicesImpl Simpl = new ServicesImpl();
	public static void main(String[] args) {

		IServices i = new ServicesImpl();

		//i.insert(new TesterClass("uname", "pwd", 12.22), true);

		i.findByPk(TesterClass.class, 1);

		Simpl.create(person.class);
		Simpl.create(Account.class);

		System.out.println("It worked");

		person batman = new person("Batman");
		person superMan = new person("Superman");
		batman.setAccessLevel(0);
		batman.setAddress("Gotham");

		batman.setDob(LocalDate.of(1915, 04, 17));
		batman.setEmail("Dark@gmail.com");
		batman.setFirstName("Bruce");
		batman.setLastName("Wayne");
		batman.setPassword("bats");
		batman.setPhoneNumber("931-548-4861");
		batman.setVerified(true);

		Account BruceAccount = new Account(batman, 800000000);

		superMan.setEmail("kryptonite@yeahoo.com");
		superMan.setFirstName("Clark");
		superMan.setLastName("Kent");
		superMan.setPassword("KryptonIstheBEST!");
		superMan.setVerified(false);

		Account SuperAccount = new Account(superMan, 1200000);

		// Simpl.insert(batman, false);
		// Simpl.insert(superMan, true);

		// Simpl.insert(SuperAccount, false);
		// Simpl.insert(BruceAccount, false);


		// i.create(TesterClass.class);

		// i.insert(new TesterClass(1,"uname1", "pwd", 12.22), true);
		// i.insert(new TesterClass(1, "uname3", "pwd", 12.22), true);

		//i.insert(new TesterClass("uname1", "pwd1", 12.55), true);


	}
}

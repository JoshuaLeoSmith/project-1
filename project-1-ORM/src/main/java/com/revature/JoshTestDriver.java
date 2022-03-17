package com.revature;

import java.time.LocalDate;
import java.util.Random;

import com.revature.service.ServicesImpl;
public class JoshTestDriver {

	static ServicesImpl Simpl = new ServicesImpl();

	public static void main(String[] args) {

		
		//Simpl.create(User.class);
		//Simpl.create(UserAccounts.class);
		
		//Simpl.insert(new User("jls", LocalDate.of(2000, 02, 14), null));
		//Simpl.insert(new UserAccounts(12.44, true, 1));
		//Simpl.insert(new UserAccounts(21.55, false, 1));
		
		System.out.println(Simpl.findByPk(User.class, 1));
		
		
	}

	public static MegaClassOnlyPrimitivesAndString generateRandomObjects() {

		return new MegaClassOnlyPrimitivesAndString(randomString(), randomByte(), randomShort(),
				randomLong(), randomFloat(), randomDouble(), randomBool(), randomChar());

	}

	public static String randomString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		return salt.toString();
	}

	public static int randomInt() {
		Random rnd = new Random();
		return rnd.nextInt();
	}

	public static byte randomByte() {
		Random rnd = new Random();
		return (byte) rnd.nextInt((Byte.MAX_VALUE/2) + 1);
	}

	public static short randomShort() {
		Random rnd = new Random();
		return (short) rnd.nextInt(Short.MAX_VALUE + 1);
	}

	public static long randomLong() {
		Random rnd = new Random();
		return rnd.nextLong();
	}

	public static float randomFloat() {
		Random rnd = new Random();
		return -1000 + (rnd.nextFloat() * (1000 - -1000));
	}

	public static double randomDouble() {
		Random rnd = new Random();

		return Math.round((1000.0 - -1000.0) * rnd.nextDouble()*100.0)/100.0;
	}

	public static boolean randomBool() {
		Random rnd = new Random();
		return rnd.nextBoolean();
	}

	public static char randomChar() {
		Random rnd = new Random();
		return (char) (rnd.nextInt(26) + 'a');
	}


}

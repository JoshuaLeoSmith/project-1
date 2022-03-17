package com.revature;

import java.time.LocalDate;
import java.util.Random;
import java.time.LocalDate;

import com.revature.service.IServices;
import com.revature.service.ServicesImpl;
public class JoshTestDriver {
	
	static ServicesImpl Simpl = new ServicesImpl();
	
	public static void main(String[] args) {
		//Simpl.truncate(MegaClassOnlyPrimitivesAndString.class);
//		Simpl.create(MegaClassOnlyPrimitivesAndString.class);
//
//		for (int i=0; i<100; i++) {
//			Simpl.insert(generateRandomObjects(), true);
//		}
		Random r = new Random();
		MegaClassOnlyPrimitivesAndString tmp = new MegaClassOnlyPrimitivesAndString();
		//tmp.setB((byte)45);
		//tmp.setCharacter((char)(3+ 'a'));
		//System.out.println(Simpl.findBySimilarAttributes(tmp)); --WORKING FOR NO NULLS
		
		//Simpl.removeByPk(MegaClassOnlyPrimitivesAndString.class,4); -- WORKING FOR NO NULLS
		//Simpl.remove(MegaClassOnlyPrimitivesAndString.class, "\"id\" > 90"); -- WORKING FOR NO NULLS
		
		//System.out.println(Simpl.find(MegaClassOnlyPrimitivesAndString.class, "id < 50")); --WORKING FOR NO NULLS
		
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
		return (byte) rnd.nextInt(Byte.MAX_VALUE/2 + 1);
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
		return -1000 + rnd.nextFloat() * (1000 - -1000);
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

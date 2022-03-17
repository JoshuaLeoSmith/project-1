package com.revature;
import java.io.Serial;
import java.util.Objects;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Id;

@Entity
public class MegaClassOnlyPrimitivesAndString {

	@Id
	@Serial
	private int id;
	private String username;
	private byte b;
	private short shortNum;
	private long longNum;
	private float floatNum;
	@Column(scale=2)
	private double doubleNum;
	private boolean binary;
	private char character;
	
	
	public MegaClassOnlyPrimitivesAndString() {
		super();
	}
	public MegaClassOnlyPrimitivesAndString(String username, byte b, short shortNum, long longNum, float floatNum,
			double doubleNum, boolean binary, char character) {
		super();
		this.username = username;
		this.b = b;
		this.shortNum = shortNum;
		this.longNum = longNum;
		this.floatNum = floatNum;
		this.doubleNum = doubleNum;
		this.binary = binary;
		this.character = character;
	}
	public MegaClassOnlyPrimitivesAndString(int id, String username, byte b, short shortNum, long longNum,
			float floatNum, double doubleNum, boolean binary, char character) {
		super();
		this.id = id;
		this.username = username;
		this.b = b;
		this.shortNum = shortNum;
		this.longNum = longNum;
		this.floatNum = floatNum;
		this.doubleNum = doubleNum;
		this.binary = binary;
		this.character = character;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public byte getB() {
		return b;
	}
	public void setB(byte b) {
		this.b = b;
	}
	public short getShortNum() {
		return shortNum;
	}
	public void setShortNum(short shortNum) {
		this.shortNum = shortNum;
	}
	public long getLongNum() {
		return longNum;
	}
	public void setLongNum(long longNum) {
		this.longNum = longNum;
	}
	public float getFloatNum() {
		return floatNum;
	}
	public void setFloatNum(float floatNum) {
		this.floatNum = floatNum;
	}
	public double getDoubleNum() {
		return doubleNum;
	}
	public void setDoubleNum(double doubleNum) {
		this.doubleNum = doubleNum;
	}
	public boolean isBinary() {
		return binary;
	}
	public void setBinary(boolean binary) {
		this.binary = binary;
	}
	public char getCharacter() {
		return character;
	}
	public void setCharacter(char character) {
		this.character = character;
	}
	@Override
	public int hashCode() {
		return Objects.hash(b, binary, character, doubleNum, floatNum, id, longNum, shortNum, username);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MegaClassOnlyPrimitivesAndString other = (MegaClassOnlyPrimitivesAndString) obj;
		return b == other.b && binary == other.binary && character == other.character
				&& Double.doubleToLongBits(doubleNum) == Double.doubleToLongBits(other.doubleNum)
				&& Float.floatToIntBits(floatNum) == Float.floatToIntBits(other.floatNum) && id == other.id
				&& longNum == other.longNum && shortNum == other.shortNum && Objects.equals(username, other.username);
	}
	@Override
	public String toString() {
		return "MegaClassOnlyPrimitivesAndString [id=" + id + ", username=" + username + ", b=" + b + ", shortNum="
				+ shortNum + ", longNum=" + longNum + ", floatNum=" + floatNum + ", doubleNum=" + doubleNum
				+ ", binary=" + binary + ", character=" + character + "]";
	}
	
	
	
	
	
}

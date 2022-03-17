package com.revature;

import java.time.LocalDate;
import java.util.Objects;

import com.revature.annotations.Entity;
import com.revature.annotations.Id;

@Entity
public class MegaClass {

	@Id(serial = true)
	private int id;
	private String username;
	private byte b;
	private short shortNum;
	private long longNum;
	private float floatNum;
	private double doubleNum;
	private boolean binary;
	private char character;
	private Integer wrapperInt;
	private Double wrapperDouble;
	private LocalDate date;
	private Character characterWrapper;

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
	public Integer getWrapperInt() {
		return wrapperInt;
	}
	public void setWrapperInt(Integer wrapperInt) {
		this.wrapperInt = wrapperInt;
	}
	public Double getWrapperDouble() {
		return wrapperDouble;
	}
	public void setWrapperDouble(Double wrapperDouble) {
		this.wrapperDouble = wrapperDouble;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Character getCharacterWrapper() {
		return characterWrapper;
	}
	public void setCharacterWrapper(Character characterWrapper) {
		this.characterWrapper = characterWrapper;
	}
	@Override
	public int hashCode() {
		return Objects.hash(b, binary, character, characterWrapper, date, doubleNum, floatNum, id, longNum, shortNum,
				username, wrapperDouble, wrapperInt);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		MegaClass other = (MegaClass) obj;
		return (b == other.b) && (binary == other.binary) && (character == other.character)
				&& Objects.equals(characterWrapper, other.characterWrapper) && Objects.equals(date, other.date)
				&& (Double.doubleToLongBits(doubleNum) == Double.doubleToLongBits(other.doubleNum))
				&& (Float.floatToIntBits(floatNum) == Float.floatToIntBits(other.floatNum)) && (id == other.id)
				&& (longNum == other.longNum) && (shortNum == other.shortNum) && Objects.equals(username, other.username)
				&& Objects.equals(wrapperDouble, other.wrapperDouble) && Objects.equals(wrapperInt, other.wrapperInt);
	}
	@Override
	public String toString() {
		return "MegaClass [id=" + id + ", username=" + username + ", b=" + b + ", shortNum=" + shortNum + ", longNum="
				+ longNum + ", floatNum=" + floatNum + ", doubleNum=" + doubleNum + ", binary=" + binary
				+ ", character=" + character + ", wrapperInt=" + wrapperInt + ", wrapperDouble=" + wrapperDouble
				+ ", date=" + date + ", characterWrapper=" + characterWrapper + "]";
	}




}

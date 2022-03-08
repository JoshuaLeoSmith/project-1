package com.revature.service;

import java.io.Serializable;
import java.lang.annotation.Target;
import java.util.Objects;
import com.revature.annotations.*;

@Entity
public class TesterClass implements Serializable {

	@Column
	private String username;
	
	@Column
	private String pwd;
	
	@Column
	private double balance;
	
	@Id
	private int id;
	
	public TesterClass() {
		
	}
	
	public TesterClass(String username, String pwd, double balance, int id) {
		super();
		this.username = username;
		this.pwd = pwd;
		this.balance = balance;
		this.id = id;
	}

	public TesterClass(String username, String pwd, double balance) {
		super();
		this.username = username;
		this.pwd = pwd;
		this.balance = balance;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(balance, id, pwd, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TesterClass other = (TesterClass) obj;
		return Double.doubleToLongBits(balance) == Double.doubleToLongBits(other.balance) && id == other.id
				&& Objects.equals(pwd, other.pwd) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "TesterClass [username=" + username + ", pwd=" + pwd + ", balance=" + balance + ", id=" + id + "]";
	}
	
	
	
	
	
	
	
	
}

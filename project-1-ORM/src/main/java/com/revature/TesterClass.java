package com.revature;

import java.io.Serializable;
import java.util.Objects;
import com.revature.annotations.*;

@Entity(tableName="josh_tester_table")
public class TesterClass implements Serializable {

	@Column(columnName="username", unique=true, nullable=false)
	private String username;
	
	@Column(columnName="pwd", nullable=false)
	private String pwd;
	
	@Column(columnName="balance", scale=2)
	private double balance;
	
	@Id(columnName="id",serial=true)
	private int id;
	
	
	
	public TesterClass(int id, String username, String pwd, double balance) {
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
	public TesterClass() {
		
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

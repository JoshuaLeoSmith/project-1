package com.revature;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.annotations.ManyToMany;
import com.revature.annotations.ManyToOne;
import com.revature.annotations.OneToMany;

@Entity
public class User {
	
	@Id(serial=true)
	private int id;
	private String username;
	@Column(columnName="date-of-birth")
	private LocalDate dob;
	
	
	@JoinColumn(mappedByTable = "UserAccounts", mappedByColumn = "ownedBy")
	@OneToMany
	private ArrayList<UserAccounts> accounts;

	
	
	
	public User() {
		super();
	}

	public User(String username, LocalDate dob, ArrayList<UserAccounts> accounts) {
		super();
		this.username = username;
		this.dob = dob;
		this.accounts = accounts;
	}


	public User(int id, String username, LocalDate dob, ArrayList<UserAccounts> accounts) {
		super();
		this.id = id;
		this.username = username;
		this.dob = dob;
		this.accounts = accounts;
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

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public ArrayList<UserAccounts> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<UserAccounts> accounts) {
		this.accounts = accounts;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accounts, dob, id, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(accounts, other.accounts) && Objects.equals(dob, other.dob) && id == other.id
				&& Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", dob=" + dob + ", accounts=" + accounts + "]";
	}
	
	
	
	
}

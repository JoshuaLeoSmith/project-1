package com.revature;

import java.util.Objects;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.annotations.ManyToOne;

@Entity
public class UserAccounts {

	@Id(serial=true)
	private int id;
	@Column(scale=2)
	private double balance;
	private boolean active;
	
	@JoinColumn(mappedByTable = "User", mappedByColumn = "id")
	@ManyToOne
	private int ownedBy;

	public UserAccounts(int id, double balance, boolean active, int ownedBy) {
		super();
		this.id = id;
		this.balance = balance;
		this.active = active;
		this.ownedBy = ownedBy;
	}

	public UserAccounts(double balance, boolean active, int ownedBy) {
		super();
		this.balance = balance;
		this.active = active;
		this.ownedBy = ownedBy;
	}

	public UserAccounts() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getOwnedBy() {
		return ownedBy;
	}

	public void setOwnedBy(int ownedBy) {
		this.ownedBy = ownedBy;
	}

	@Override
	public int hashCode() {
		return Objects.hash(active, balance, id, ownedBy);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAccounts other = (UserAccounts) obj;
		return active == other.active && Double.doubleToLongBits(balance) == Double.doubleToLongBits(other.balance)
				&& id == other.id && Objects.equals(ownedBy, other.ownedBy);
	}

	@Override
	public String toString() {
		return "UserAccounts [id=" + id + ", balance=" + balance + ", active=" + active + ", ownedBy=" + ownedBy + "]";
	}


	
	
	
	
}

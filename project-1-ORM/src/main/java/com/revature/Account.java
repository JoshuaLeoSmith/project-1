package com.revature;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Exclude;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;

@Entity
public class Account {

	@Id
	int ID;
	@JoinColumn(mappedByTable = "person", mappedByColumn = "accounts")
	List<person> owners;
	@Column(default_value = "0", precision = 20, scale = 2)
	int balance;//Stored as pennies
	@Exclude
	private static Logger logBot=Logger.getLogger(Account.class);



	public Account(Account copyAccount) {
		this.owners=copyAccount.owners;
		this.balance=copyAccount.balance;
		this.ID= copyAccount.getID();
		Account.logBot.info("Account copied to new account");
	}
	public Account(person owner) {
		List<person> owners= new ArrayList<>();
		owners.add(owner);
		this.owners=owners;
		this.balance = 0;
		Account.logBot.info("New account with account "+owner.getUserID()+" as owner created with 0 balance");
	}
	public Account(List<person> owners) {
		this.owners = owners;
		this.balance = 0;
		Account.logBot.info("New account with multiple owners created with 0 balance");
	}

	public Account(person owner, int balance) {
		List<person> owners= new ArrayList<>();
		owners.add(owner);
		this.owners=owners;
		this.balance = balance;
		Account.logBot.info("New account with account "+owner.getUserID()+" as owner created with a balance of "+this.getBalance());
	}
	public Account(List<person> owners, int balance) {
		this.owners = owners;
		this.balance = balance;
		Account.logBot.info("New account with multiple owners created with a balance of "+this.getBalance());
	}



	public Account transferTo(Account otherAccount, double amount) {
		int amountInPennies=(int)amount*100;
		this.subtractFromBalance(amountInPennies);
		otherAccount.addToBalance(amountInPennies);

		Account.logBot.info("Account transfered $"+amount+" money from "+this.ID+" to account"+otherAccount.ID);
		return this;
	}
	public Account transferFrom(Account otherAccount, double amount) {
		int amountInPennies=(int)amount*100;
		this.addToBalance(amountInPennies);
		otherAccount.subtractFromBalance(amountInPennies);
		Account.logBot.info("Account transfered $"+amount+" money to "+this.ID+" from account"+otherAccount.ID);
		return this;
	}
	private int subtractFromBalance(int amount) {

		if(this.balance>=amount) {
			this.balance-=amount;

			Account.logBot.info("Reduced "+amount +" from account "+this.ID);

		}
		else {
			Account.logBot.info("The value of account "+this.ID+" would go negative if ran");

		}
		return this.balance;

	}
	private int addToBalance(int amount) {

		this.balance+=amount;
		Account.logBot.info("Added "+amount +" to account "+this.ID);
		return this.balance;
	}


	public double withdraw(double amount)  {
		int withdrawAmount=(int)(amount*100);
		this.subtractFromBalance(withdrawAmount);
		amount=Math.floor(amount*100.0)/100.0;
		Account.logBot.info("Withdrew $"+amount+" from account "+this.ID);
		return this.getBalance();
	}
	public double deposit(double amount) {
		int withdrawAmount=(int) (amount*100);
		this.addToBalance(withdrawAmount);
		Account.logBot.info("Deposited $"+amount+" into account "+this.ID);
		return this.getBalance();
	}

	public boolean withdrawAll() {
		double currentValue=this.getBalance();
		this.withdraw(this.getBalance());
		Account.logBot.info("Withdrew all $"+currentValue+" from account "+this.ID);
		return true;
	}
	public boolean closeAccount() {
		this.balance=0;
		this.owners=null;


		Account.logBot.info("Account "+this.ID+" closed.");//Need some way to force this to stop?
		return true;
	}

	public Account addOwner(person newOwner) {
		this.owners.add(newOwner);
		Account.logBot.info("Added "+newOwner.getUserID()+" as owner of account "+this.ID);
		return this;
	}
	public Account removeOwner(person badOwner) {
		this.owners.add(badOwner);
		Account.logBot.info("Removed "+badOwner.getUserID()+" as owner of account "+this.ID);
		return this;
	}

	public Account submitNew() {
		return this;
	}

	public Account update() {
		//new Account();


		return this;
	}


	public int getID() {
		return this.ID;
	}
	public List<person> getOwners() {
		return this.owners;
	}
	public double getBalance() {
		double cents=(this.balance%100)/100.0;
		double dollars=this.balance/100;
		double balanceInDollars=(dollars+cents);

		//System.out.println(balance);
		return Math.round(balanceInDollars*100.0)/100.0;
	}

	public int getIntBalance() {
		return this.balance;
	}


	public void setID(int iD) {
		this.ID = iD;
	}
	public void setOwners(List<person> owners) {
		this.owners = owners;
	}
	public void setBalance(int balance) {

		this.balance = balance;
	}


	@Override
	public int hashCode() {
		return Objects.hash(this.ID, this.balance, this.owners);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (this.getClass() != obj.getClass())) {
			return false;
		}
		Account other = (Account) obj;
		return (this.ID == other.ID) && (this.balance == other.balance) && Objects.equals(this.owners, other.owners);
	}
	@Override
	public String toString() {
		return "Account [ID=" + this.ID + ", owners=" + this.owners + ", balance=" + this.balance + "]";
	}



}

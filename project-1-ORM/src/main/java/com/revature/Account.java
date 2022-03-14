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
import com.revature.annotations.ManyToMany;

@Entity
public class Account {

	@Id
	int ID;
	@JoinColumn(mappedByTable = "person", mappedByColumn = "accounts")
	@ManyToMany
	List<person> owners;
	@Column(default_value = "0", precision = 20, scale = 2)
	int balance;//Stored as pennies
	@Exclude
	private static Logger logBot=Logger.getLogger(Account.class);



	public Account(Account copyAccount) {
		owners=copyAccount.owners;
		balance=copyAccount.balance;
		ID= copyAccount.getID();
		Account.logBot.info("Account copied to new account");
	}
	public Account(person owner) {
		List<person> owners= new ArrayList<>();
		owners.add(owner);
		this.owners=owners;
		balance = 0;
		Account.logBot.info("New account with account "+owner.getUserID()+" as owner created with 0 balance");
	}
	public Account(List<person> owners) {
		this.owners = owners;
		balance = 0;
		Account.logBot.info("New account with multiple owners created with 0 balance");
	}

	public Account(person owner, int balance) {
		List<person> owners= new ArrayList<>();
		owners.add(owner);
		this.owners=owners;
		this.balance = balance;
		Account.logBot.info("New account with account "+owner.getUserID()+" as owner created with a balance of "+getBalance());
	}
	public Account(List<person> owners, int balance) {
		this.owners = owners;
		this.balance = balance;
		Account.logBot.info("New account with multiple owners created with a balance of "+getBalance());
	}



	public Account transferTo(Account otherAccount, double amount) {
		int amountInPennies=(int)amount*100;
		subtractFromBalance(amountInPennies);
		otherAccount.addToBalance(amountInPennies);

		Account.logBot.info("Account transfered $"+amount+" money from "+ID+" to account"+otherAccount.ID);
		return this;
	}
	public Account transferFrom(Account otherAccount, double amount) {
		int amountInPennies=(int)amount*100;
		addToBalance(amountInPennies);
		otherAccount.subtractFromBalance(amountInPennies);
		Account.logBot.info("Account transfered $"+amount+" money to "+ID+" from account"+otherAccount.ID);
		return this;
	}
	private int subtractFromBalance(int amount) {

		if(balance>=amount) {
			balance-=amount;

			Account.logBot.info("Reduced "+amount +" from account "+ID);

		}
		else {
			Account.logBot.info("The value of account "+ID+" would go negative if ran");

		}
		return balance;

	}
	private int addToBalance(int amount) {

		balance+=amount;
		Account.logBot.info("Added "+amount +" to account "+ID);
		return balance;
	}


	public double withdraw(double amount)  {
		int withdrawAmount=(int)(amount*100);
		subtractFromBalance(withdrawAmount);
		amount=Math.floor(amount*100.0)/100.0;
		Account.logBot.info("Withdrew $"+amount+" from account "+ID);
		return getBalance();
	}
	public double deposit(double amount) {
		int withdrawAmount=(int) (amount*100);
		addToBalance(withdrawAmount);
		Account.logBot.info("Deposited $"+amount+" into account "+ID);
		return getBalance();
	}

	public boolean withdrawAll() {
		double currentValue=getBalance();
		withdraw(getBalance());
		Account.logBot.info("Withdrew all $"+currentValue+" from account "+ID);
		return true;
	}
	public boolean closeAccount() {
		balance=0;
		owners=null;


		Account.logBot.info("Account "+ID+" closed.");//Need some way to force this to stop?
		return true;
	}

	public Account addOwner(person newOwner) {
		owners.add(newOwner);
		Account.logBot.info("Added "+newOwner.getUserID()+" as owner of account "+ID);
		return this;
	}
	public Account removeOwner(person badOwner) {
		owners.add(badOwner);
		Account.logBot.info("Removed "+badOwner.getUserID()+" as owner of account "+ID);
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
		return ID;
	}
	public List<person> getOwners() {
		return owners;
	}
	public double getBalance() {
		double cents=(balance%100)/100.0;
		double dollars=balance/100;
		double balanceInDollars=(dollars+cents);

		//System.out.println(balance);
		return Math.round(balanceInDollars*100.0)/100.0;
	}

	public int getIntBalance() {
		return balance;
	}


	public void setID(int iD) {
		ID = iD;
	}
	public void setOwners(List<person> owners) {
		this.owners = owners;
	}
	public void setBalance(int balance) {

		this.balance = balance;
	}


	@Override
	public int hashCode() {
		return Objects.hash(ID, balance, owners);
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
		return (ID == other.ID) && (balance == other.balance) && Objects.equals(owners, other.owners);
	}
	@Override
	public String toString() {
		return "Account [ID=" + ID + ", owners=" + owners + ", balance=" + balance + "]";
	}



}

package com.revature;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;


public class Account {
	
	int ID;
	List<person> owners;
	int balance;//Stored as pennies
	private static Logger logBot=Logger.getLogger(Account.class);
	
	

	public Account(Account copyAccount) {
		this.owners=copyAccount.owners;
		this.balance=copyAccount.balance;
		this.ID= copyAccount.getID();
		logBot.info("Account copied to new account");
	}
	public Account(person owner) {
		super();
		List<person> owners= new ArrayList<>();
		owners.add(owner);
		this.owners=owners;
		this.balance = 0;
		logBot.info("New account with account "+owner.getUserID()+" as owner created with 0 balance");
	}	
	public Account(List<person> owners) {
		super();
		this.owners = owners;
		this.balance = 0;
		logBot.info("New account with multiple owners created with 0 balance");
	}

	public Account(person owner, int balance) {
		super();
		List<person> owners= new ArrayList<>();
		owners.add(owner);
		this.owners=owners;
		this.balance = balance;
		logBot.info("New account with account "+owner.getUserID()+" as owner created with a balance of "+this.getBalance());
	}	
	public Account(List<person> owners, int balance) {
		super();
		this.owners = owners;
		this.balance = balance;
		logBot.info("New account with multiple owners created with a balance of "+this.getBalance());
	}
	
	
	
	public Account transferTo(Account otherAccount, double amount) {
		int amountInPennies=(int)amount*100;		
		this.subtractFromBalance(amountInPennies);
		otherAccount.addToBalance(amountInPennies);
		
		logBot.info("Account transfered $"+amount+" money from "+this.ID+" to account"+otherAccount.ID);
		return this;
	}	
	public Account transferFrom(Account otherAccount, double amount) {
		int amountInPennies=(int)amount*100;		
		this.addToBalance(amountInPennies);
		otherAccount.subtractFromBalance(amountInPennies);
		logBot.info("Account transfered $"+amount+" money to "+this.ID+" from account"+otherAccount.ID);
		return this;
	}	
	private int subtractFromBalance(int amount) {
		
		if(this.balance>=amount) {
			this.balance-=amount;
			
			logBot.info("Reduced "+amount +" from account "+this.ID);
			
		}	
		else {
			logBot.info("The value of account "+this.ID+" would go negative if ran");
			
		}
		return balance;	
		
	}
	private int addToBalance(int amount) {
		
		this.balance+=amount;		
		logBot.info("Added "+amount +" to account "+this.ID);
		return balance;		
	}
	
	
	public double withdraw(double amount)  {
		int withdrawAmount=(int)(amount*100);
		this.subtractFromBalance(withdrawAmount);
		amount=Math.floor(amount*100.0)/100.0;
		logBot.info("Withdrew $"+amount+" from account "+this.ID);
		return this.getBalance();
	}
	public double deposit(double amount) {
		int withdrawAmount=(int) (amount*100);
		this.addToBalance(withdrawAmount);	
		logBot.info("Deposited $"+amount+" into account "+this.ID);
		return this.getBalance();
	}
	
	public boolean withdrawAll() {
		double currentValue=this.getBalance();
		this.withdraw(this.getBalance());	
		logBot.info("Withdrew all $"+currentValue+" from account "+this.ID);
		return true;		
	}
	public boolean closeAccount() {
				this.balance=0;
		this.owners=null;
		
		
		logBot.info("Account "+this.ID+" closed.");//Need some way to force this to stop? 
		return true;
	}
	
	public Account addOwner(person newOwner) {
		this.owners.add(newOwner);
		logBot.info("Added "+newOwner.getUserID()+" as owner of account "+this.ID);
		return this;
	}
	public Account removeOwner(person badOwner) {
		this.owners.add(badOwner);
		logBot.info("Removed "+badOwner.getUserID()+" as owner of account "+this.ID);
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
		balanceInDollars=Math.round(balanceInDollars*100.0)/100.0;
		//System.out.println(balance);
		return balanceInDollars;
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Account other = (Account) obj;
		return ID == other.ID && balance == other.balance && Objects.equals(owners, other.owners);
	}
	@Override
	public String toString() {
		return "Account [ID=" + ID + ", owners=" + owners + ", balance=" + balance + "]";
	}
	
	

}

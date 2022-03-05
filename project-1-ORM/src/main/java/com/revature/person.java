package com.revature;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.revature.annotations.*;



public class person {

	@Exclude
	private static Logger logBot=Logger.getLogger(person.class);
	@Exclude
	protected int maxAccessLevel=2;
	@Exclude
	protected int minAccessLevel=0;
	
	@Id
	protected int userID;
	protected String username;
	protected String passwordHash;
	protected int accessLevel; //0=customer, 1=employee, 2=admin
	protected boolean verified=false;	
	protected String email;
	protected String phoneNumber;
	protected String fName;
	protected String lName;
	protected String address;
	protected LocalDate dob;
	@Exclude
	protected List<Account> accounts;
	
	
	public person() {
		logBot.info("Created blank User");		
	}
	
	public person(person oldPerson) {
		super();
		this.userID=oldPerson.userID;		
		this.username = oldPerson.username;
		this.passwordHash = oldPerson.passwordHash;
		this.accessLevel = oldPerson.accessLevel;
		this.email = oldPerson.email;
		this.phoneNumber = oldPerson.phoneNumber;
		this.fName = oldPerson.fName;
		this.lName = oldPerson.lName;
		this.lName = oldPerson.lName;
		this.address = oldPerson.address;
		this.accounts = oldPerson.accounts;
		
		logBot.info("Copied user "+oldPerson.userID);
	}

	public person(String username) {
		this.username = username;
		this.accessLevel = minAccessLevel;		
		//this.passwordHash=this.Hasher(password);
			
		logBot.info("Created a new user with username "+username);
	}

	public person(String username, int accessLevel) {
		this.username = username;
		this.accessLevel = accessLevel;		
		//this.passwordHash = this.Hasher(password);
		logBot.info("Created a new user with username "+username+" with access level "+accessLevel);
	}
	
	
	private String Hasher(String phrase) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(phrase.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
			
		} 
		catch (NoSuchAlgorithmException e) {
			logBot.warn("No algorithm to hash the passwords");			
			e.printStackTrace();
		}
		return null;
	}
	
	public int upgradeSecurityLevel() {
		this.setAccessLevel(this.accessLevel++);	
		logBot.info("Increased securtiy level for user "+this.userID);
		return this.accessLevel;
	}
	
	public int downgradeSecurityLevel() {
		this.setAccessLevel(this.accessLevel--);	
		logBot.info("Decreased securtiy level for user "+this.userID);
		return this.accessLevel;
	}
	
	public boolean Fire() {
		this.accessLevel=minAccessLevel;
		//boolean finished;
		logBot.info("User "+this.userID+" has beed fired and reduced to a customer");
		return true;
	}
	
	public boolean approveUser() {
		this.setVerified(true);
		logBot.info("Approved account "+this.userID);
		
		return true;
	}
	public boolean banUser() {
		this.setVerified(false);
		
		logBot.info("Banned account "+this.userID);
		
		return true;	
	}
	
	public boolean removeUser() {
		
		for(Account acc : this.accounts) {
			if (acc.getBalance()>0 && acc.getOwners().size()==1) {
				logBot.warn("Can't delete an user with an account with a positive balanace");
				return false;
			}
		}
		
		//this.userID=void; //Keep the ID to remove it from the database
		this.username = null;
		this.passwordHash = null;
		this.accessLevel = 0;
		this.email = null;
		this.phoneNumber = null;
		this.fName = null;
		this.lName = null;
		this.lName = null;
		this.address = null;
		this.accounts = null;
		
		
		
		logBot.info("Removed user "+this.userID);
		return true;
		
	}
	
	public boolean addAcount(Account acc) {
		List<Account> accounts=this.getAccounts();
		//System.out.println(accounts.toString());
		if(accounts==null) {
			List<Account> newAccounts=new ArrayList<>();
			newAccounts.add(acc);
			this.setAccounts(newAccounts);
			return true;
		}
		else if(!accounts.contains(acc)) {
			accounts.add(acc);
			this.setAccounts(accounts);
			logBot.info("Added account "+acc.getID()+"to user "+this.userID);
			return true;
		}
		else {
			logBot.info("User "+this.userID+"already has access to account "+acc.getID());
			return false;
		}
		
	}
	
	public boolean removeAcount(Account acc) {		
		List<Account> accounts=this.getAccounts();
		if(accounts.contains(acc)) {
			accounts.remove(acc);
			this.setAccounts(accounts);	
			
			logBot.info("Removed account "+acc.getID()+" from user "+this.userID);
			return true;		
		}
		else {
			logBot.info("Account "+acc.getID()+" does not belong to user "+this.userID);
			return false;
		}
	}
	
	
	public static person Login(String username, String password) {
		person bob=new person(username);
		bob.setPassword(password);
		if(bob!=null) {
			logBot.info("Logged in with username "+username);
		}
		return bob;
	}
	
	public person createNew() {
		person bob=null;
		
			
		return bob;
	}
	
	public person update() {
		person bob = null;
		return bob;
	}
	
	public int getUserID() {
		return userID;
	}

	public String getUsername() {
		return username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public boolean isVerified() {
		return verified;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getFirstName() {
		return fName;
	}

	public String getLastName() {
		return lName;
	}

	public String getAddress() {
		return address;
	}

	public List<Account> getAccounts() {
		return this.accounts;
	}

	public LocalDate getDob() {
		return dob;
	}
	
	
	
	
	

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	} //This needs to be set by the dao

	public void setUsername(String username) {
				this.username = username;
	}

	public void setPassword(String password) {
		this.passwordHash = this.Hasher(password);
	}
	
	public void setPasswordHash(String password) {
		this.passwordHash = (password);
	}

	public void setAccessLevel(int accessLevel) {
		
		this.accessLevel = accessLevel;			
				
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setFirstName(String fName) {
		this.fName = fName;
	}

	public void setLastName(String lName) {
		this.lName = lName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	
	
	@Override
	public int hashCode() {
		return Objects.hash(accessLevel, accounts, address, dob, email, fName, lName, maxAccessLevel, minAccessLevel,
				passwordHash, phoneNumber, userID, username, verified);
	}

	@Override
	public String toString() {
		return "User [maxAccessLevel=" + maxAccessLevel + ", minAccessLevel=" + minAccessLevel + ", userID=" + userID
				+ ", username=" + username + ", passwordHash=" + passwordHash + ", accessLevel=" + accessLevel
				+ ", verified=" + verified + ", email=" + email + ", phoneNumber=" + phoneNumber + ", fName=" + fName
				+ ", lName=" + lName + ", address=" + address + ", dob=" + dob + ", accounts=" + accounts + "]";
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
		person other = (person) obj;
		return accessLevel == other.accessLevel && Objects.equals(accounts, other.accounts)
				&& Objects.equals(address, other.address) && Objects.equals(dob, other.dob)
				&& Objects.equals(email, other.email) && Objects.equals(fName, other.fName)
				&& Objects.equals(lName, other.lName) && maxAccessLevel == other.maxAccessLevel
				&& minAccessLevel == other.minAccessLevel && Objects.equals(passwordHash, other.passwordHash)
				&& Objects.equals(phoneNumber, other.phoneNumber) && userID == other.userID
				&& Objects.equals(username, other.username) && verified == other.verified;
	}

	
	
	
	
}


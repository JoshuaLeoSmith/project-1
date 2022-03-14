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

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Exclude;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.annotations.ManyToMany;


@Entity
public class person {

	@Exclude
	private static Logger logBot=Logger.getLogger(person.class);
	@Exclude
	protected int maxAccessLevel=2;
	@Exclude
	protected int minAccessLevel=0;

	@Id(serial=true)
	protected int userID;
	@Column(unique=true,nullable=false)
	protected String username;
	@Column(nullable=false)
	protected String passwordHash;
	@Column(default_value="0")
	protected int accessLevel; //0=customer, 1=employee, 2=admin
	@Column(default_value="false")
	protected boolean verified=false;
	@Column(unique=true)
	protected String email;
	@Column(length=15)
	protected String phoneNumber;
	@Column(columnName="First Name")
	protected String fName;
	protected String lName;
	protected String address;
	protected LocalDate dob;
	@Column(precision=5, scale=2, default_value="0" )
	protected double balance;
	@Column(serial=true )
	protected int testSerial;

	@JoinColumn(mappedByTable = "Account", mappedByColumn = "owners")
	@ManyToMany
	protected List<Account> accounts;


	public person() {
		person.logBot.info("Created blank User");
	}

	public person(person oldPerson) {
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

		person.logBot.info("Copied user "+oldPerson.userID);
	}

	public person(String username) {
		this.username = username;
		this.accessLevel = this.minAccessLevel;
		//this.passwordHash=this.Hasher(password);

		person.logBot.info("Created a new user with username "+username);
	}

	public person(String username, int accessLevel) {
		this.username = username;
		this.accessLevel = accessLevel;
		//this.passwordHash = this.Hasher(password);
		person.logBot.info("Created a new user with username "+username+" with access level "+accessLevel);
	}


	private String Hasher(String phrase) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(phrase.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);

		}
		catch (NoSuchAlgorithmException e) {
			person.logBot.warn("No algorithm to hash the passwords");
			e.printStackTrace();
		}
		return null;
	}

	public int upgradeSecurityLevel() {
		this.setAccessLevel(this.accessLevel++);
		person.logBot.info("Increased securtiy level for user "+this.userID);
		return this.accessLevel;
	}

	public int downgradeSecurityLevel() {
		this.setAccessLevel(this.accessLevel--);
		person.logBot.info("Decreased securtiy level for user "+this.userID);
		return this.accessLevel;
	}

	public boolean Fire() {
		this.accessLevel=this.minAccessLevel;
		//boolean finished;
		person.logBot.info("User "+this.userID+" has beed fired and reduced to a customer");
		return true;
	}

	public boolean approveUser() {
		this.setVerified(true);
		person.logBot.info("Approved account "+this.userID);

		return true;
	}
	public boolean banUser() {
		this.setVerified(false);

		person.logBot.info("Banned account "+this.userID);

		return true;
	}

	public boolean removeUser() {

		for(Account acc : this.accounts) {
			if ((acc.getBalance()>0) && (acc.getOwners().size()==1)) {
				person.logBot.warn("Can't delete an user with an account with a positive balanace");
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



		person.logBot.info("Removed user "+this.userID);
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
			person.logBot.info("Added account "+acc.getID()+"to user "+this.userID);
			return true;
		}
		else {
			person.logBot.info("User "+this.userID+"already has access to account "+acc.getID());
			return false;
		}

	}

	public boolean removeAcount(Account acc) {
		List<Account> accounts=this.getAccounts();
		if(accounts.contains(acc)) {
			accounts.remove(acc);
			this.setAccounts(accounts);

			person.logBot.info("Removed account "+acc.getID()+" from user "+this.userID);
			return true;
		}
		else {
			person.logBot.info("Account "+acc.getID()+" does not belong to user "+this.userID);
			return false;
		}
	}


	public static person Login(String username, String password) {
		person bob=new person(username);
		bob.setPassword(password);
		if(bob!=null) {
			person.logBot.info("Logged in with username "+username);
		}
		return bob;
	}

	public person createNew() {
		return null;
	}

	public person update() {
		return null;
	}

	public int getUserID() {
		return this.userID;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPasswordHash() {
		return this.passwordHash;
	}

	public int getAccessLevel() {
		return this.accessLevel;
	}

	public boolean isVerified() {
		return this.verified;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getFirstName() {
		return this.fName;
	}

	public String getLastName() {
		return this.lName;
	}

	public String getAddress() {
		return this.address;
	}

	public List<Account> getAccounts() {
		return this.accounts;
	}

	public LocalDate getDob() {
		return this.dob;
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
		return Objects.hash(this.accessLevel, this.accounts, this.address, this.dob, this.email, this.fName, this.lName, this.maxAccessLevel, this.minAccessLevel,
				this.passwordHash, this.phoneNumber, this.userID, this.username, this.verified);
	}

	@Override
	public String toString() {
		return "User [maxAccessLevel=" + this.maxAccessLevel + ", minAccessLevel=" + this.minAccessLevel + ", userID=" + this.userID
				+ ", username=" + this.username + ", passwordHash=" + this.passwordHash + ", accessLevel=" + this.accessLevel
				+ ", verified=" + this.verified + ", email=" + this.email + ", phoneNumber=" + this.phoneNumber + ", fName=" + this.fName
				+ ", lName=" + this.lName + ", address=" + this.address + ", dob=" + this.dob + ", accounts=" + this.accounts + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (this.getClass() != obj.getClass())) {
			return false;
		}
		person other = (person) obj;
		return (this.accessLevel == other.accessLevel) && Objects.equals(this.accounts, other.accounts)
				&& Objects.equals(this.address, other.address) && Objects.equals(this.dob, other.dob)
				&& Objects.equals(this.email, other.email) && Objects.equals(this.fName, other.fName)
				&& Objects.equals(this.lName, other.lName) && (this.maxAccessLevel == other.maxAccessLevel)
				&& (this.minAccessLevel == other.minAccessLevel) && Objects.equals(this.passwordHash, other.passwordHash)
				&& Objects.equals(this.phoneNumber, other.phoneNumber) && (this.userID == other.userID)
				&& Objects.equals(this.username, other.username) && (this.verified == other.verified);
	}





}


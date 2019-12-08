package entity;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
//import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import entity.Account;

import java.sql.Timestamp;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import entity.ProjectHibernate_Messages;



@Entity
@Table(name="User")

public class User {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	@Column(name="CLIENT_ID_PRIMARY_KEY")
	private String userIdPrimarKey;
	
	
	
	@Column(name="NAME")
	private String name;
	
	
	@Column(name="USER_NAME")
	private String userName;
	
	
	@Column(name="PASSWORD")
	private String password;
	
	
	@Column(name="AGE")
	private int age;
	
	@Column(name="CREATION_TIME")
	private Timestamp creationTimestamp;
	
	@Column(name="LAST_UPDATE_TIME")
	private Timestamp lastUpdateTimestamp;
	
	@Column(name="LAST_ACCESSED_TIME")
	private Timestamp lastAccessedTimestamp;
	
	
	@ManyToMany(cascade=CascadeType.ALL)  
    @JoinTable(name="CLIENT_JOIN_ACCOUNT", joinColumns=@JoinColumn(name="CLIENT_FK"), inverseJoinColumns=@JoinColumn(name="ACCOUNT_FK"))  
	private List<Account> accounts;
	
	
	
	public User(){
		
		Calendar calendar = Calendar.getInstance();
		setName(ProjectHibernate_Messages.UNKNOWN);
		setUserName(ProjectHibernate_Messages.UNKNOWN);
		setPassword(ProjectHibernate_Messages.UNKNOWN);
		setCreationTimestamp(new Timestamp(calendar.getTimeInMillis()));
		setLastUpdateTimestamp(new Timestamp(calendar.getTimeInMillis()));
		setLastAccessedTimestamp(new Timestamp(calendar.getTimeInMillis()));
		setAge(0);
		
		
	}
	
	
	public void   setName(String newName){name = newName;}
	public String getName(){ return name;  }
	
	public void   setUserName(String newUserName){userName = newUserName;}
	public String getUserName(){return userName; }
	
	public void   setPassword(String newPass){password = newPass;}
	public String getPassword(){return password;}
	
	public void   setAge(int newAge){age = newAge;}
	public int    getAge(){return age;}
	
	
	public String getUserID(){
		
		return userIdPrimarKey;
	}
	
	
	public void setCreationTimestamp(Timestamp creationTimestamp) {
		
		this.creationTimestamp = creationTimestamp;
	}
	
	public Timestamp getCreationTimestamp() {
		
		return creationTimestamp;
	}
	
	
	public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp){
		
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}
	
	public Timestamp getLastUpdateTimestamp(){
		
		return lastUpdateTimestamp;
	}
	
	
	public void setLastAccessedTimestamp(Timestamp lastAccessedTimestamp){
		
		this.lastAccessedTimestamp = lastAccessedTimestamp;
	}
	
	public Timestamp getLastAccessedTimestamp(){
		
		return lastAccessedTimestamp;
	}
	
	
	public void upDateUser(User user){
		
		setName(user.getName());
		setUserName(user.getUserName());
		setPassword(user.getPassword());
		setAge(user.getAge());
		setCreationTimestamp(user.getCreationTimestamp());
		setLastUpdateTimestamp(user.getLastUpdateTimestamp());
		setLastAccessedTimestamp(user.getLastAccessedTimestamp());
	}
	
	
	public User copyUser(){
		User newUser = new User();
		newUser.upDateUser(this);
		return newUser;
	}
	
	
	public List<Account> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	public void addAccount(Account account) {
		getAccounts().add(account);	
	}
	

}

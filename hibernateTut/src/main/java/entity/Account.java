package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import entity.ProjectHibernate_Messages;

@Entity
@Table(name="Account")

public class Account {
	
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	@Column(name="Account_PRIMARY_KEY")
	private String accountPrimaryKey;
	
	
	@Column(name="NAME")
	private String name;
	
	public Account() {
		setName(ProjectHibernate_Messages.UNKNOWN);
	}
	
	public Account(String name) {
		setName(name);
	}
	
	public String getCounterPrimaryKey() {
		return accountPrimaryKey;
	}
	
	
	public void setCounterPrimaryKey(String counterPrimaryKey) {
		this.accountPrimaryKey = counterPrimaryKey;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	
	

}

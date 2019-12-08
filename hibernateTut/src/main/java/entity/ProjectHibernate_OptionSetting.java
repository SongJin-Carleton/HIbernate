package entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name="OPTION_SETTING")
public class ProjectHibernate_OptionSetting {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	@Column(name="OPTION_SETTING_PRIMARY_KEY")
	private String optionSettingPrimaryKey;
	
	@Column(name="ACTIVE")
	private boolean active;
	
	@Column(name="SECURITY_LEVEL")
	private int securityLevel;
	
	public static int HIGH_SECURITY_LEVEL = 2;
	public static int MEDIUM_SECURITY_LEVEL = 1;
	public static int LOW_SECURITY_LEVEL = 0;
	
	public ProjectHibernate_OptionSetting() {
		setActive();
		setSecurityLevel(HIGH_SECURITY_LEVEL);	
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setActive() {
		setActive(true);
	}
	
	public void setInactive() {
		setActive(false);
	}
	
	public int getSecurityLevel() {
		return securityLevel;
	}
	
	public void setSecurityLevel(int securityLevel) {
		this.securityLevel = securityLevel;
	}
	
	public String getOptionSettingPrimaryKey() {
		return optionSettingPrimaryKey;
	}

	public void setOptionSettingPrimaryKey(String optionSettingPrimaryKey) {
		this.optionSettingPrimaryKey = optionSettingPrimaryKey;
	}

}

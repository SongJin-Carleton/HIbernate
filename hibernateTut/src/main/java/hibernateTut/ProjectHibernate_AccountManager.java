package hibernateTut;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;


import hibernateTut.ProjectHibernate_LoggerManager;
import entity.ProjectHibernate_Messages;
import hibernateTut.Testjdbc;



public class ProjectHibernate_AccountManager  extends
            ProjectHibernate_AbstractDatabaseManager {

	private static String ACCOUNT_TABLE_NAME = "ACCOUNT";
	private static String ACCOUNT_CLASS_NAME = "Account";
	
	private static String SELECT_ALL_ACCOUNTS = "from "
	+ ACCOUNT_TABLE_NAME + " as optionSetting";
	
	private static final String DROP_TABLE_SQL = "drop table "
	+ ACCOUNT_TABLE_NAME + ";";
	
	private static String SELECT_NUMBER_ACCOUNTS = "select count (*) from "
	+ ACCOUNT_CLASS_NAME;
	
	private static final String CREATE_TABLE_SQL = "create table ACCOUNT(ACCOUNT_PRIMARY_KEY char(36) primary key not null, "
	+ "NAME tinytext);";
	
	private static ProjectHibernate_AccountManager manager;
	
	ProjectHibernate_AccountManager() {
		super();
	}
	
	public static ProjectHibernate_AccountManager getDefault() {
	
	if (manager == null) {
	manager = new ProjectHibernate_AccountManager();
	}
	return manager;
	}
	
	public String getClassName() {
	return ACCOUNT_CLASS_NAME;
	}
	
	@Override
	public boolean setupTable() {
		Testjdbc.executeSQLQuery(DROP_TABLE_SQL);
		return Testjdbc.executeSQLQuery(CREATE_TABLE_SQL);
	}

	public String getTableName() {
		return ACCOUNT_TABLE_NAME;
	}
	
public synchronized int getNumberOfAccounts() {
		
		Session session = null;
		Long aLong;

		try {
			session = Testjdbc.getCurrentSession();
			Query query = session
					.createQuery(SELECT_NUMBER_ACCOUNTS);
			aLong = (Long) query.uniqueResult();
			return aLong.intValue();
		} catch (ObjectNotFoundException exception) {
			ProjectHibernate_LoggerManager.current().error(this,
					ProjectHibernate_Messages.METHOD_GET_NUMBER_OF_ADDRESSES,
					ProjectHibernate_Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return 0;
		} catch (HibernateException exception) {
			ProjectHibernate_LoggerManager.current().error(this,
					ProjectHibernate_Messages.METHOD_GET_NUMBER_OF_ADDRESSES,
					ProjectHibernate_Messages.HIBERNATE_FAILED, exception);
			return 0;
		} catch (RuntimeException exception) {
			ProjectHibernate_LoggerManager.current().error(this,
					ProjectHibernate_Messages.METHOD_GET_NUMBER_OF_ADDRESSES,
					ProjectHibernate_Messages.GENERIC_FAILED, exception);
			return 0;
		} finally {
			closeSession();
		}
	}
}

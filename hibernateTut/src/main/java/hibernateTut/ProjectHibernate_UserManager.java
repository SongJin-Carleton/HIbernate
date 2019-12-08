package hibernateTut;

import java.util.List;



import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.JDBCConnectionException;

import hibernateTut.ProjectHibernate_AbstractDatabaseManager;
import hibernateTut.Testjdbc;


import hibernateTut.ProjectHibernate_LoggerManager;
import entity.ProjectHibernate_Messages;
import entity.User;

public class ProjectHibernate_UserManager extends
             ProjectHibernate_AbstractDatabaseManager {

	private static String CLIENT_TABLE_NAME = "User";
	private static String CLIENT_ACCOUNT_JOIN_TABLE_NAME = "CLIENT_JOIN_ACCOUNT";
	private static String CLIENT_CLASS_NAME = "User";

	private static String SELECT_ALL_CLIENTS = "from "
			+ CLIENT_CLASS_NAME + " as client";
	private static String SELECT_CLIENT_WITH_EMAIL_ADDRESS = "from "
			+ CLIENT_CLASS_NAME + " as client where client.emailAddress = :email";
	private static String SELECT_CLIENT_WITH_USERID = "from "
			+ CLIENT_CLASS_NAME + "  as client where client.userId = :userId";
	private static String SELECT_CLIENT_WITH_EMAIL_PASSWORD = "from "
			+ CLIENT_CLASS_NAME
			+ " as client where client.emailAddress = :email and client.password = :password";
	/* private static String DELETE_CLIENT_WITH_PRIMARY_KEY = " delete from "
			+ CLIENT_CLASS_NAME + " as client where client.id = ?"; */
	/* private static String SELECT_ALL_CLIENT_EMAIL_ADDRESSES = "select client.emailAddress from "
			+ CLIENT_CLASS_NAME + " as client"; */

	private static final String DROP_TABLE_SQL = "drop table "
			+ CLIENT_TABLE_NAME + ";";
	
	private static final String DROP_JOIN_TABLE_SQL = "drop table "
			+ CLIENT_ACCOUNT_JOIN_TABLE_NAME + ";";
	
	private static String SELECT_NUMBER_CLIENTS = "select count (*) from "
		+ CLIENT_CLASS_NAME;
	
	//private static final String CREATE_TABLE_SQL = "create table " + CLIENT_TABLE_NAME + "(CLIENT_ID_PRIMARY_KEY int primary key not null AUTO_INCREMENT,"
			
			private static final String CREATE_TABLE_SQL = "create table " + CLIENT_TABLE_NAME + "(CLIENT_ID_PRIMARY_KEY char(36) primary key not null,"
			+ "OPTION_SETTING_FK char(36),"
			+ "FIRST_NAME tinytext, LAST_NAME tinytext, EMAIL_ADDRESS tinytext, PASSWORD tinytext, "
			+ "PHONE_NUMBER tinytext, LANGUAGE varchar(2), "
			/* TIMESTAMP has a range of '1970-01-01 00:00:01' UTC 
			 * to '2038-01-19 03:14:07' UTC (see doc). The default value must 
			 * be within that range. 
			 * Side note, if you want to insert NULLS:
			 * 	CREATION_TIME timestamp NULL DEFAULT NULL
			 * pr not nulls then a value
			 * 	ts2 TIMESTAMP DEFAULT '1970-01-01 00:00:01'
			 */
			+ "CREATION_TIME timestamp NULL DEFAULT NULL, LAST_UPDATE_TIME timestamp NULL DEFAULT NULL, LAST_ACCESSED_TIME timestamp NULL DEFAULT NULL"
			+ ");";
	
	private static final String CREATE_JOIN_TABLE_SQL = "create table " + CLIENT_ACCOUNT_JOIN_TABLE_NAME + "(CLIENT_FK char(36), ACCOUNT_FK char(36));";
	
	private static final String METHOD_GET_N_CLIENTS = "getNClientsStartingAtIndex";

	private static ProjectHibernate_UserManager manager;

	ProjectHibernate_UserManager() {
		super();
	}

	/**
	 * Returns default instance.
	 * 
	 * @return
	 */
	public static ProjectHibernate_UserManager getDefault() {
		
		if (manager == null) {
			manager = new ProjectHibernate_UserManager();
		}
		return manager;
	}

	public String getClassName() {
		return CLIENT_CLASS_NAME;
	}

	@Override
	public boolean setupTable() {
		Testjdbc.executeSQLQuery(DROP_TABLE_SQL);
		Testjdbc.executeSQLQuery(DROP_JOIN_TABLE_SQL);
		Testjdbc.executeSQLQuery(CREATE_JOIN_TABLE_SQL);
		return Testjdbc.executeSQLQuery(CREATE_TABLE_SQL);
	}

	public synchronized boolean add(Object object) {
		Transaction transaction = null;
		Session session = null;
		User client = (User) object;
		
		try {
			session = Testjdbc.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_CLIENT_WITH_USERID);
			query.setParameter("userId", client.getUserID());
			@SuppressWarnings("unchecked")
			List<User> clients = query.list();

			if (!clients.isEmpty()) {
				return false;
			}
				
			session.save(client);
			transaction.commit();
			return true;

		} catch (HibernateException exception) {
			ProjectHibernate_LoggerManager.current().error(this, ProjectHibernate_Messages.METHOD_ADD_CLIENT,
					"error.addClientToDatabase", exception);

			rollback(transaction);
			return false;
		} finally {
			closeSession();
		}
	}

	public synchronized boolean update(User client) {
		boolean result = super.update(client);	
		return result;
	}

	public synchronized boolean delete(User client){
		
		Session session = null;
		Transaction transaction = null;
		boolean errorResult = false;
		
		try {
			session = Testjdbc.getCurrentSession();
			transaction = session.beginTransaction();
			session.delete(client);
			transaction.commit();
			return true;
		}
		catch (HibernateException exception) {
			rollback(transaction);
			ProjectHibernate_LoggerManager.current().error(this, ProjectHibernate_Messages.METHOD_DELETE_CLIENT, ProjectHibernate_Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		}	
		catch (RuntimeException exception) {
			rollback(transaction);
			ProjectHibernate_LoggerManager.current().error(this, ProjectHibernate_Messages.METHOD_DELETE_CLIENT, ProjectHibernate_Messages.GENERIC_FAILED, exception);
			return errorResult;
		}
		finally {
			closeSession();
		}
	}
	 
	@SuppressWarnings("unchecked")
	public synchronized User getClientByEmailAddress(String emailAddress) {
		
		Session session = null;
		Transaction transaction = null;
		try {
			session = Testjdbc.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_CLIENT_WITH_EMAIL_ADDRESS);
			query.setParameter("email", emailAddress);
			List<User> clients = query.list();
			transaction.commit();

			if (clients.isEmpty()) {
				return null;
			} else {
				User client = clients.get(0);
				return client;
			}
		} catch (HibernateException exception) {
			ProjectHibernate_LoggerManager.current().error(this,
					ProjectHibernate_Messages.METHOD_GET_CLIENT_BY_EMAIL_ADDRESS,
					"error.getClientByEmailAddressInDatabase", exception);
			return null;
		} finally {
			closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized User getClientByUserId(
			String userID) {
		
		Session session = null;
		Transaction transaction = null;
		try {
			session = Testjdbc.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(SELECT_CLIENT_WITH_USERID);
			query.setParameter("userId", userID);
			List<User> clients = query.list();
			transaction.commit();

			if (clients.isEmpty()) {
				return null;
			} else {
				User client = clients.get(0);
				return client;
			}
		} catch (HibernateException exception) {
			ProjectHibernate_LoggerManager.current().error(this,
					ProjectHibernate_Messages.METHOD_GET_CLIENT_BY_PHONENUMBER,
					"error.getClientByPhonenumber", exception);
			return null;
		} finally {
			closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized User getClientByEmailAddressPasssword(
			String emailAddress, String password) {
		
		Session session = null;
		Transaction transaction = null;

		try {
			session = Testjdbc.getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session
					.createQuery(SELECT_CLIENT_WITH_EMAIL_PASSWORD);
			query.setParameter("email", emailAddress);
			query.setParameter("password", password);
			List<User> clients = query.list();
			transaction.commit();

			if (clients.isEmpty()) {
				return null;
			} else {
				User client = clients.get(0);
				return client;
			}
		} catch (HibernateException exception) {
			ProjectHibernate_LoggerManager.current().error(this,
					ProjectHibernate_Messages.METHOD_GET_CLIENT_BY_EMAIL_ADDRESS_PASSWORD,
					"error.getClientByEmailAddressInDatabase", exception);
			return null;
		} finally {
			closeSession();
		}
	}

	public synchronized User getClientById(String id) {
		
		Session session = null;
		try {
			session = Testjdbc.getCurrentSession();
			User client = (User) session.load(User.class, id);
			return client;
		} catch (HibernateException exception) {
			ProjectHibernate_LoggerManager.current().error(this,
					ProjectHibernate_Messages.METHOD_GET_CLIENT_BY_ID, "error.getClientById",
					exception);
			return null;
		} finally {
			closeSession();
		}
	}
	

	public User getClient(String emailAddress, String password) {
		
		User client = getClientByEmailAddress(emailAddress);
		if ((client != null) && (client.getPassword().equals(password))) {
			return client;
		} else {
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public synchronized  List<User> getNClientsStartingAtIndex(int index, int n) {
		List<User> errorResult = null;
		Session session = null;
		try {
			session = Testjdbc.getCurrentSession();
			Query query = session.createQuery(SELECT_ALL_CLIENTS);
			query.setFirstResult(index);
			query.setMaxResults(n);
			List<User> clients = query.list();

			return clients;
		} catch (ObjectNotFoundException exception) {
			ProjectHibernate_LoggerManager.current().error(this, METHOD_GET_N_CLIENTS,
					ProjectHibernate_Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return errorResult;
		}  catch (JDBCConnectionException exception) {
			Testjdbc.clearSessionFactory();
			ProjectHibernate_LoggerManager.current().error(this, METHOD_GET_N_CLIENTS,
					ProjectHibernate_Messages.HIBERNATE_CONNECTION_FAILED, exception);
			return errorResult;
		} catch (HibernateException exception) {
			ProjectHibernate_LoggerManager.current().error(this, METHOD_GET_N_CLIENTS,
					ProjectHibernate_Messages.HIBERNATE_FAILED, exception);
			return errorResult;
		} catch (RuntimeException exception) {
			ProjectHibernate_LoggerManager.current().error(this, METHOD_GET_N_CLIENTS,
					ProjectHibernate_Messages.GENERIC_FAILED, exception);
			return errorResult;
		} finally {
			closeSession();
		}
	}
	
	public String getTableName() {
		return CLIENT_TABLE_NAME;
	}
	
	public synchronized int getNumberOfClients() {
		
		Session session = null;
		Long aLong;

		try {
			session = Testjdbc.getCurrentSession();
			Query query = session
					.createQuery(SELECT_NUMBER_CLIENTS);
			aLong = (Long) query.uniqueResult();
			return aLong.intValue();
		} catch (ObjectNotFoundException exception) {
			ProjectHibernate_LoggerManager.current().error(this,
					ProjectHibernate_Messages.METHOD_GET_NUMBER_OF_CLIENTS,
					ProjectHibernate_Messages.OBJECT_NOT_FOUND_FAILED, exception);
			return 0;
		} catch (HibernateException exception) {
			ProjectHibernate_LoggerManager.current().error(this,
					ProjectHibernate_Messages.METHOD_GET_NUMBER_OF_CLIENTS,
					ProjectHibernate_Messages.HIBERNATE_FAILED, exception);
			return 0;
		} catch (RuntimeException exception) {
			ProjectHibernate_LoggerManager.current().error(this,
					ProjectHibernate_Messages.METHOD_GET_NUMBER_OF_CLIENTS,
					ProjectHibernate_Messages.GENERIC_FAILED, exception);
			return 0;
		} finally {
			closeSession();
		}
	}
}
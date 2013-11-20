package teabooker.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	//In local ,we support singleton session factory
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	/*
	 * //Uncommented this in SAE 
	 * private static SessionFactory sessionFactory = buildSessionFactory();
	 */

	private static SessionFactory buildSessionFactory() {

		try {
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration configuration = new Configuration()
					.configure("/teabooker/dao/hibernate.cfg.xml");

			return configuration.buildSessionFactory();

		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}

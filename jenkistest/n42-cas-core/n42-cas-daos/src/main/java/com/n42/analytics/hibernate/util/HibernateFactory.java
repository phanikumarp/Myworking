package com.n42.analytics.hibernate.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateFactory {

	private static Log log = LogFactory.getLog(HibernateFactory.class);
	private static SessionFactory sessionFactory = null; 

	//private constructor
	public HibernateFactory() {
	}

	public static SessionFactory buildSessionFactory() throws HibernateException {
		return configureSessionFactory();
	}


	public static SessionFactory getSessionFactory() {
		return configureSessionFactory();
	}


	public static void close(Session session) {
		if (session != null && session.isOpen()) {
			try {
				session.close();
			} catch (HibernateException ignored) {
				log.error("Couldn't close Session", ignored);
			}
		}
	}

	public static void rollback(Transaction tx) {
		try {
			if (tx != null) {
				tx.rollback();
			}
		} catch (HibernateException ignored) {
			log.error("Couldn't rollback Transaction", ignored);
		}
	}
	/**
	 *
	 * @return
	 * @throws HibernateException
	 */
	private static SessionFactory configureSessionFactory() {
		try {  
			if (sessionFactory == null)  {
				sessionFactory = new Configuration().configure().buildSessionFactory();  
			}
		} catch (HibernateException e) {  
			log.error("HibernateException :", e);
		}  
		return sessionFactory;  
	}
}

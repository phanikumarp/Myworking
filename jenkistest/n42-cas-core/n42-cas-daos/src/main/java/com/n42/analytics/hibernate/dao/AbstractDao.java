package com.n42.analytics.hibernate.dao;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import n42.model.domain.DomainBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.n42.analytics.hibernate.exception.DataAccessLayerException;
import com.n42.analytics.hibernate.util.HibernateFactory;

public abstract class AbstractDao {
	protected Session session;
	protected Transaction transaction;


	protected DomainBase save(DomainBase obj) {
		try {
			startOperation();
			session.save(obj);
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return obj;
	}


	protected void deleteAll(Class<?> clazz) {
		List<?> objects = null;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(clazz)
//					.setCacheable(true)
					.addOrder( Order.desc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true));
			objects = criteria.list();
			for(Object domainBase : objects) {
				session.delete(domainBase);
				//((DomainBase)domainBase).setActive(false);
			}
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
	}

	public void delete(Class<?> clazz, Integer id) {
		try {
			startOperation();
			DomainBase domainBase = (DomainBase) session.get(clazz, id);
			domainBase.setActive(false);
			session.update(domainBase);
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}	
	}
	
	public void hardDelete(Class<?> clazz, Integer id) {
		try {
			startOperation();
			DomainBase domainBase = (DomainBase) session.get(clazz, id);
			session.delete(domainBase);
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}	
	}

	protected DomainBase findById(Class<?> clazz, Integer id) {
		DomainBase obj = null;
		try {
			startOperation();
			if(id != null) {
				obj = (DomainBase) session.get(clazz, id);
			}
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return obj;
	}


	protected List<?> findAll(Class<?> clazz) {
		List<?> objects = null;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(clazz)
//					.setCacheable(true)
					.addOrder( Order.desc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true));
			objects = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return objects;
	}


	protected List<?> findAll(Class<?> clazz, List<Class<?>> associations) {
		List<?> objects = null;
		try {
			startOperation();
			loadAssociations(associations);
			Criteria criteria = session.createCriteria(clazz)
//					.setCacheable(true)
					.addOrder( Order.desc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true));
			objects = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return objects;
	}


	protected List<?> findByAttribute(Class<?> clazz, String attribute, String value) {
		List<?> objects = null;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(clazz)
//					.setCacheable(true)
					.addOrder( Order.desc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true))
					.add(Restrictions.eq(attribute, value));
			objects = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return objects;
	}


	protected List<?> findByAttributes(Class<?> clazz, String attribute1, String value1, String attribute2, String value2) {
		List<?> objects = null;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(clazz)
//					.setCacheable(true)
					.addOrder( Order.desc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true))
					.add(Restrictions.eq(attribute1, value1))
					.add(Restrictions.eq(attribute2, value2));
			objects = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return objects;
	}
	
	protected List<?> findByAttributes(Class<?> clazz, String attribute1, String value1, String attribute2, String value2, String attribute3, String value3) {
		List<?> objects = null;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(clazz)
//					.setCacheable(true)
					.addOrder( Order.desc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true))
					.add(Restrictions.eq(attribute1, value1))
					.add(Restrictions.eq(attribute2, value2))
					.add(Restrictions.eq(attribute3, value3));
			objects = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return objects;
	}


	protected List<?> findByAttributes(Class<?> clazz, String attribute1, String value1, String attribute2, Integer value2) {
		List<?> objects = null;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(clazz)
//					.setCacheable(true)
					.addOrder( Order.desc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true))
					.add(Restrictions.eq(attribute1, value1))
					.add(Restrictions.eq(attribute2, value2));
			objects = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return objects;
	}
	
	
	protected List<?> findByAttribute(Class<?> clazz, String attribute, Integer value) {
		List<?> objects = null;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(clazz)
//					.setCacheable(true)
					.addOrder( Order.desc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true))
					.add(Restrictions.eq(attribute, value));
			objects = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return objects;
	}


	protected List<?> findByAttribute(Class<?> clazz, String attribute, Boolean value) {
		List<?> objects = null;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(clazz)
//					.setCacheable(true)
					.addOrder( Order.desc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true))
					.add(Restrictions.eq(attribute, value));
			objects = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return objects;
	}


	protected List<?> selectAttributeAllValues(Class<?> clazz, String attribute) {
		List<?> objects = null;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(clazz)
//					.setCacheable(true)
					.addOrder( Order.desc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true))
					.setProjection(Projections.projectionList()
							.add(Projections.property(attribute)));
			objects = criteria.list();
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return objects;
	}

	public DomainBase setMetaAttributes(DomainBase domainBase) {
		if(domainBase.getN42Id() == null || domainBase.getN42Id() <= 0){
			domainBase.setN42CreatedDate(new Date());
//			domainBase.setN42Version(1);
		}
//		else {
//			Integer n42Version = (domainBase.getN42Version() != null && domainBase.getN42Version() > 0) ? domainBase.getN42Version() : 1;
//			domainBase.setN42Version(++n42Version);
//		}
		domainBase.setN42LastUpdatedDate(new Date());
		domainBase.setN42Hash(""+domainBase.hashCode());
		domainBase.setActive(true);
		return domainBase;
	}

	protected void handleException(HibernateException e) throws DataAccessLayerException {
		HibernateFactory.rollback(transaction);
		throw new DataAccessLayerException(e);
	}

	protected void startOperation() throws HibernateException {
		session = HibernateFactory.getSessionFactory().openSession();
		transaction = session.beginTransaction();
	}

	protected void loadAssociations(List<Class<?>> associations) {
		if(CollectionUtils.isNotEmpty(associations)) {
			for(Class<?> clazz : associations){
				Criteria criteria = session.createCriteria(clazz)
//						.setCacheable(true)
						.addOrder( Order.desc("n42LastUpdatedDate") )
						.add(Restrictions.eq("active", true));
				criteria.list();
			}		
		}
	}

	protected void loadMetaAttributes(DomainBase domainBase) {
		if(domainBase == null) {
			return;
		}
		Hibernate.initialize(domainBase.getN42Id());
		Hibernate.initialize(domainBase.getN42CreatedDate());
		Hibernate.initialize(domainBase.getN42LastUpdatedDate());
//		Hibernate.initialize(domainBase.getN42Version());
		Hibernate.initialize(domainBase.getActive());
	}


	//	protected void loadAssociations(DomainBase parent, List<Class<?>> associations) {
	//		if(parent == null || CollectionUtils.isEmpty(associations)) {
	//			return;
	//		}
	//		Method[] methods = parent.getClass().getMethods();
	//		List<Method> metList = new ArrayList<>();
	//		for(Method method : methods) {
	//			if(isGetter(method)) {
	//				metList.add(method);
	//			}
	//		}
	//		for(Class<?> clazz : associations) {
	//			for(Method method : methods) {
	//				if(isGetter(method)) {
	//					if(method.getName().contains(clazz.getSimpleName())) {
	//						try {
	//							Object args[] = new Object[]{};
	//							Hibernate.initialize(method.invoke(parent, args));
	//						} catch (HibernateException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	//							e.printStackTrace();
	//						}
	//					}
	//				}
	//			}
	//		}
	//	}
	public static boolean isGetter(Method method){
		if(!method.getName().startsWith("get")) {
			return false;
		}
		if(method.getParameterTypes().length != 0) {
			return false;  
		}
		if(void.class.equals(method.getReturnType())) {
			return false;
		}
		return true;
	}

	protected String encloseSingleQuote(String input) {
		if(StringUtils.isBlank(input)) {
			return input;
		}
		return "'" + input + "'";
	}


}
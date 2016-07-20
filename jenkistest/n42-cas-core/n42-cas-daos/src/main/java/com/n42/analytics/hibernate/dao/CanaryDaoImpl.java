package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.Canary;

import org.hibernate.HibernateException;

import com.n42.analytics.hibernate.util.HibernateFactory;

public class CanaryDaoImpl extends AbstractDao  implements CanaryDao {

	@Override
	public Canary saveCanary(Canary canary) {
		try {
			startOperation();
			canary = (Canary) setMetaAttributes(canary);
			session.save(canary);

			//		AlertUpdatedTime alertUpdatedTime = saveAlertUpdatedTime(alert);
			//		session.saveOrUpdate(alertUpdatedTime);

			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return canary;
	}

	@Override
	public List<Canary> getAllCanary(){
		@SuppressWarnings("unchecked")
		List<Canary> canaryObj = (List<Canary>) super.findAll(Canary.class);
		return canaryObj;

	}

	@Override
	public Canary getCanaryById(Integer id){
		return (Canary) super.findById(Canary.class, id);
	}

	@Override
	public void deleteCanary(Integer id) {
		super.delete(Canary.class, id);
	}

	//	@Override
	//	public void updateCanaryReason(Integer id, String reason){
	//		Canary canary = (Canary) super.findById(Canary.class , id);
	//		canary.setReason(reason);
	//		super.save(canary);
	//	}

	@Override
	public void updateCanaryReason(Integer id, String reason) {
		Canary fromDb = null;
		try {
			startOperation();
			fromDb = (Canary) session.get(Canary.class,
					id);
			//			fromDb = (Canary) setMetaAttributes(canary);
			fromDb.setReason(reason);

			if (id != null) {
				session.merge(fromDb);
			} else {
				session.update(fromDb);
			}
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}

	}

	@Override
	public void updateCanary(Canary canary) {
		Canary fromDb = null;
		try {
			startOperation();
			fromDb = canary;
			//			fromDb = (Canary) setMetaAttributes(canary);

			if (canary.getN42Id() != null) {
				session.merge(fromDb);
			} else {
				session.update(fromDb);
			}
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}

	}
	
	@Override
	public List<Canary> getCanaryByApplication(String applicationName){
		List<Canary> canaries = (List<Canary>) super.findByAttribute(Canary.class, "application", applicationName);
		return canaries;
	}

	@Override
	public void disableCanary(Canary canary) {
		try {
			startOperation();
			session.update(canary);
			transaction.commit();
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
	}
}

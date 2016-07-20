package com.n42.analytics.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.n42.analytics.hibernate.util.HibernateFactory;

import n42.domain.model.Canary;
import n42.domain.model.CanaryAnalysis;


public class CanaryAnalysisDaoImpl extends AbstractDao implements CanaryAnalysisDao {

	@Override
	public List<CanaryAnalysis> getAllCanaryAnalysis(){
		@SuppressWarnings("unchecked")
		List<CanaryAnalysis> serviceVersionsCharacterstic = (List<CanaryAnalysis>) super.findAll(CanaryAnalysis.class);
		return serviceVersionsCharacterstic;
	}

	@Override
	public CanaryAnalysis saveCanaryAnalysis(CanaryAnalysis canaryAnalysis) {
		canaryAnalysis = (CanaryAnalysis) setMetaAttributes(canaryAnalysis);
		return (CanaryAnalysis) super.save(canaryAnalysis);
	}

	@Override
	public void deleteCanaryAlaysis(Integer id) {
		super.hardDelete(CanaryAnalysis.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateCanaryResultById(Integer id, float canaryResultScore) {
		List<CanaryAnalysis> listFromDb = null;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(CanaryAnalysis.class)
					.addOrder( Order.desc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true));
			listFromDb  =  criteria.list();

			for(CanaryAnalysis fromDb : listFromDb){

				if( fromDb.getCanaryId()!=null && fromDb.getCanaryId().intValue() == id ){
					fromDb.setFinalScore(canaryResultScore);
					if (id != null) {
						session.merge(fromDb);
					} else {
						session.update(fromDb);
					}
				}
			}
			transaction.commit();
			return 1;
		} catch (HibernateException e) {
			handleException(e);
			return 0;
		} finally {
			HibernateFactory.close(session);
		}

	}
	
	@Override
	public float getCanaryResultById( Integer id ) {
		List<CanaryAnalysis> listFromDb = null;
		float result = 0 ;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(CanaryAnalysis.class)
					.addOrder( Order.asc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true));
			listFromDb  =  criteria.list();

			for(CanaryAnalysis fromDb : listFromDb){

				if( fromDb.getCanaryId()!=null && fromDb.getCanaryId().intValue() == id.intValue() ){
					result = fromDb.getFinalScore();
				}
			}
			transaction.commit();
			return result;
		} catch (HibernateException e) {
			handleException(e);
			return 0;
		} finally {
			HibernateFactory.close(session);
		}

	}
	
	@Override
	public CanaryAnalysis getCanaryAnalysisById( Integer id ) {
		List<CanaryAnalysis> listFromDb = null;
		CanaryAnalysis result = null ;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(CanaryAnalysis.class)
					.addOrder( Order.asc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true));
			listFromDb  =  criteria.list();

			for(CanaryAnalysis fromDb : listFromDb){

				if( fromDb.getCanaryId()!=null && fromDb.getCanaryId().intValue() == id.intValue() ){
					result = fromDb;
				}
			}
			transaction.commit();
			return result;
		} catch (HibernateException e) {
			handleException(e);
			return null;
		} finally {
			HibernateFactory.close(session);
		}

	}
	
}

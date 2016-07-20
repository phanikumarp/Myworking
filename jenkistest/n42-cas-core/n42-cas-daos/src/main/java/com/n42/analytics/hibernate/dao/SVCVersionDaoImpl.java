package com.n42.analytics.hibernate.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.n42.analytics.hibernate.util.HibernateFactory;

import n42.domain.model.SVCResult;
import n42.domain.model.SVCVersion;

public class SVCVersionDaoImpl extends AbstractDao implements SVCVersionDao{

	@Override
	@SuppressWarnings("unchecked")
	public List<SVCVersion> getAllSVCVersion() {
		List<SVCVersion> svcVersions = (List<SVCVersion>) super.findAll(SVCVersion.class);
		return svcVersions;
	}
	
	@Override
	public SVCVersion getSVCVersionDetails(String version){
		SVCVersion svcVersion = null;
		try {
			startOperation();
			Criteria criteria = session.createCriteria(SVCVersion.class)
					.addOrder( Order.desc("n42LastUpdatedDate") )
					.add(Restrictions.eq("active", true))
					.add(Restrictions.eq("versionName", version));
					
			svcVersion = (SVCVersion) (CollectionUtils.isNotEmpty(criteria.list()) ? criteria.list().get(0) : null);
		} catch (HibernateException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return svcVersion;
		
	}

	
}

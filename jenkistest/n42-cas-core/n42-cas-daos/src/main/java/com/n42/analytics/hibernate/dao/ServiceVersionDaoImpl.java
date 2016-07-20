package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.ServiceVersion;

public class ServiceVersionDaoImpl extends AbstractDao implements ServiceVersionDao {

	@Override
	public List<ServiceVersion> getAllServiceVersion() {
		List<ServiceVersion> serviceVersions = (List<ServiceVersion>) super.findAll(ServiceVersion.class);
		return serviceVersions;
	}

}

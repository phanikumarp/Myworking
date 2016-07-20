package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.ServiceVersionCharacterstic;

public class ServiceVersionCharactersticDaoImpl  extends AbstractDao implements ServiceVersionCharactersticDao {

	@Override
	public ServiceVersionCharacterstic saveServiceVersionModel(ServiceVersionCharacterstic sv) {
		sv = (ServiceVersionCharacterstic) setMetaAttributes(sv);
		return (ServiceVersionCharacterstic)super.save(sv);
				
	}
	
	@Override
	public List<ServiceVersionCharacterstic> getAllServiceVersionCharacterstic() {
		List<ServiceVersionCharacterstic> serviceVersionsCharacterstic = (List<ServiceVersionCharacterstic>) super.findAll(ServiceVersionCharacterstic.class);
		return serviceVersionsCharacterstic;
	}

}

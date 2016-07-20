package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.ServiceVersionCharacterstic;

public interface ServiceVersionCharactersticDao {
	
	ServiceVersionCharacterstic saveServiceVersionModel(ServiceVersionCharacterstic sv);

	List<ServiceVersionCharacterstic> getAllServiceVersionCharacterstic();
}

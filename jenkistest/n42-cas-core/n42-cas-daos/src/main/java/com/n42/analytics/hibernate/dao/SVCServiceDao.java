package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.SVCService;

public interface SVCServiceDao {
	
	List<String> getServiceVersionNames(String serviceName, int numberOfVersions);

	List<String> getAllServiceVersionNames(String serviceName);

	SVCService getServiceByName(String serviceName);
	
	List<SVCService> getAllSVCServices();
}

package com.n42.analytics.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import n42.domain.model.SVCService;

public class SVCServiceDaoImpl extends AbstractDao implements SVCServiceDao {

	public List<String> getServiceVersionNames(String serviceName,
			int numberOfVersions) {
		List<String> versionNames = getAllServiceVersionNames(serviceName);
		if (versionNames == null || versionNames.isEmpty()) {
			return null;
		}
		int size = versionNames.size();
		if (numberOfVersions >= size) {
			return versionNames;
		}
		ArrayList<String> subVersionNames = new ArrayList<String>();
		for (int i = size - numberOfVersions; i < size; i++) {
			subVersionNames.add(versionNames.get(i));
		}
		// versionNames = versionNames.subList(size - numberOfVersions, size);
		return subVersionNames;
	}

	public List<String> getAllServiceVersionNames(String serviceName) {
		SVCService service = getServiceByName(serviceName);
		if (service == null) {
			return null;
		}
		return service.getServiceVersions();
	}

	public SVCService getServiceByName(String serviceName) {
		List<SVCService> serviceList = (List<SVCService>) super
				.findByAttribute(SVCService.class, "serviceName", serviceName);
		if (serviceList == null || serviceList.isEmpty()) {
			return null;
		}
		return serviceList.get(0);
	}
	
	
	public List<SVCService> getAllSVCServices() {
		List<SVCService> svcServices = (List<SVCService>) super.findAll(SVCService.class);
		return svcServices;
	}
}

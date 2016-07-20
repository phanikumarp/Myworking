package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.Service;

public interface ServiceDao {

	List<Service> getAllServices();
	
	List<Service> getAllServices(List<Class<?>> assocations);
	
	Service getServiceById(Integer id);
	
	Service saveService(Service service);
	
	void deleteService(Integer id);
	
	void updateService(Service service);

	List<String> getAllGroups();

	List<Service> getServicesByGroup(String group);

	void deleteAll();

	Service getServiceById(Integer id, List<Class<?>> listAssocations);
	
	Service getServiceByName(String name);
	
	Service getServiceByName(String name, List<Class<?>> listAssocations);

	List<Service> getAllServicesByApplication(String serviceName, List<Class<?>> associations);

	List<String> getAllServiceNames(String application);

	List<Service> getAllServices(String application, String tenant, List<Class<?>> associations);

}

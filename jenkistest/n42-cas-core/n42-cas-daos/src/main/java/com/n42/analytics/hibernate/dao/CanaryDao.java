package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.Canary;

public interface CanaryDao {

	public Canary saveCanary(Canary canary);

	public List<Canary> getAllCanary();

	public void deleteCanary(Integer id);

	public void updateCanaryReason(Integer id, String reason);
	
	public Canary getCanaryById(Integer id);
	
	public List<Canary> getCanaryByApplication(String applicationName);
	
	public void disableCanary(Canary canary);

	public void updateCanary(Canary canary);

}


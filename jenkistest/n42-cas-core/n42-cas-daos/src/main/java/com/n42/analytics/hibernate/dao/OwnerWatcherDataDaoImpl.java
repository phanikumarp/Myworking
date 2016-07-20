package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.Cluster;
import n42.domain.model.OwnerWatcherData;

public class OwnerWatcherDataDaoImpl extends AbstractDao implements OwnerWatcherDataDao {
	@Override
	public OwnerWatcherData saveOwnerWatcherData(OwnerWatcherData ownerWatcherData) {
		ownerWatcherData = (OwnerWatcherData) setMetaAttributes(ownerWatcherData);
		return (OwnerWatcherData) super.save(ownerWatcherData);
	}
	
	@Override
	public List<OwnerWatcherData> getAllOwnerWatcherData(){
		@SuppressWarnings("unchecked")
		List<OwnerWatcherData> ownerWatcherData = (List<OwnerWatcherData>) super.findAll(OwnerWatcherData.class);
		return ownerWatcherData;
		
	}
	
	@Override
	public void deleteOwnerWatcherData(Integer id) {
		super.hardDelete(OwnerWatcherData.class, id);
	}
}

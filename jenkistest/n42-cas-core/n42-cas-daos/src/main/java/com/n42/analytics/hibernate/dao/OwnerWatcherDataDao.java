package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.Cluster;
import n42.domain.model.OwnerWatcherData;

public interface OwnerWatcherDataDao {

	public OwnerWatcherData saveOwnerWatcherData(OwnerWatcherData ownerWatcherData);

	public List<OwnerWatcherData> getAllOwnerWatcherData();

	public void deleteOwnerWatcherData(Integer id);

}

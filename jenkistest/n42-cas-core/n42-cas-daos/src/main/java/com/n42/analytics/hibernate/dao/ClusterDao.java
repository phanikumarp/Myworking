package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.Cluster;


public interface ClusterDao {

	public Cluster saveCluster(Cluster cluster);

	public List<Cluster> getAllClusters();

	public void deleteCluster(Integer id);
	
	public Cluster getClusterById(Integer id);
	
	
}

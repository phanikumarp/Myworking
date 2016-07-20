package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.Cluster;

public class ClusterDaoImpl extends AbstractDao implements ClusterDao {
	@Override
	public Cluster saveCluster(Cluster cluster) {
		cluster = (Cluster) setMetaAttributes(cluster);
		return (Cluster) super.save(cluster);
	}
	
	@Override
	public List<Cluster> getAllClusters(){
		@SuppressWarnings("unchecked")
		List<Cluster> clusterObj = (List<Cluster>) super.findAll(Cluster.class);
		return clusterObj;
		
	}
	
	@Override
	public void deleteCluster(Integer id) {
		super.hardDelete(Cluster.class, id);
	}

	@Override
	public Cluster getClusterById(Integer id) {
		return (Cluster) super.findById(Cluster.class, id);
	}
}

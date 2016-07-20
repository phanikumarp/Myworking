package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.VersionStats;

public class VersionStatsDaoImpl extends AbstractDao implements VersionStatsDao {
	@Override
	public VersionStats saveVersionStats(VersionStats versionStats) {
		versionStats = (VersionStats) setMetaAttributes(versionStats);
		return (VersionStats) super.save(versionStats);
	}
	
	@Override
	public List<VersionStats> getAllVersionStats(){
		@SuppressWarnings("unchecked")
		List<VersionStats> versionStatsObj = (List<VersionStats>) super.findAll(VersionStats.class);
		return versionStatsObj;
	}

	@Override
	public VersionStats getVersionStatsById(Integer id) {
		return (VersionStats) super.findById(VersionStats.class, id);
	}

	@Override
	public void deleteVersionStats(Integer id) {
		super.hardDelete(VersionStats.class, id);
	}
}

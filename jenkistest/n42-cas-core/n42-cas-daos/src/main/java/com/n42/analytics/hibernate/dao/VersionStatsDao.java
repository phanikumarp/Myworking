package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.MetricScore;
import n42.domain.model.VersionStats;

public interface VersionStatsDao {
	public VersionStats saveVersionStats(VersionStats versionStats);
	public VersionStats getVersionStatsById(Integer id);
	public void deleteVersionStats(Integer id);
	public List<VersionStats> getAllVersionStats();
}

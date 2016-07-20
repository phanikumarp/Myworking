package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.MetricScore;


public interface MetricScoreDao {
	
	public MetricScore saveMetricScore(MetricScore metricScore);
	public MetricScore getMetricScoreById(Integer id);
	public void deleteMetricScore(Integer id);
	public List<MetricScore> getAllMetricScore();

}

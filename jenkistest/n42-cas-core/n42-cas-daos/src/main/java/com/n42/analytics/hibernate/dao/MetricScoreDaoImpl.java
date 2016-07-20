package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.MetricScore;

public class MetricScoreDaoImpl extends AbstractDao implements MetricScoreDao {

	@Override
	public MetricScore saveMetricScore(MetricScore metricScore) {
		metricScore = (MetricScore) setMetaAttributes(metricScore);
		return (MetricScore) super.save(metricScore);
	}
	
	@Override
	public List<MetricScore> getAllMetricScore(){
		@SuppressWarnings("unchecked")
		List<MetricScore> MetricScoreObj = (List<MetricScore>) super.findAll(MetricScore.class);
		return MetricScoreObj;
	}

	@Override
	public MetricScore getMetricScoreById(Integer id) {
		return (MetricScore) super.findById(MetricScore.class, id);
	}

	@Override
	public void deleteMetricScore(Integer id) {
		super.hardDelete(MetricScore.class, id);
	}

}

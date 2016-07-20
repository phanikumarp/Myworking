package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.CasServiceMetricDetails;


public class CasServiceMetricDetailsDaoImpl extends AbstractDao implements CasServiceMetricDetailsDao{

	@Override
	public List<CasServiceMetricDetails> getAllCasServiceMetricDetails(){
		@SuppressWarnings("unchecked")
		List<CasServiceMetricDetails> MetricScoreObj = (List<CasServiceMetricDetails>) super.findAll(CasServiceMetricDetails.class);
		return MetricScoreObj;
	}

	@Override
	public CasServiceMetricDetails getCasServiceMetricDetails(String metricname) {
		List<CasServiceMetricDetails> MetricScoreObj = (List<CasServiceMetricDetails>) super.findByAttribute(CasServiceMetricDetails.class, "metricName", metricname);
		return MetricScoreObj.get(0);
	}
	
	
}

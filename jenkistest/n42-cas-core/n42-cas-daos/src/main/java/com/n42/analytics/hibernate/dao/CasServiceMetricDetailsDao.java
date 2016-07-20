package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.CasServiceMetricDetails;

public interface CasServiceMetricDetailsDao {

	List<CasServiceMetricDetails> getAllCasServiceMetricDetails();
	
	CasServiceMetricDetails getCasServiceMetricDetails(String metricname);

}

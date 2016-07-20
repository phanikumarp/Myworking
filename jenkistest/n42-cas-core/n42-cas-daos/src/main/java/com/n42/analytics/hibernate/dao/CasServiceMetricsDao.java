package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.CasServiceMetrics;

public interface CasServiceMetricsDao {

	public List<CasServiceMetrics> getAllCasServiceMetrics();

	public CasServiceMetrics getCasServiceMetricsByServiceName(
			String serviceName);

	CasServiceMetrics getCasServiceMetricsByService(String serviceName);

}

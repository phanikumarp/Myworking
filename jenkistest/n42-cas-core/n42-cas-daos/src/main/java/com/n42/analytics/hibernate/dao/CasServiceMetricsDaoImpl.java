package com.n42.analytics.hibernate.dao;

import java.util.HashSet;
import java.util.List;

import n42.domain.model.CasServiceMetricDetails;
import n42.domain.model.CasServiceMetrics;

import org.apache.commons.lang3.StringUtils;

public class CasServiceMetricsDaoImpl extends AbstractDao implements
		CasServiceMetricsDao {

	@Override
	public List<CasServiceMetrics> getAllCasServiceMetrics() {
		@SuppressWarnings("unchecked")
		List<CasServiceMetrics> casServiceMetrics = (List<CasServiceMetrics>) super
				.findAll(CasServiceMetrics.class);
		return casServiceMetrics;
	}

	@Override
	public CasServiceMetrics getCasServiceMetricsByServiceName(
			String serviceName) {
		@SuppressWarnings("unchecked")
		List<CasServiceMetrics> casServiceMetrics = (List<CasServiceMetrics>) super
				.findAll(CasServiceMetrics.class);
		CasServiceMetrics result = new CasServiceMetrics();
		for (CasServiceMetrics casServiceMetric : casServiceMetrics) {
			if (casServiceMetric != null
					&& casServiceMetric.getServiceName() != null) {
				if (StringUtils.equalsIgnoreCase(serviceName,
						casServiceMetric.getServiceName())) {
					result = casServiceMetric;
				}
			}
		}
		return result;
	}

	@Override
	public CasServiceMetrics getCasServiceMetricsByService(String serviceName) {
		@SuppressWarnings("unchecked")
		List<CasServiceMetrics> casServiceMetrics = (List<CasServiceMetrics>) super
				.findByAttribute(CasServiceMetrics.class, "serviceName",
						serviceName);
		if (casServiceMetrics == null || casServiceMetrics.isEmpty()) {
			return null;
		}
		return casServiceMetrics.get(0);
	}
	
}

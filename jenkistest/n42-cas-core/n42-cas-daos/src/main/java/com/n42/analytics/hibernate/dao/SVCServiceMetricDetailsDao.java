package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.SVCServiceMetricDetails;

public interface SVCServiceMetricDetailsDao {

	List<SVCServiceMetricDetails> getServiceMetricsbyService(String service);

}

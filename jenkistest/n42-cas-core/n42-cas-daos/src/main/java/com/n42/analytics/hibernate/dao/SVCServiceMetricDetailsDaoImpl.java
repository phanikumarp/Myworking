package com.n42.analytics.hibernate.dao;

import java.util.ArrayList;
import java.util.List;
import n42.domain.model.SVCServiceMetricDetails;
import org.apache.commons.lang3.StringUtils;

public class SVCServiceMetricDetailsDaoImpl extends AbstractDao implements SVCServiceMetricDetailsDao {

	@SuppressWarnings("unchecked")
	public List<SVCServiceMetricDetails> getServiceMetricsbyService(String service){
		ArrayList<SVCServiceMetricDetails> result = new ArrayList<SVCServiceMetricDetails>();
		List<SVCServiceMetricDetails> svcServiceMetrics = (List<SVCServiceMetricDetails>) super
				.findAll(SVCServiceMetricDetails.class);
		for (SVCServiceMetricDetails ServiceMetric : svcServiceMetrics) {
			if (ServiceMetric != null
					&& ServiceMetric.getMetricType() != null) {
				if (StringUtils.equalsIgnoreCase(service,
						ServiceMetric.getMetricType())) {
					result.add(ServiceMetric);
				}
			}
		}
		return result;
	}


}

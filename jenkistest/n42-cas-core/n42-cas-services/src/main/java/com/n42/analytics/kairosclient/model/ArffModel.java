package com.n42.analytics.kairosclient.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArffModel {
	
	public ArffModel() {
	}
	
	private List<String> metricNames = new ArrayList<>();
	private List<Map<Long, Double>> timesVsValue = new ArrayList<>();
	
	public List<String> getMetricNames() {
		return metricNames;
	}
	public void setMetricNames(List<String> metricNames) {
		this.metricNames = metricNames;
	}
	public List<Map<Long, Double>> getTimesVsValue() {
		return timesVsValue;
	}
	public void setTimesVsValue(List<Map<Long, Double>> timesVsValue) {
		this.timesVsValue = timesVsValue;
	}
	
	
}

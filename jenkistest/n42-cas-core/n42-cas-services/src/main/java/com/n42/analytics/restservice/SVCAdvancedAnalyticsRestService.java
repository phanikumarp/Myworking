package com.n42.analytics.restservice;

import java.util.HashSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.simple.JSONArray;

import com.n42.analytics.services.SVCAdvancedAnalytics;
import com.n42.analytics.util.JSONUtils;

@Path("/svcadvanced")
public class SVCAdvancedAnalyticsRestService {
	@GET
	@Produces("application/json")
	@Path("/scoretrend/{service}/{numOfVersions}")
	public JSONArray getScoreAnalysis(@PathParam("service") String serviceName,
			@PathParam("numOfVersions") int numOfVersions) {
		SVCAdvancedAnalytics svcAdvancedAnalytics = new SVCAdvancedAnalytics();
		return svcAdvancedAnalytics
				.getScoreAnalysis(serviceName, numOfVersions);
	}

	@GET
	@Produces("application/json")
	@Path("/metrictrend/{service}/{metricName}/{numOfVersions}")
	public JSONArray getMetricAnalysis(
			@PathParam("service") String serviceName,
			@PathParam("metricName") String metricName,
			@PathParam("numOfVersions") int numOfVersions) {
		SVCAdvancedAnalytics svcAdvancedAnalytics = new SVCAdvancedAnalytics();
		return svcAdvancedAnalytics.getMetricAnalysis(serviceName, metricName,
				numOfVersions);
	}

	@GET
	@Produces("application/json")
	@Path("/metrictypes/{service}")
	public HashSet<String> getServiceMetricTypes(
			@PathParam("service") String serviceName) {
		SVCAdvancedAnalytics svcAdvancedAnalytics = new SVCAdvancedAnalytics();
		return svcAdvancedAnalytics.getCasServiceMetricsTypes(serviceName);
	}

	@GET
	@Produces("application/json")
	@Path("/metricnames/{service}/{type}")
	public HashSet<String> getServiceMetricNames(
			@PathParam("service") String serviceName,
			@PathParam("type") String type) {
		SVCAdvancedAnalytics svcAdvancedAnalytics = new SVCAdvancedAnalytics();
		return svcAdvancedAnalytics.getCasServiceMetricsByServiceAndType(
				serviceName, type);
	}

	public static void main(String[] args) {
		SVCAdvancedAnalyticsRestService advancedAnalyticsRestService = new SVCAdvancedAnalyticsRestService();
		JSONArray result = advancedAnalyticsRestService.getScoreAnalysis(
				"Tomcat", 2);

		System.out.println(JSONUtils.getFormattedJson(result));
	}
}

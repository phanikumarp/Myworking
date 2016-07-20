package com.n42.analytics.services;

import java.util.HashSet;
import java.util.List;

import n42.domain.model.CasServiceMetricDetails;
import n42.domain.model.CasServiceMetrics;
import n42.domain.model.SVCResult;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.n42.analytics.hibernate.dao.CasServiceMetricsDao;
import com.n42.analytics.hibernate.dao.CasServiceMetricsDaoImpl;
import com.n42.analytics.hibernate.dao.SVCResultDaoImpl;
import com.n42.analytics.hibernate.dao.SVCServiceDao;
import com.n42.analytics.hibernate.dao.SVCServiceDaoImpl;
import com.n42.analytics.util.JSONUtils;

public class SVCAdvancedAnalytics {

	final int FIFTH_PERC_INDEX = 0;
	final int MEAN_INDEX = 1;
	final int MEDIAN_INDEX = 2;
	final int NINFIV_PERC_INDEX = 3;
	final int MAX_INDEX = 4;

	public JSONArray getScoreAnalysis(String service, int numOfVersions) {
		final int defaultCompScore = 100;
		final String VERSION_KEY = "version";
		final String SCORE_KEY = "score";
		JSONArray outputJson = new JSONArray();
		SVCServiceDao svcServiceDao = new SVCServiceDaoImpl();
		List<String> versionNames = svcServiceDao.getServiceVersionNames(
				service, numOfVersions);
		if (versionNames == null || versionNames.isEmpty()) {
			return outputJson;
		}
		String prevVersion = versionNames.get(0);
		SVCResultDaoImpl svcResultDaoImpl = new SVCResultDaoImpl();
		for (String currVersion : versionNames) {
			JSONObject scoreJson = new JSONObject();
			scoreJson.put(VERSION_KEY, currVersion);
			outputJson.add(scoreJson);
			if (currVersion.equals(prevVersion)) {
				scoreJson.put(SCORE_KEY, defaultCompScore);
			} else {
				SVCResult svcResult = svcResultDaoImpl.getSVCResult(service,
						prevVersion, currVersion);
				if (svcResult == null) {
					scoreJson.put(SCORE_KEY, defaultCompScore);
					System.out.println("Result not yet computed for versions: "
							+ prevVersion + "," + currVersion);
					continue;
				}
				String resultString = svcResult.getResult();
				if (resultString == null || resultString.isEmpty()) {
					System.out.println("Empty result for versions: "
							+ prevVersion + "," + currVersion);
					scoreJson.put(SCORE_KEY, defaultCompScore);
					continue;
				}
				JSONObject resultJson = parseJson(resultString);
				if (resultJson == null || resultJson.isEmpty()) {
					System.out.println("JSONParse failure for versions: "
							+ prevVersion + "," + currVersion);
					scoreJson.put(SCORE_KEY, defaultCompScore);
					continue;
				}
				JSONObject compOutputJson = (JSONObject) resultJson
						.get("comparison_output");
				double compScore = (double) compOutputJson
						.get("comparisionScore");
				scoreJson.put(SCORE_KEY, compScore);
			}
			prevVersion = currVersion;
		}
		return outputJson;
	}

	public JSONArray getMetricAnalysis(String service, String metricName,
			int numOfVersions) {
		JSONArray outputJson = new JSONArray();
		initialiseOutputJson(outputJson);
		SVCServiceDao svcServiceDao = new SVCServiceDaoImpl();
		List<String> versionNames = svcServiceDao.getServiceVersionNames(
				service, numOfVersions);
		if (versionNames == null || versionNames.isEmpty()) {
			return outputJson;
		}
		String prevVersion = versionNames.get(0);
		SVCResultDaoImpl svcResultDaoImpl = new SVCResultDaoImpl();
		int counter = 1;
		for (String currVersion : versionNames) {
			// JSONObject versionStatsJson = new JSONObject();
			// First version data will be filled in second iteration
			if (counter != 1) {
				SVCResult svcResult = svcResultDaoImpl.getSVCResult(service,
						prevVersion, currVersion);
				if (svcResult == null) {
					fillDefaultMetricStats(outputJson, currVersion);
					System.out.println("Result not yet computed for versions: "
							+ prevVersion + "," + currVersion);
					continue;
				}
				String resultString = svcResult.getResult();
				if (resultString == null || resultString.isEmpty()) {
					System.out.println("Empty result for versions: "
							+ prevVersion + "," + currVersion);
					fillDefaultMetricStats(outputJson, currVersion);
					continue;
				}
				JSONObject resultJson = parseJson(resultString);
				if (resultJson == null || resultJson.isEmpty()) {
					System.out.println("JSONParse failure for versions: "
							+ prevVersion + "," + currVersion);
					fillDefaultMetricStats(outputJson, currVersion);
					continue;
				}
				JSONObject compOutputJson = (JSONObject) resultJson
						.get("comparison_output");
				JSONObject results = (JSONObject) compOutputJson.get("results");
				JSONObject metricJson = (JSONObject) ((JSONObject) ((JSONObject) results
						.get(metricName)).get("metricstats"));
				if (counter == 2) {
					fillMetricStats(metricJson, "version1", outputJson,
							prevVersion);

				}
				fillMetricStats(metricJson, "version2", outputJson, currVersion);
			}
			counter++;
			prevVersion = currVersion;
		}
		return outputJson;
	}

	private void initialiseOutputJson(JSONArray outputJson) {
		JSONObject fifthPerOutputJson = new JSONObject();
		fifthPerOutputJson.put("key", "5th Percentile");
		JSONArray fifthPerValuesArray = new JSONArray();
		fifthPerOutputJson.put("values", fifthPerValuesArray);
		outputJson.add(fifthPerOutputJson);
		
		JSONObject meanOutput = new JSONObject();
		meanOutput.put("key", "Mean");
		JSONArray meanValuesArray = new JSONArray();
		meanOutput.put("values", meanValuesArray);
		outputJson.add(meanOutput);
		
		JSONObject medianOutput = new JSONObject();
		medianOutput.put("key", "Median");
		JSONArray medianValuesArray = new JSONArray();
		medianOutput.put("values", medianValuesArray);
		outputJson.add(medianOutput);
		
		JSONObject ninFiveOutput = new JSONObject();
		ninFiveOutput.put("key", "95th Percentile");
		JSONArray ninFiveValuesArray = new JSONArray();
		ninFiveOutput.put("values", ninFiveValuesArray);
		outputJson.add(ninFiveOutput);
		
		JSONObject maxOutput = new JSONObject();
		maxOutput.put("key", "Max");
		JSONArray maxOutputValuesArray = new JSONArray();
		maxOutput.put("values", maxOutputValuesArray);
		outputJson.add(maxOutput);
	}

	private void fillDefaultMetricStats(JSONArray outputJsonArray,
			String versionStr) {
		final int defaultMetricVal = 0;
		JSONArray defArray = new JSONArray();
		float versionNum = getOnlyVersionNum(versionStr); 
		defArray.add(versionNum);
		defArray.add(defaultMetricVal);
		((JSONArray) ((JSONObject) outputJsonArray.get(FIFTH_PERC_INDEX))
				.get("values")).add(defArray);
		((JSONArray) ((JSONObject) outputJsonArray.get(MEAN_INDEX))
				.get("values")).add(defArray);
		((JSONArray) ((JSONObject) outputJsonArray.get(MEDIAN_INDEX))
				.get("values")).add(defArray);
		((JSONArray) ((JSONObject) outputJsonArray.get(NINFIV_PERC_INDEX))
				.get("values")).add(defArray);
		((JSONArray) ((JSONObject) outputJsonArray.get(MAX_INDEX))
				.get("values")).add(defArray);
	}

	private void fillMetricStats(JSONObject metricJson, String version,
			JSONArray outputJsonArray, String versionStr) {
		JSONObject version1StatsInfo = (JSONObject) ((JSONObject) metricJson
				.get("statinfo")).get(version);
		
		JSONArray meanArray = new JSONArray();
		float versionNum = getOnlyVersionNum(versionStr); 
		meanArray.add(versionNum);
		meanArray.add(version1StatsInfo.get("Mean"));
		((JSONArray) ((JSONObject) outputJsonArray.get(MEAN_INDEX))
				.get("values")).add(meanArray);
		
		JSONArray medianArray = new JSONArray();
		medianArray.add(versionNum);
		medianArray.add(version1StatsInfo.get("Median"));
		((JSONArray) ((JSONObject) outputJsonArray.get(MEDIAN_INDEX))
				.get("values")).add(medianArray);
		
		JSONArray maxArray = new JSONArray();
		maxArray.add(versionNum);
		maxArray.add(version1StatsInfo.get("Max."));
		((JSONArray) ((JSONObject) outputJsonArray.get(MAX_INDEX))
				.get("values")).add(maxArray);
		
		JSONObject version1QuantInfo = (JSONObject) ((JSONObject) metricJson
				.get("quantileinfo")).get(version);
		
		JSONArray fifArray = new JSONArray();
		fifArray.add(versionNum);
		fifArray.add(version1QuantInfo.get("5%"));
		((JSONArray) ((JSONObject) outputJsonArray.get(FIFTH_PERC_INDEX))
				.get("values")).add(fifArray);
		
		JSONArray ninFiveArray = new JSONArray();
		ninFiveArray.add(versionNum);
		ninFiveArray.add(version1QuantInfo.get("95%"));
		((JSONArray) ((JSONObject) outputJsonArray.get(NINFIV_PERC_INDEX))
				.get("values")).add(ninFiveArray);
	}

	public JSONObject parseJson(String input) {
		return JSONUtils.parseJsonFromInputString(input);
	}

	public HashSet<String> getCasServiceMetricsTypes(String serviceName) {
		CasServiceMetricsDao casServiceMetricsDao = new CasServiceMetricsDaoImpl();
		CasServiceMetrics casServiceMetrics = casServiceMetricsDao
				.getCasServiceMetricsByService(serviceName);
		HashSet<String> metricTypes = new HashSet<String>();
		if (casServiceMetrics == null) {
			return metricTypes;
		}
		for (CasServiceMetricDetails casServiceMetricDetails : casServiceMetrics
				.getMetricsDetails()) {
			if (casServiceMetricDetails != null
					&& casServiceMetricDetails.getMetricType() != null) {
				metricTypes.add(casServiceMetricDetails.getMetricType());
			}
		}
		return metricTypes;
	}

	public HashSet<String> getCasServiceMetricsByServiceAndType(
			String serviceName, String type) {
		CasServiceMetricsDao casServiceMetricsDao = new CasServiceMetricsDaoImpl();
		CasServiceMetrics casServiceMetrics = casServiceMetricsDao
				.getCasServiceMetricsByService(serviceName);
		HashSet<String> metricNames = new HashSet<String>();
		if (casServiceMetrics == null) {
			return metricNames;
		}
		for (CasServiceMetricDetails casServiceMetricDetails : casServiceMetrics
				.getMetricsDetails()) {
			if (casServiceMetricDetails != null
					&& casServiceMetricDetails.getMetricType() != null
					&& StringUtils.equals(
							casServiceMetricDetails.getMetricType(), type)) {
				metricNames.add(casServiceMetricDetails.getMetricName());
			}
		}
		return metricNames;
	}
	
	private float getOnlyVersionNum(String version){
		String versionString = version.substring(1);
		return Float.parseFloat(versionString);
	}

	public static void main(String args[]) {
		SVCAdvancedAnalytics svcAdvancedAnalytics = new SVCAdvancedAnalytics();
		JSONArray outputJsonArray = svcAdvancedAnalytics.getMetricAnalysis(
				"Tomcat", "system.cpu.util", 3);
		/*
		 * JSONArray outputJsonArray = svcAdvancedAnalytics.getScoreAnalysis(
		 * "Tomcat", 3);
		 */
		// HashSet<String> outputJsonArray =
		// svcAdvancedAnalytics.getCasServiceMetricsTypes("Tomcat");
		System.out.println("Output array:" + outputJsonArray);
	}
}

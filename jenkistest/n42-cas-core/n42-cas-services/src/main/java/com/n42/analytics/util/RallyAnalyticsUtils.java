package com.n42.analytics.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RallyAnalyticsUtils {

	private static final Logger LOG = LoggerFactory.getLogger(RallyAnalyticsUtils.class);

	@SuppressWarnings("unchecked")
	public static Object getRallyAnalytics(String filepath, boolean isAvgAndMaxRequired, String attributeToDelete) {
		Map<String, Object> root = new HashMap<>();
		JSONArray results = getResultsObject(filepath);
		Map<String, Object> atomic_actions = null;

		JSONArray vmVsTime = new JSONArray();
		double avgTimeForCreation = 0;
		double avgTimeFordeletion = 0;
		List<Double> creationTime = new ArrayList<>();
		List<Double> deletionTime = new ArrayList<>();
		for(int i = 0; i < results.size(); i++){
			JSONObject result = (JSONObject) results.get(i);
			atomic_actions =  (Map<String, Object>) result.get("atomic_actions");
			for(Entry<String, Object> entry : atomic_actions.entrySet()) {
				if(entry.getKey().equalsIgnoreCase(attributeToDelete)){
					continue;
				}
				
				JSONObject objectCreateTime = new JSONObject();
				double time = Double.parseDouble(new DecimalFormat("#0.000").format(atomic_actions.get(entry.getKey())));
				objectCreateTime.put("name", entry.getKey().replaceAll("\\(", "").replaceAll("\\)", ""));
				objectCreateTime.put("time", time);
				objectCreateTime.put("units", "sec");
//				analytics.put(entry.getKey().replaceAll("\\.", "_"), objectCreateTime);
				vmVsTime.add(objectCreateTime);
				if(entry.getKey().contains("create")){
					avgTimeForCreation += time;
					creationTime.add(time);
				} else if(entry.getKey().contains("delete")) {
					avgTimeFordeletion += time;
					deletionTime.add(time);
				}
			}
			
			if(isAvgAndMaxRequired) {
 				root.put("avg_creation_time", getAvgTime(avgTimeForCreation/result.size(), creationTime));
				root.put("avg_deletion_time", getAvgTime(avgTimeFordeletion/result.size(), deletionTime));
			}
			
//			vmVsTime.add(analytics);
		}

		root.put("analytics", vmVsTime);

		LOG.debug("The rally analytics json is : {}", root);
		return root;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONObject getAvgTime(double avgTime, List<Double> time){
		JSONObject avgTimeObject = new JSONObject();
		Collections.sort(time);
		
		avgTimeObject.put("avg_time", Double.parseDouble(new DecimalFormat("#0.000").format(avgTime)));
		avgTimeObject.put("Max_time", time.get(time.size() - 1));
		avgTimeObject.put("unit", "sec");
		
		return avgTimeObject;
	}

	public static JSONArray getResultsObject(String filepath) {
		String rallyResponse = JSONUtils.createJsonFromFile(filepath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject;
		JSONArray results = null;
		try {
			JSONArray jsonArray = (JSONArray) jsonParser.parse(rallyResponse);
			jsonObject = (JSONObject) jsonArray.get(0);
			results = (JSONArray) jsonObject.get("result");

		} catch (ParseException e) {
			LOG.error("Unable to parse json from {}", filepath);
		}

		return results;
	}
	
	@SuppressWarnings("unchecked")
	public static Object getRallyAnalyticsWithName(String filePath, String attributeName) {
		Map<String, Object> root = new HashMap<>();
		JSONArray results = getResultsObject(filePath);
		
		JSONArray vmVsTime = new JSONArray();
		JSONObject analytics = new JSONObject();
		for(int i = 0; i < results.size(); i++){
			
			JSONObject result = (JSONObject) results.get(i);
			Map<String, Object> atomic_actions =  (Map<String, Object>) result.get("atomic_actions");
			for(Entry<String, Object> entry : atomic_actions.entrySet()) {
				JSONObject objectCreateTime = new JSONObject();
				if(entry.getKey().equalsIgnoreCase(attributeName)){
					double time = Double.parseDouble(new DecimalFormat("#0.00").format(atomic_actions.get(entry.getKey())));
					objectCreateTime.put("name", entry.getKey());
					objectCreateTime.put("time", time);
					objectCreateTime.put("units", "sec");
					analytics.put(entry.getKey().replaceAll("\\.", "_"), objectCreateTime);
				}
			}
			vmVsTime.add(analytics);
		}

		root.put("analytics", vmVsTime);

		LOG.debug("The Json for rally analytics for filepath {} is : {}", filePath, root);
		return root;
	}
}

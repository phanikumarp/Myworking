package com.n42.analytics.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(JSONUtils.class);

	public static String parseJson(String input) {

		String result = "";

		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(input);
			JSONArray rootJsonArray = (JSONArray) jsonObject.get("queries");
			if(rootJsonArray == null){
				return null;
			}
			JSONObject datapoint = (JSONObject) rootJsonArray.get(0);
			JSONArray datapoints = (JSONArray) datapoint.get("results");
			JSONObject obj = (JSONObject) datapoints.get(0);
			JSONArray objs = (JSONArray) obj.get("values");
			result = rickshawFormat(objs);

		} catch (ParseException e) {
			LOG.warn("Unexpected exception : {}", e);
			return null;
		}

		return result;

	}
	
	public static Map<Long, Double> parseJsonToMap(String input) {
		
		Map<Long, Double> dataPointMap = new HashMap<Long, Double>();
		
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(input);
			JSONArray rootJsonArray = (JSONArray) jsonObject.get("queries");
			if(rootJsonArray == null){
				return null;
			}
			JSONObject datapoint = (JSONObject) rootJsonArray.get(0);
			JSONArray datapoints = (JSONArray) datapoint.get("results");
			JSONObject obj = (JSONObject) datapoints.get(0);
			JSONArray objs = (JSONArray) obj.get("values");
			System.out.println(objs.get(0));
			
			for(int i = 0; i < objs.size(); i++) {
				JSONArray point = (JSONArray) objs.get(i);
				dataPointMap.put((Long) point.get(0), NumberUtils.toDouble(point.get(1).toString()));
				System.out.print(point.get(0) + ":" + point.get(1));
			}

		} catch (ParseException e) {
			LOG.warn("Unexpected exception : {}", e);
			return null;
		}
		System.out.println(dataPointMap);
		return dataPointMap;

	}

	public static String extractLastValueFromJson(String input) {
		String result = StringUtils.EMPTY;
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(input);
			JSONArray rootJsonArray = (JSONArray) jsonObject.get("queries");
			if(rootJsonArray == null){
				return result;
			}
			JSONObject datapoint = (JSONObject) rootJsonArray.get(0);
			JSONArray datapoints = (JSONArray) datapoint.get("results");
			JSONObject obj = (JSONObject) datapoints.get(0);
			JSONArray objs = (JSONArray) obj.get("values");
			if(objs != null && objs.size() > 0 ){
				int lastIndexOfValues = objs.size() - 1;
				JSONArray lastValueArray = (JSONArray)objs.get(lastIndexOfValues);
				result = lastValueArray.get(1).toString();
			}
		} catch(ParseException e) {
			LOG.warn("Unexpected exception : {}", e);
		}
		return result;
	}


	private static String rickshawFormat(JSONArray objs) {

		StringBuilder rickshawFormat = new StringBuilder();

		rickshawFormat.append("[");
		if(objs.size() == 0){
			// TODO If there is no data, we send back some "dummy data". Handle empty data sets on the client too.
			return "[[0.0,1402308915],[0.0,1402309450],[0.0,1402309990],[0.0,1402310525],[0.0,1402311065]]";
		}

		// There is some data		
		for(int i = 0; i < objs.size(); i++) {
			String resultPoint = "[";
			JSONArray point = (JSONArray) objs.get(i);
			resultPoint += point.get(1) + "," + point.get(0).toString().substring(0, 10);
			resultPoint += "]";
			rickshawFormat.append(resultPoint);	
			rickshawFormat.append(",");			
		}

		rickshawFormat.deleteCharAt(rickshawFormat.length() - 1);
		rickshawFormat.append("]");

		return rickshawFormat.toString();
	}

	public static String getFormattedJson(Object item) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	public static String createJsonFromFile(String fileName) {
		String result = null;
		JSONUtils jsonUtils = new JSONUtils();
		try(InputStream is = jsonUtils.getClass().getResourceAsStream(fileName);) {
			result = IOUtils.toString(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static <T> String createJsonFromFile(Class<T> className, String filePath) {
		String result = null;
		try(InputStream is = className.getResourceAsStream(filePath);) {
			result = IOUtils.toString(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static JSONObject parseJsonFromInputString(String inputJsonStr) {
		JSONObject result = null;
		JSONParser jsonParser = new JSONParser();
		try {
			result = (JSONObject) jsonParser.parse(inputJsonStr);
			if(result == null){
				return result;
			}

		} catch (ParseException e) {
			LOG.warn("Unexpected exception : {}", e);
			return result;
		}
		return result;
	}
	public static JSONArray parseJsonArrayFromInputString(String inputJsonStr) {
		JSONArray result = null;
		JSONParser jsonParser = new JSONParser();
		try {
			result = (JSONArray) jsonParser.parse(inputJsonStr);
			if(result == null){
				return result;
			}

		} catch (ParseException e) {
			LOG.warn("Unexpected exception : {}", e);
			return result;
		}
		return result;
	}
}

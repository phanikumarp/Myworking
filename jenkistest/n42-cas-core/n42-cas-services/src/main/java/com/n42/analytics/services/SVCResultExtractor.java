package com.n42.analytics.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import n42.domain.model.CasServiceMetricDetails;
import n42.domain.model.SVCResult;
import n42.domain.model.SVCServiceMetricDetails;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.n42.analytics.hibernate.dao.CasServiceMetricDetailsDao;
import com.n42.analytics.hibernate.dao.CasServiceMetricDetailsDaoImpl;
import com.n42.analytics.hibernate.dao.SVCResultDao;
import com.n42.analytics.hibernate.dao.SVCResultDaoImpl;
import com.n42.analytics.hibernate.dao.SVCServiceMetricDetailsDao;
import com.n42.analytics.hibernate.dao.SVCServiceMetricDetailsDaoImpl;
import com.n42.analytics.util.JSONUtils;

import edu.emory.mathcs.backport.java.util.Arrays;

public class SVCResultExtractor {

	String arrayOfMetricType[] = {"IO KPI","Memory Group","Network Group","Compute KPI","Service KPI"};
	LinkedHashMap<String, List<String>> groupNmaeAnlistofMetrics = new LinkedHashMap<String, List<String>>();
	LinkedHashMap<String,Float> groupTypeandScore = new LinkedHashMap<String, Float>();
	LinkedHashMap<String,Long> metricNameandScore = new LinkedHashMap<String, Long>();

	ArrayList<Object> finalObjects = new ArrayList<Object>();
	JSONObject comparison_output = new JSONObject();
	JSONObject result = new JSONObject();
	SVCResult  svcResult;
	JSONObject resultjsonobject;
	JSONObject comparison ;
	JSONObject results;
	JSONObject metric; 


	@SuppressWarnings({ "unchecked" })
	public String SVCExtractor(String serviceName ,String inputVersion1,String inputVersion2 ){

		JSONUtils jsonUtil = new JSONUtils();
		ArrayList<String> metricstypes = new ArrayList<String>(Arrays.asList(arrayOfMetricType));

		SVCServiceMetricDetailsDao serviceMetricsDao = new SVCServiceMetricDetailsDaoImpl();
		ArrayList<SVCServiceMetricDetails> serviceMetrics = (ArrayList<SVCServiceMetricDetails>) serviceMetricsDao.getServiceMetricsbyService(serviceName);


		CasServiceMetricDetailsDao svcMetricDetails = new CasServiceMetricDetailsDaoImpl();
		List<CasServiceMetricDetails> svcMertic =svcMetricDetails.getAllCasServiceMetricDetails();
		SVCResultDao svcresultDao = new SVCResultDaoImpl();
		List<SVCResult>  svcResults = svcresultDao.getAllSVCReslut();
		String ver1=inputVersion1;
		String ver2=inputVersion2;

		for(SVCResult listofSVcResults : svcResults){

			String version1=listofSVcResults.getVersionFirst();
			String version2=listofSVcResults.getVersionSecond();

			if(StringUtils.equalsIgnoreCase(ver1, version1) && StringUtils.equalsIgnoreCase(ver2, version2)){
				svcResult= listofSVcResults;
				String compareResult = svcResult.getResult();
				resultjsonobject = jsonUtil.parseJsonFromInputString(compareResult);
				comparison = (JSONObject) resultjsonobject.get("comparison_output");
				results = (JSONObject)comparison.get("results");
				//				metric = (JSONObject)results.get("http.responsetime");
				//				System.out.println(metric.get("metricName"));
			}
		}

		for(String type:metricstypes){
			ArrayList<String> listofMetrics = new ArrayList<String>();
			if(!(StringUtils.equalsIgnoreCase(type,"Service KPI"))){
				for(CasServiceMetricDetails met:svcMertic){
					if(StringUtils.equalsIgnoreCase(type, met.getMetricType().trim())){
						listofMetrics.add(met.getMetricName());
					}
					groupNmaeAnlistofMetrics.put(type, listofMetrics);
				}
			}
			if((StringUtils.equalsIgnoreCase(type,"Service KPI"))){
				for(SVCServiceMetricDetails svcServiceMetric:serviceMetrics){
					if(StringUtils.equalsIgnoreCase(serviceName, svcServiceMetric.getMetricType().trim())){
						listofMetrics.add(svcServiceMetric.getMetricName());
					}
					groupNmaeAnlistofMetrics.put(type, listofMetrics);
				}
			}
		}

		for (Map.Entry<String, List<String>> entry : groupNmaeAnlistofMetrics.entrySet()) {
			String type = entry.getKey();
			ArrayList<String> listOfMetric = (ArrayList<String>) entry.getValue();
			Float score=0.0F;
			int sizeofListmetric = listOfMetric.size();
			Float countMetricWithNA1=0.0F;
			for(String metricName : listOfMetric){
				JSONObject resultOb = (JSONObject) results.get(metricName.trim());
				if(resultOb == null) {
					continue;
				}
				String resultMetricName = (String) resultOb.get("metricName");
				if(StringUtils.isBlank(resultMetricName)) {
					continue;
				}
				if(StringUtils.equalsIgnoreCase(resultMetricName,metricName)){
					JSONObject metricObject = (JSONObject)results.get(metricName);
					System.out.println(metricObject.get("score").getClass().getName());
					if(metricObject.get("score")!= null ){
						String scoreStr = "" + metricObject.get("score"); 
						if(NumberUtils.isNumber(scoreStr)) {
							Long num = NumberUtils.toLong(scoreStr);
							score = score + num;
							metricNameandScore.put(metricName, num);
						}
						if(StringUtils.equalsIgnoreCase(scoreStr.trim(),"NA")){
							String numb ="0";  
							Long num = NumberUtils.toLong(numb);
							score = score + num;
							metricNameandScore.put(metricName, num);
							countMetricWithNA1++;
						}
					}
				}
				
			}

			Float num = NumberUtils.toFloat(String.format("%.2f", (score/(sizeofListmetric-countMetricWithNA1))));	
			groupTypeandScore.put(type, num);
		}

		//filling data

		for (Map.Entry<String, Float> entry : groupTypeandScore.entrySet()) {
			result = new JSONObject();
			JSONArray listOfGroupTypeMetrics = new JSONArray();
			result.put("groupName", entry.getKey());
			result.put("groupScore", entry.getValue());
			if(entry.getValue()<50){
				result.put("groupColour", "red");
			}
			if(entry.getValue()>=50 && entry.getValue()<80){
				result.put("groupColour", "yellow");
			}
			if(entry.getValue()>=80){
				result.put("groupColour", "green");
			}
			ArrayList<String> metricName = (ArrayList<String>) groupNmaeAnlistofMetrics.get(entry.getKey().toString());
			for(String metri : metricName){
				JSONObject metricObjecSvcElement= (JSONObject) results.get(metri);
				System.out.println("metric name " + metri );
				Long metricScore = metricNameandScore.get(metri);

				if(metricScore == null){
					continue;
				}
				if(metricScore<50){
					metricObjecSvcElement.put("metricColour", "red");
				}
				if(metricScore>=50 && metricScore<80){
					metricObjecSvcElement.put("metricColour", "yellow");
				}
				if(metricScore>=80){
					metricObjecSvcElement.put("metricColour", "green");
				}
				listOfGroupTypeMetrics.add(metricObjecSvcElement);

			}
			
			
			result.put("Metrics", listOfGroupTypeMetrics);
			finalObjects.add(result);
		}

		
		JSONObject finalJson = new JSONObject();
		finalJson.put("serviceName", serviceName);
		finalJson.put("comparisionScore",comparison.get("comparisionScore"));
		String scoreStr = "" + comparison.get("comparisionScore");
		Long overallSocre=0l;

		if(NumberUtils.isNumber(scoreStr)) {
			overallSocre = NumberUtils.toLong(scoreStr);
		}

		if(overallSocre<50){
			finalJson.put("comparisonColour", "red");
		}
		if(overallSocre>=50 && overallSocre<80){
			finalJson.put("comparisonColour", "yellow");
		}
		if(overallSocre>=80){
			finalJson.put("comparisonColour", "green");
		}
		finalJson.put("version1name", inputVersion1);
		finalJson.put("version2name", inputVersion2);
		finalJson.put("results", finalObjects);
		comparison_output.put("comparison_output", finalJson);
		//System.out.println(finalJson);

		String outputResult = JSONUtils.getFormattedJson(comparison_output);
		if(StringUtils.isNotBlank(outputResult)) {
			outputResult = outputResult.replaceAll("3rd Qu.", "3rd_Qu");
			outputResult = outputResult.replaceAll("Min.", "Min");
			outputResult = outputResult.replaceAll("1st Qu.", "1st_Qu");
			outputResult = outputResult.replaceAll("Max.", "Max");
		}
		
		
		JSONParser parser = new JSONParser();
		
		String platformMetrics = JSONUtils.createJsonFromFile("/platform_metrics.json");
		
		try {
			JSONObject root = (JSONObject) parser.parse(outputResult);
			JSONObject comparisonOutput = (JSONObject) root.get("comparison_output");
			JSONArray results = (JSONArray) comparisonOutput.get("results");
			JSONObject plMetricsObj = (JSONObject) parser.parse(platformMetrics);
			results.add(plMetricsObj);
			comparisonOutput.put("results", results);
			root.put("comparison_output", comparisonOutput);
			outputResult = JSONUtils.getFormattedJson(root);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(outputResult);
		return outputResult;

	}

	public static void main(String args[]){

		SVCResultExtractor call = new SVCResultExtractor();
		call.SVCExtractor("mysql","v1.0","v1.1");
	}
}

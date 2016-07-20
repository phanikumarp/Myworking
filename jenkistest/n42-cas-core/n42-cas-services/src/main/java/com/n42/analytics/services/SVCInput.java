package com.n42.analytics.services;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import n42.domain.model.CasServiceMetricDetails;
import n42.domain.model.SVCResult;
import n42.domain.model.SVCServiceMetricDetails;
import n42.domain.model.SVCVersion;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.n42.analytics.hibernate.dao.CasServiceMetricDetailsDao;
import com.n42.analytics.hibernate.dao.CasServiceMetricDetailsDaoImpl;
import com.n42.analytics.hibernate.dao.SVCResultDao;
import com.n42.analytics.hibernate.dao.SVCResultDaoImpl;
import com.n42.analytics.hibernate.dao.SVCServiceMetricDetailsDao;
import com.n42.analytics.hibernate.dao.SVCServiceMetricDetailsDaoImpl;
import com.n42.analytics.hibernate.dao.SVCVersionDao;
import com.n42.analytics.hibernate.dao.SVCVersionDaoImpl;
import com.n42.analytics.util.JSONUtils;

public class SVCInput {

	JSONObject service = new JSONObject();
	JSONObject serviceObject = new JSONObject();
	JSONObject tagsData = null;
	JSONObject time = new JSONObject();
	JSONObject version1 = new JSONObject();
	JSONObject version2 = new JSONObject();
	JSONArray metrics = new JSONArray();

	CasServiceMetricDetailsDao casMetricDetails = new CasServiceMetricDetailsDaoImpl();
	List<CasServiceMetricDetails> casMertic =casMetricDetails.getAllCasServiceMetricDetails();
	SVCResultExtractor extractor = new SVCResultExtractor();


	@SuppressWarnings("unchecked")
	public String svcComparision(String serviceName,String ver1,String ver2) throws IOException{

		JSONArray metrics1 = new JSONArray();
		SVCVersionDao svcVersionDao = new SVCVersionDaoImpl();
		SVCVersion v1= svcVersionDao.getSVCVersionDetails(ver1);
		SVCVersion v2= svcVersionDao.getSVCVersionDetails(ver2);

		SVCResultDao svcResultDao = new SVCResultDaoImpl();
		SVCResult svcResult = svcResultDao.getSVCResult(serviceName, ver1, ver2);

		SVCServiceMetricDetailsDao serviceMetricsDao = new SVCServiceMetricDetailsDaoImpl();
		ArrayList<SVCServiceMetricDetails> serviceMetrics = (ArrayList<SVCServiceMetricDetails>) serviceMetricsDao.getServiceMetricsbyService(serviceName);

		if(svcResult == null) {
			int flag1=0;
			for(CasServiceMetricDetails metricdetails1 : casMertic){

				JSONArray tagList1 = new JSONArray();
				JSONObject metricTypevsValue = new JSONObject();
				JSONObject aggregator = new JSONObject();
				String MetricType = metricdetails1.getMetricType();
				String MetricName = metricdetails1.getMetricName();
				String metricAgg =  metricdetails1.getKairosAggregator();

				/////////////////////////////////////////////
				if(flag1==0){
					for(SVCServiceMetricDetails serviceMetricsdetails : serviceMetrics){

						tagList1 = new JSONArray();
						metricTypevsValue = new JSONObject();
						metricTypevsValue.put("metricName", serviceMetricsdetails.getMetricName());
						metricTypevsValue.put("metricType", serviceMetricsdetails.getMetricType());
						tagsData = new JSONObject();
						tagsData.put("tagName", "host");
						tagsData.put("tagValue", "67.205.124.58");
						aggregator.put("unit", "seconds");
						aggregator.put("Function", "sum");

						tagList1.add(tagsData);
						metricTypevsValue.put("tagsData", tagList1);

						metricTypevsValue.put("aggregator", aggregator);
						metrics1.add(metricTypevsValue);

					}
					flag1++;
				}

				////////////////////////////////////////////

				tagList1 = new JSONArray();
				metricTypevsValue = new JSONObject();
				tagsData = new JSONObject();
				
				metricTypevsValue.put("metricName", MetricName);
				metricTypevsValue.put("metricType", MetricType);
				tagsData.put("tagName", "host");
				tagsData.put("tagValue", "insure-qa");
				aggregator.put("unit", "seconds");
//				if(StringUtils.startsWithIgnoreCase(MetricName, "http")){
//					tagsData.put("tagValue", "insure-qa");
//				}
				if(StringUtils.equalsIgnoreCase("http.requests", MetricName)){
					aggregator.put("time", "10");
				}
				else{
					aggregator.put("time", "1");
				}
				aggregator.put("Function", metricAgg);


				tagList1.add(tagsData);
				metricTypevsValue.put("tagsData", tagList1);

				metricTypevsValue.put("aggregator", aggregator);
				metrics1.add(metricTypevsValue);

			}

			version1.put("startTime",NumberUtils.toLong(v1.getStartTime()));
			version1.put("endTime",NumberUtils.toLong(v1.getEndTime()));
			version1.put("metrics", metrics1);


			JSONArray metrics2 = new JSONArray();
			int flag2=0;
			for(CasServiceMetricDetails metricdetails2 : casMertic){

				JSONArray tagList2 = new JSONArray();

				JSONObject metricTypevsValue = new JSONObject();
				JSONObject aggregator = new JSONObject();
				String MetricType = metricdetails2.getMetricType();
				String MetricName = metricdetails2.getMetricName();
				String metricAgg =  metricdetails2.getKairosAggregator();

				/////////////////////////////////////////////
				
				if(flag2==0){
					for(SVCServiceMetricDetails serviceMetricsdetails : serviceMetrics){

						tagList2 = new JSONArray();
						metricTypevsValue = new JSONObject();

						metricTypevsValue.put("metricName", serviceMetricsdetails.getMetricName());
						metricTypevsValue.put("metricType", serviceMetricsdetails.getMetricType());
						tagsData = new JSONObject();
						tagsData.put("tagName", "host");
						tagsData.put("tagValue", "67.205.124.58");
						aggregator.put("unit", "seconds");
						aggregator.put("Function", "sum");

						tagList2.add(tagsData);
						metricTypevsValue.put("tagsData", tagList2);

						metricTypevsValue.put("aggregator", aggregator);
						metrics2.add(metricTypevsValue);

					}
					flag2++;
				}

				////////////////////////////////////////////

				tagList2 = new JSONArray();
				metricTypevsValue = new JSONObject();
				tagsData = new JSONObject();

				metricTypevsValue.put("metricName", MetricName);
				metricTypevsValue.put("metricType", MetricType);
				tagsData.put("tagName", "host");
				tagsData.put("tagValue", "insure-qa");
				aggregator.put("unit", "seconds");
//				if(StringUtils.startsWithIgnoreCase(MetricName, "http")){
//					tagsData.put("tagValue", "insure-qa");
//				}
				if(StringUtils.equalsIgnoreCase("http.requests", MetricName)){
					aggregator.put("time", "10");
				}
				else{
					aggregator.put("time", "1");
				}
				aggregator.put("Function", metricAgg);


				tagList2.add(tagsData);
				metricTypevsValue.put("tagsData", tagList2);
				metricTypevsValue.put("aggregator", aggregator);
				metrics2.add(metricTypevsValue);

			}

			version2.put("startTime",NumberUtils.toLong(v2.getStartTime()));
			version2.put("endTime",NumberUtils.toLong(v2.getEndTime()));
			version2.put("metrics", metrics2);

			service.put("serviceName",serviceName);
			service.put("version1",version1);
			service.put("version2",version2);
			service.put("version1Name", ver1);
			service.put("version2Name", ver2);


			serviceObject.put("service", service);

			String inputToR = JSONUtils.getFormattedJson(serviceObject);
			System.out.println(inputToR);
			
			Process shell = Runtime.getRuntime().exec(new String[] { "/usr/bin/Rscript","/opt/canaryplusversion.R",inputToR});
			BufferedReader reader = new BufferedReader(new InputStreamReader(shell.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			System.out.println("###############@@@@@@@@@@@@@##################");
			System.out.println(System.currentTimeMillis() + " Start calling extractor form R this time ***************");
			String outIfPresent = extractor.SVCExtractor(serviceName, ver1, ver2);
			return outIfPresent;
		}

		System.out.println(System.currentTimeMillis() + "Start calling extractor from  program this time");
		String outIfPresent = extractor.SVCExtractor(serviceName, ver1, ver2);
		return outIfPresent;
		//				return "hello";

	}


	public static void main (String args[]) throws IOException{
		SVCInput in = new SVCInput();
		in.svcComparision("Tomcat","v1.3","v1.0");

	}

}

package com.n42.analytics.services;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import n42.domain.model.ServiceVersionCharacterstic;

import com.n42.analytics.hibernate.dao.ServiceVersionCharactersticDao;
import com.n42.analytics.hibernate.dao.ServiceVersionCharactersticDaoImpl;

public class SVCModelService {

	public Integer saveSVCResults(JSONObject jsonObj){
//		ServiceVersionResultDao sVD = new ServiceVersionResultDaoImpl();
//		ServiceVersionResult sVR = new ServiceVersionResult();
//		JSONObject jsonObject =  (JSONObject) jsonObj.get("Models");
//		sVR.setAppId((Integer) (jsonObject.get("appId")));
//		sVR.setService((jsonObject.get("servicename")).toString());
//		sVR.setLoadMetric((jsonObject.get("loadmetric")).toString());
//		sVR.setResponseMetric((jsonObject.get("responsemetric")).toString());
//		List<JSONObject> bucketArray = (List<JSONObject>) jsonObject.get("buckets");
//		List<Bucket> buckets = new ArrayList<Bucket>(); 
//		List<String> bucketIds = new ArrayList<String>(); 
//		
//		for(JSONObject bucketobj : bucketArray){
//			Bucket bucket = new Bucket();
//			bucket.setBucketId(bucketobj.get("bucket_id").toString());
//			bucketIds.add(bucketobj.get("bucket_id").toString());
//			bucket.setLoadRange(bucketobj.get("load_range").toString());
//			List<JSONObject> coefficientArray = (List<JSONObject>) bucketobj.get("coefficients");
//			List<Coefficient> coefficients = new ArrayList<Coefficient>(); 
//		
//			for(JSONObject coefficientObj : coefficientArray){
//				Coefficient coefficient = new Coefficient();
//				coefficient.setTau((Float) coefficientObj.get("tau"));
//				coefficient.setIntercept((Float) coefficientObj.get("Intercept"));
//				coefficient.setLoad((Float) coefficientObj.get("load"));
//				coefficients.add(coefficient);
//			}
//			
//			bucket.setCoefficients(coefficients);
//			buckets.add(bucket);
//		}
//		
//		sVR.setBucketIds(bucketIds);;
//		sVD.saveServiceVersionModel(sVR);
		return 1;
	}

	public static void main(String args[]){
		SVCModelService modelService = new SVCModelService();
		JSONParser parser = new JSONParser();
		try {
			
			JSONObject json = (JSONObject) parser.parse("{\n\t\"Models\": {\n\t\t\"Service\": \"haproxy\",\n\t\t\"AppId\": 13,\n\t\t\"LoadMetric\": \"haproxy.requests\",\n\t\t\"ResponseMetric\": \"haproxy.responsetime\",\n\t\t\"buckets\": [{\n\t\t\t\"bucket_id\": \"bucket1\",\n\t\t\t\"load_range\": \"0-2000\",\n\t\t\t\"coefficients\": [{\n\t\t\t\t\"tau\": 0.05,\n\t\t\t\t\"Intercept\": 4.89413710981832,\n\t\t\t\t\"load\": 0.00127816682808155\n\t\t\t}, {\n\t\t\t\t\"tau\": 0.95,\n\t\t\t\t\"Intercept\": 6.40148690516579,\n\t\t\t\t\"load\": 0.000226282811788825\n\t\t\t}]\n\t\t}, {\n\t\t\t\"bucket_id\": \"bucket2\",\n\t\t\t\"load_range\": \"10000-12000\",\n\t\t\t\"coefficients\": [{\n\t\t\t\t\"tau\": 0.05,\n\t\t\t\t\"Intercept\": -166.949544293227,\n\t\t\t\t\"load\": 0.0161174247443671\n\t\t\t}, {\n\t\t\t\t\"tau\": 0.95,\n\t\t\t\t\"Intercept\": -91.2274835412986,\n\t\t\t\t\"load\": 0.0104782663127407\n\t\t\t}]\n\t\t}, {\n\t\t\t\"bucket_id\": \"bucket3\",\n\t\t\t\"load_range\": \"12000-14000\",\n\t\t\t\"coefficients\": [{\n\t\t\t\t\"tau\": 0.05,\n\t\t\t\t\"Intercept\": 110.657809909573,\n\t\t\t\t\"load\": -0.00747047681665369\n\t\t\t}, {\n\t\t\t\t\"tau\": 0.95,\n\t\t\t\t\"Intercept\": 13.1297001428433,\n\t\t\t\t\"load\": 0.00202572993908714\n\t\t\t}]\n\t\t}]\n\t}\n}");
			modelService.saveSVCResults(json);
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

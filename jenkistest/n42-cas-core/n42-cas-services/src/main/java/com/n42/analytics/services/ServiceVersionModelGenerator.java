package com.n42.analytics.services;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import n42.domain.model.Host;
import n42.domain.model.Service;
import n42.domain.model.ServiceVersion;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.n42.analytics.util.ModelUtils;
import com.n42.analytics.util.JSONUtils;
import com.n42.analytics.util.N42Properties;


public class ServiceVersionModelGenerator {

	private static final String R_HOME = N42Properties.getInstance().getProperty("r.env.path");
	private static final String CHARACTERIZATION_SCRIPT = N42Properties.getInstance().getProperty("characterization.script.path");
	private static final String COMPARATOR_SCRIPT = N42Properties.getInstance().getProperty("comparator.script.path");

	public static List<Integer> sVCID = new ArrayList<Integer>();
	

	private Map<String, Set<String>> modelsMap  = new LinkedHashMap<String, Set<String>>();
	private LinkedHashMap<String, Object> svcJsonObject = new LinkedHashMap<String, Object>();
	private Map<Integer, Integer> serviceVersionIdVsServiceIdMap = ModelUtils.getServiceVersionIdVsServiceIdMap(null);
	private Map<Integer, String> applicationVersionVsJsonModelMap = new HashMap<Integer, String>();
	private Map<Integer, String> serviceVersionVsJsonModelMap = new HashMap<Integer, String>();
	private Map<Integer, ServiceVersion> serviceVersionIdMap = ModelUtils.getServiceVersionIdMap(null);

	public Long getTimeInMillis(String strDate) {
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		long epoch = 0;
		try {
			Date date = dateFormatGmt.parse(strDate);
			epoch = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(epoch); // 1055545912454

		return epoch;
	}

	//appId, appVersionId, versions, serviceVersion, serviceName
	//public List<String> svcChecker(Integer appId,List<Integer> appVersionId,List<Integer> serviceVersionID){
	public List<String> svcChecker(Integer appId,Integer appVersionId,List<Integer> versions,Boolean serviceVersioncom, String ServiceName, Integer serviceId){
	
		List<String> listOfModels = null;

		//if(CollectionUtils.isNotEmpty(serviceVersionID)){
//			listOfModels = new ArrayList<String>();
//			serviceVersionVsJsonModelMap = ModelUtils.getServiceVersionVsJsonModelMap(null);
//			for(Integer serviceVersionId :versions ){
//			//for(Integer serviceVersionId :serviceVersionID ){
//				String serviceVersionModel = new String();
//				for(Map.Entry<Integer,String> entry : serviceVersionVsJsonModelMap.entrySet()){
//					Integer serviceVersion = entry.getKey();
//					if(serviceVersion.equals(serviceVersionId)){
//						serviceVersionModel = entry.getValue();
//					}
//				}
//				//serviceVersionID already have Charterization json in DB.
//				if(StringUtils.isEmpty(serviceVersionModel)){
//					String model = new String();
//					model =	createCharterizationModelforService(appId,serviceVersionId,appVersionId,serviceId);
//					listOfModels.add(model);
//				}
//			}
		return listOfModels;
		//return JSONUtils.getFormattedJson(listOfModels);
	}//svcChecker method closed.....!

	public String createCharterizationModelforService(Integer appId,Integer serviceVersionID,Integer applicationVersionID, Integer serviceId2){

//		ApplicationDao applicationDao = new ApplicationDaoImpl();
//		Application application = applicationDao.getApplicationById(appId,null);
//		Map<Integer, Service> serviceIdVsService = ModelUtils.getServiceIdMap(application.getName(),null, null);
//		List<Integer> vmIds = application.getVmIds();
//
//		for(Map.Entry<Integer,Integer> entry : serviceVersionIdVsServiceIdMap.entrySet()){
//			Integer svcVersionId = entry.getKey();
//			if(svcVersionId.equals(serviceVersionID)){
//				Integer serviceId = entry.getValue();
//				for(Integer vmId : vmIds){
//					VirtualMachine vm = vmIdVsVm.get(vmId);
//					if(vm != null) {
//						List<Integer> curentServiceIds = vm.getServiceIds();
//						if(serviceId.equals(curentServiceIds.get(0))){
//							Service currentService = serviceIdVsService.get(curentServiceIds.get(0));
//							String currentServiceName = currentService.getName();
//
//							LinkedHashMap<String, Object> curentServiceObject = new LinkedHashMap<String, Object>();
//							LinkedHashMap<String, Object> metricObject1 = new LinkedHashMap<String, Object>();
//							LinkedHashMap<String, Object> tagObject1 = new LinkedHashMap<String, Object>();
//							LinkedHashMap<String, Object> aggregatorObject1 = new LinkedHashMap<String, Object>();
//							ArrayList<Object> metricsList = new ArrayList<Object>();
//							ArrayList<Object> tagsList = new ArrayList<Object>();
//							ServiceVersion serviceVersion = serviceVersionIdMap.get(svcVersionId);
//							//used for load metric		
//							curentServiceObject.put("serviceName",currentServiceName );
//							curentServiceObject.put("appID",appId );
//							curentServiceObject.put("applicationVersionID",applicationVersionID );
//							curentServiceObject.put("serviceVersionId",serviceVersionID );
//							curentServiceObject.put("serviceId",serviceId );
//
//							aggregatorObject1.put("Function", "sum");
//							aggregatorObject1.put("time", "60");
//							aggregatorObject1.put("unit", "seconds");
//							tagObject1.put("tagName","host");
//							tagObject1.put("tagValue",vm.getHost());
//							tagsList.add(tagObject1);
//							metricObject1.put("metricType" , "Load");
//							metricObject1.put("metricName" , currentServiceName+"."+"requests");
//							metricObject1.put("startTime" ,getTimeInMillis(serviceVersion.getStartTime()));
//							metricObject1.put("endTime" , getTimeInMillis(serviceVersion.getEndTime()));	
//							metricObject1.put("tagsData" , tagsList);	
//							metricObject1.put("aggregator" , aggregatorObject1);	
//							metricsList.add(metricObject1);			
//
//							//used for Response metric		
//							LinkedHashMap<String, Object> metricObject2 = new LinkedHashMap<String, Object>();
//							LinkedHashMap<String, Object> aggregatorObject2 = new LinkedHashMap<String, Object>();
//							LinkedHashMap<String, Object> tagObject2 = new LinkedHashMap<String, Object>();
//							ArrayList<Object> tagsList2 = new ArrayList<Object>();
//
//							aggregatorObject2.put("Function", "sum");
//							aggregatorObject2.put("time", "60");
//							aggregatorObject2.put("unit", "seconds");
//							tagObject2.put("tagName","host");
//							tagObject2.put("tagValue",vm.getHost());
//							tagsList2.add(tagObject2);			
//							metricObject2.put("metricType" , "Response");
//							metricObject2.put("metricName" , currentServiceName+"."+"responsetime");
//							metricObject2.put("startTime" , getTimeInMillis(serviceVersion.getStartTime()));
//							metricObject2.put("endTime" , getTimeInMillis(serviceVersion.getEndTime()));
//							metricObject2.put("tagsData" , tagsList2);
//							metricObject2.put("aggregator" , aggregatorObject2);	
//							metricsList.add(metricObject2);	
//
//							curentServiceObject.put("Metrics", metricsList);
//							svcJsonObject.put("service",curentServiceObject );
//
//						}
//					}
//				}
//			}
//		}

		return JSONUtils.getFormattedJson(svcJsonObject);
	}

	public String createCharterizationModelforApplication(Integer appId, Integer Versionid,Integer appVersionId){

		modelsMap = new LinkedHashMap<String, Set<String>>();
//		modelsMap = modelMapUtil(appId);
//
//		int count = 1;
//		for(Map.Entry<String, Set<String>> entry1 : modelsMap.entrySet()){
//
//			if(count == 1){
//				count++;
//				String currentServiceName = entry1.getKey().split("#")[1];
//				String vmId = entry1.getKey().split("#")[0];
//				VirtualMachine vm = vmIdVsVm.get(Integer.parseInt(vmId));
//
//				LinkedHashMap<String, Object> curentServiceObject = new LinkedHashMap<String, Object>();
//				LinkedHashMap<String, Object> metrcisObjects = new LinkedHashMap<String, Object>();
//				LinkedHashMap<String, Object> tagObjects = new LinkedHashMap<String, Object>();
//				LinkedHashMap<String, Object> aggregatorObjects = new LinkedHashMap<String, Object>();
//				ArrayList<Object> metrcisList = new ArrayList<Object>();
//				ArrayList<Object> tagsList = new ArrayList<Object>();
//				AppVersion applicationVersion = applicationVersionIdMap.get(Versionid);
//				//used for load metric		
//			//	appVersionId=1&serviceVersionId=1"
//				curentServiceObject.put("serviceName",currentServiceName);
//				curentServiceObject.put("appID",appId );
//				curentServiceObject.put("applicationVersionID",Versionid );
//				curentServiceObject.put("serviceVersionId",0 );
//				curentServiceObject.put("serviceId",123456);
//				
//				
//		
//				aggregatorObjects.put("Function", "sum");
//				aggregatorObjects.put("time", "60");
//				aggregatorObjects.put("unit", "seconds");
//				tagObjects.put("tagName","host");
//				tagObjects.put("tagValue",vm.getHost());
//				tagsList.add(tagObjects);
//				metrcisObjects.put("metricType" , "Load");
//				metrcisObjects.put("metricName" , currentServiceName+"."+"requests");
//				metrcisObjects.put("startTime" , getTimeInMillis(applicationVersion.getStartTime()));
//				metrcisObjects.put("endTime" , getTimeInMillis(applicationVersion.getEndTime()));	
//				metrcisObjects.put("tagsData" , tagsList);	
//				metrcisObjects.put("aggregator" , aggregatorObjects);	
//				metrcisList.add(metrcisObjects);	
//
//				//used for response metric
//				LinkedHashMap<String, Object> metrcisObjects1 = new LinkedHashMap<String, Object>();
//				LinkedHashMap<String, Object> aggregatorObjects1 = new LinkedHashMap<String, Object>();
//				LinkedHashMap<String, Object> tagObjects1 = new LinkedHashMap<String, Object>();
//				ArrayList<Object> tagsList1 = new ArrayList<Object>();
//
//				aggregatorObjects1.put("Function", "sum");
//				aggregatorObjects1.put("time", "60");
//				aggregatorObjects1.put("unit", "seconds");
//				tagObjects1.put("tagName","host");
//				tagObjects1.put("tagValue",vm.getHost());
//				tagsList1.add(tagObjects1);			
//				metrcisObjects1.put("metricType" , "Response");
//				metrcisObjects1.put("metricName" , currentServiceName+"."+"responsetime");
//				metrcisObjects1.put("startTime" , getTimeInMillis(applicationVersion.getStartTime()));
//				metrcisObjects1.put("endTime" , getTimeInMillis(applicationVersion.getEndTime()));
//				metrcisObjects1.put("tagsData" , tagsList1);
//				metrcisObjects1.put("aggregator" , aggregatorObjects1);	
//				metrcisList.add(metrcisObjects1);	
//
//				curentServiceObject.put("Metrics", metrcisList);
//				svcJsonObject.put("service",curentServiceObject );
//
//			}
//		}

		return JSONUtils.getFormattedJson(svcJsonObject);
	}

	//appId, appVersionId, versions, serviceVersion, serviceName
	public List<String> comparatorInput(Integer appId,List<Integer> VersionIds,Boolean serviceVersion){
		List<String> inputTocomparator = new ArrayList<String>();
//		serviceVersionVsJsonModelMap = ModelUtils.getServiceVersionVsJsonModelMap(null);
//		Map<Integer, Integer> serviceVersionVsN42ID  = ModelUtils.getServiceVersionVsN42ID(null);
//		if(serviceVersion){
//			sVCID = new ArrayList<Integer>();
//		//if(CollectionUtils.isNotEmpty(serviceVersionIds)){
//			for(Integer serviceVersionId : VersionIds ) {
//				String input = serviceVersionVsJsonModelMap.get(serviceVersionId);
//				sVCID.add(serviceVersionVsN42ID.get(serviceVersionId));
//				inputTocomparator.add(input);
//			}
//		}

		return inputTocomparator;
	}





	public static void main(String[] args) {
		ServiceVersionModelGenerator service =  new ServiceVersionModelGenerator();
		List<Integer> abc1 = new ArrayList<Integer>();
		abc1.add(7);
		abc1.add(10);
		List<Integer> abc2 = new ArrayList<Integer>();
		//abc2.add(4);
		//abc2.add(5);
		//appId, appVersionId, versions, serviceVersion, serviceName
		List<String> inputToRcode = service.svcChecker(23,null,abc1,false,"abcdeghijklm",null);
		//String inputToRcode = "{\"Service\" : {\"serviceName\" : \"haproxy\",\"appID\" : 15,\"Metrics\" : [ {\"metricType\" : \"Load\",\"metricName\" : \"haproxy.requests\",\"startTime\" : \"1459810680000\",\"endTime\" : \"1459821360000\",\"tagsData\" : [ {\"tagName\" : \"host\",\"tagValue\" : \"haproxy\"} ],\"aggregator\" : {\"Function\" : \"sum\",\"time\" : \"1\",\"unit\" : \"minutes\"}}, {\"metricType\" : \"Response\",\"metricName\" : \"haproxy.responsetime\",\"startTime\" : \"1459810680000\",\"endTime\" : \"1459821360000\",\"tagsData\" : [ {\"tagName\" : \"host\",\"tagValue\" : \"haproxy\"} ],\"aggregator\" : {\"Function\" : \"sum\",\"time\" : \"1\",\"unit\" : \"minutes\"}} ]}}";

		BufferedReader reader =  null;
		Process shell = null;
		for(String inputToR : inputToRcode){
			try {


				shell = Runtime.getRuntime().exec(new String[] { "/usr/bin/Rscript","/home/ipsg/lalit/characterization.R",inputToR});

				reader = new BufferedReader(new InputStreamReader(shell.getInputStream())); 
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//System.out.println(JSONUtils.getFormattedJson(null));
		//		r.env.path=/usr/bin/Rscript
		//				characterization.script.path=/home/ipsg/lalit/characterization.R
		//				comparator.script.path=/home/ipsg/lalit/characterization.R

		List<String> inputToCom = service.comparatorInput(23,abc1,false);

		try {

			System.out.println(inputToCom.get(0));
			System.out.println("***************************************");
			System.out.println(inputToCom.get(1));
			shell = Runtime.getRuntime().exec(new String[] { "/usr/bin/Rscript","/home/ipsg/lalit/comparatornew.R",inputToCom.get(0),inputToCom.get(1),abc1.get(0).toString(),abc1.get(1).toString()});

			reader = new BufferedReader(new InputStreamReader(shell.getInputStream())); 
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}


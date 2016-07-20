package com.n42.analytics.services;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//
//import weka.filters.unsupervised.attribute.Add;
//
//import com.n42.analysis.mlp.Instances;
//import com.n42.analysis.mlp.ServiceVersionModelGenerator;
//import com.n42.analytics.hibernate.dao.ComparatorAnalysisDao;
//import com.n42.analytics.hibernate.dao.ComparatorAnalysisDaoImpl;
//import com.n42.analytics.util.ModelUtils;
//import com.n42.extractor.http.MetricDataIInstancesCreation;
//
//import n42.domain.model.AppVersion;
//import n42.domain.model.Application;
//import n42.domain.model.ComparatorAnalysis;
//import n42.domain.model.Service;
//import n42.domain.model.ServiceVersion;
//import n42.domain.model.ServiceVersionMetric;
//import n42.domain.model.VirtualMachine;
//
public class SVCUIService {
//
//	Map<Integer, Application> appIdVsApp = ModelUtils.getApplicationIdMap(null);	
//	Map<Integer, Service> serviceIdVsService = ModelUtils.getServiceIdMap(null, null, null);
//	Map<Integer, VirtualMachine> vMIdVsVM = ModelUtils.getVmN42IdMap(null);
//	Map<String, List<ServiceVersionMetric>> metricTypeVsSVCMetric = ModelUtils.getMetricTypeVsSVCMetricMap();
//	Map<Integer, AppVersion> appVersionIdVsAppVersion = ModelUtils.getAppVersionN42IdMap(null);
//	Map<Integer, ServiceVersion> serviceVersionIdVsServiceVersion = ModelUtils.getServiceVersionN42IdMap(null);
//	Map<Application, List<Service>> appVsServices = new HashMap<Application, List<Service>>();
//	Map<Application, List<AppVersion>> appVsAppVersions = new HashMap<Application, List<AppVersion>>();
//	Map<Service, List<ServiceVersion>> servicesVsServiceVersions = new HashMap<Service, List<ServiceVersion>>();
//	Map<Integer, VirtualMachine> serviceIdVsVm = new HashMap<Integer, VirtualMachine>();
//
//	JSONArray tableArray = new JSONArray();
//
//	@SuppressWarnings("unchecked")
//	public JSONObject getVersionDetails(Integer appId, Boolean serviceVersion){
//		updateInstanceMaps(appId);
//		Application application = appIdVsApp.get(appId);
//		JSONObject appObj = new JSONObject();
//		try{
//
//			JSONArray appVersionArray = new JSONArray();
//
//			for(AppVersion appVersion : appVsAppVersions.get(application)){
//				Set<String> serviceChecker = new HashSet<String>();
//				JSONObject appVersionObj = new JSONObject();
//				appVersionObj.put("appVersion", appVersion.getName());
//				appVersionObj.put("ReleaseDate", appVersion.getStartTime());
//				appVersionObj.put("applicationVersionId", appVersion.getN42Id());
//				appVersionArray.add(appVersionObj);
//
//				if(serviceVersion){
//					JSONArray serviceArray = new JSONArray();
//
//					for(Service service : appVsServices.get(application)){
//						JSONObject serObj = new JSONObject();
//						if(serviceChecker.add(service.getName())){
//							if(servicesVsServiceVersions.get(service)!= null){
//								serObj.put("serviceName", service.getName());
//								serObj.put("serviceId", service.getN42Id());
//								JSONArray versionArray = new JSONArray();
//
//								for(Service service2 : appVsServices.get(application)){
//
//									for(ServiceVersion version : servicesVsServiceVersions.get(service2)){
//										if(StringUtils.equalsIgnoreCase(service2.getName(),service.getName())){
//											if(version.getAppVersionIds().contains( appVersion.getN42Id())){
//												JSONObject serviceVerObj = new JSONObject();
//												serviceVerObj.put("serviceVersion", version.getName());
//												serviceVerObj.put("ReleaseDate", version.getStartTime());
//												serviceVerObj.put("versionId", version.getN42Id());
//												versionArray.add(serviceVerObj );
//											}
//										}
//									}
//									serObj.put("service_Versions", versionArray);
//								}
//								serviceArray.add(serObj);
//							}
//							appVersionObj.put("services",  serviceArray);
//						}
//					}
//				}
//				appObj.put("Application_versions", appVersionArray);
//			}
//		}catch(Exception e ){
//			System.out.println("Errors Caugth");
//		}
//		return appObj;
//	}
//
//	@SuppressWarnings("unchecked")
//	public JSONObject getVersionData(Integer appId, String serviceName, List<Integer> versionIds, List<Integer> serviceVersionIds,Boolean serviceComparator){
//
//
//		ComparatorAnalysisDao cADao = new ComparatorAnalysisDaoImpl();
//		List<ComparatorAnalysis> analysisResults = cADao.getAllComparatorAnalysis();
//		JSONObject result = new JSONObject();
//		try {
//			updateInstanceMaps(appId);
//			JSONObject jsonObject = new JSONObject();
//			JSONParser parser = new JSONParser();
//
//			for(ComparatorAnalysis analysisResultTemp : analysisResults){
//				if(versionIds.contains(analysisResultTemp.getsVCId1())&& versionIds.contains(analysisResultTemp.getsVCId2()) ){
//					jsonObject = (JSONObject) parser.parse(analysisResultTemp.getAnalysisJson());
//				}
//
//			}
//
//			if(!serviceComparator){
//				for(Entry<String, List<Integer>> serviceDetails: updateServiceDetailsFromApp( appId, serviceName, serviceVersionIds).entrySet()){
//					versionIds = serviceDetails.getValue();
//					serviceName = serviceDetails.getKey();
//				}
//
//			}
//			else{
//				versionIds = serviceVersionIds;
//			}
//
//			JSONObject comparedBucketsData =  (JSONObject) jsonObject.get("bucketComparison");
//			int count = 0; 
//			JSONArray versionsData = new JSONArray();
//
//
//			for(Integer versionId : versionIds){
//				ServiceVersion serviceVersion = serviceVersionIdVsServiceVersion.get(versionId);
//				Integer serviceId = serviceVersion.getServiceId();
//				VirtualMachine vM = serviceIdVsVm.get(serviceId);
//				List<ServiceVersionMetric> loadMetrics = metricTypeVsSVCMetric.get("load");
//				List<ServiceVersionMetric> performanceMetrics = (List<ServiceVersionMetric>) metricTypeVsSVCMetric.get("performance");
//				String[] startAndEndDays = new String[2];
//				Map<String, Set<String>> metricHostMap= new HashMap<String, Set<String>>();
//				Map<String, Boolean> metricNamesAndCummulativeFlags = new HashMap<String, Boolean>();;
//				Map<String, Set<String>> extraMetricHostMap= new HashMap<String, Set<String>>();
//				Map<String, String> metricNamesAndAggregrator = new HashMap<String, String>();
//				//getMetricDetailsForServiceMetric(loadMetrics, serviceName, vM, metricHostMap, metricNamesAndCummulativeFlags);
//
//				for(ServiceVersionMetric loadMetric : loadMetrics ){
//					if(StringUtils.equalsIgnoreCase(loadMetric.getServiceName(),serviceName)){
//						String tagVsValue = "host@" + vM.getHost();
//						Set<String> tagVsValueSet = new HashSet<String>();
//						tagVsValueSet.add(tagVsValue);
//						metricHostMap.put(loadMetric.getMetric() , tagVsValueSet);
//						metricNamesAndCummulativeFlags.put(loadMetric.getMetric() , loadMetric.getCummulative());
//						metricNamesAndAggregrator.put(loadMetric.getMetric(), loadMetric.getAggregator());
//					}
//				}
//				//getMetricDetailsForServiceMetric(performanceMetrics, serviceName, vM, metricHostMap, metricNamesAndCummulativeFlags);
//
//				for(ServiceVersionMetric performanceMetric : performanceMetrics ){
//					if(StringUtils.equalsIgnoreCase(performanceMetric.getServiceName(),serviceName)){
//						String tagVsValue = "host@" + vM.getHost();
//						Set<String> tagVsValueSet = new HashSet<String>();
//						tagVsValueSet.add(tagVsValue);
//						metricHostMap.put(performanceMetric.getMetric() , tagVsValueSet);
//						metricNamesAndCummulativeFlags.put(performanceMetric.getMetric() , performanceMetric.getCummulative());
//						metricNamesAndAggregrator.put(performanceMetric.getMetric(), performanceMetric.getAggregator());
//					}
//				}
//
//				startAndEndDays[0] = serviceVersion.getStartTime();
//				startAndEndDays[1] = serviceVersion.getEndTime();
//
//				Instances instances = MetricDataIInstancesCreation.getDataSet1(metricHostMap, metricNamesAndCummulativeFlags,
//						metricNamesAndAggregrator, extraMetricHostMap, startAndEndDays);
//				JSONObject versionData = new JSONObject();
//				JSONArray versionDataPoints = new JSONArray();
//				Double minY = new Double(0);
//				Double maxY = new Double(0);
//
//				for(int i=0; i < instances.size();i++){
//					JSONObject versionDataPoint = new JSONObject();
//					if(i==0){
//						minY = instances.getInstances().get(i).getValue(1).getNumericValueAsDouble();
//						maxY = instances.getInstances().get(i).getValue(1).getNumericValueAsDouble();
//					}
//					else if( minY > instances.getInstances().get(i).getValue(1).getNumericValueAsDouble()){
//						minY = instances.getInstances().get(i).getValue(1).getNumericValueAsDouble();
//					}
//					else if( maxY < instances.getInstances().get(i).getValue(1).getNumericValueAsDouble()){
//						maxY = instances.getInstances().get(i).getValue(1).getNumericValueAsDouble();
//					}
//					versionDataPoint.put("x", instances.getInstances().get(i).getValue(2));
//					versionDataPoint.put("y", instances.getInstances().get(i).getValue(1));
//					versionDataPoints.add(versionDataPoint);
//				}
//				JSONArray comparedBucketsArray = createJsonObjectForBucketsAndUpdateTableJson(comparedBucketsData, minY, maxY, count, serviceVersion.getName());
//				versionData.put("graphData", versionDataPoints);
//				versionData.put("bucketsData", comparedBucketsArray);
//				versionData.put("versionId", versionId);
//				versionData.put("versionName", serviceVersion.getName());
//				versionsData.add(versionData);
//				count++;
//			}
//			result.put("data", versionsData);
//			//for(int i=0 ; i < tableArray.size() ; i++){
//			// TODO add extra details of the comparision to the output Json to UI, Exp : process Id 
//			//}
//			for(int i = 0; i < tableArray.size(); i++ ){
//				JSONObject tableObject = (JSONObject) tableArray.get(i);
//				if(tableObject.get("error") != null){
//					result.put("error", tableObject.get("error").toString());
//				}
//				else{
//					result.put("comparedStats", tableArray);
//				}
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	public void updateInstanceMaps(Integer appId){
//
//		Application application = new Application();
//		application = appIdVsApp.get(appId);
//		List<Integer> vMIDs= application.getVmIds();
//		List<Service> appServices = new ArrayList<Service>();  
//
//		for(Integer vMID : vMIDs){
//			VirtualMachine vM = vMIdVsVM.get(vMID);
//			List<Integer> serviceIds =  vM.getServiceIds();
//
//			for(Integer serviceId : serviceIds){
//				Service service = serviceIdVsService.get(serviceId);
//				appServices.add(service);
//				serviceIdVsVm.put(serviceId,vM);
//				List<ServiceVersion> versions = new ArrayList<ServiceVersion>();
//
//				for(Integer serviceVersion : service.getVersionIds()){
//					Integer versionId = serviceVersion;
//					ServiceVersion version = serviceVersionIdVsServiceVersion.get(versionId);
//					versions.add(version);
//				}
//				servicesVsServiceVersions.put(service, versions);
//			}
//		}
//		appVsServices.put(application, appServices);
//		List<Integer> appVersionIds = application.getAppVersionIds();
//		List<AppVersion> appVersions = new ArrayList<AppVersion>();
//
//		for(Integer appVersionId : appVersionIds){
//			AppVersion appVersion = appVersionIdVsAppVersion.get(appVersionId);
//			appVersions.add(appVersion);
//		}
//		appVsAppVersions.put(application, appVersions);
//
//
//	}
//
//	private void getMetricDetailsForServiceMetric(List<ServiceVersionMetric> loadMetrics, String serviceName, 
//			VirtualMachine vM, Map<String, Set<String>> metricHostMap, Map<String, Boolean> metricNamesAndCummulativeFlags) {
//
//		for(ServiceVersionMetric loadMetric : loadMetrics ){
//			if(StringUtils.equalsIgnoreCase(loadMetric.getServiceName(),serviceName)){
//				String tagVsValue = "host@" + vM.getHost();
//				Set<String> tagVsValueSet = new HashSet<String>();
//				tagVsValueSet.add(tagVsValue);
//				metricHostMap.put(loadMetric.getMetric() , tagVsValueSet);
//				metricNamesAndCummulativeFlags.put(loadMetric.getMetric() , loadMetric.getCummulative());
//			}
//		}
//	}
//
//	private Map<String, List<Integer>> updateServiceDetailsFromApp(Integer appId, String serviceName, List<Integer> versionIds){
//
//		Application application = appIdVsApp.get(appId);
//		ServiceVersionModelGenerator sVMG = new ServiceVersionModelGenerator();
//		Map<String, Set<String>> serviceVsServiceInstanceNames = sVMG.modelMapUtil(appId);
//		Integer vMId = null;
//
//		for(Entry<String, Set<String>> sVSINS : serviceVsServiceInstanceNames.entrySet()){
//			serviceName = sVSINS.getKey().split("#")[1];
//			vMId = Integer.parseInt(sVSINS.getKey().split("#")[0]);
//			break;
//		}
//
//		VirtualMachine vM = vMIdVsVM.get(vMId);
//		List<Integer> serviceIds = vM.getServiceIds();
//		List<Integer> serviceVersionIds = serviceIdVsService.get(serviceIds.get(0)).getVersionIds();
//		Integer	versionId1 = versionIds.get(0);
//		Integer	versionId2 = versionIds.get(1);
//		versionIds = new ArrayList<Integer>();
//
//		for(Integer serviceVersionId : serviceVersionIds){
//			if( (serviceVersionId.intValue() == versionId1.intValue()) || (serviceVersionId.intValue() == versionId2.intValue())){
//				versionIds.add(serviceVersionId);
//			}
//		}
//		Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
//		result.put(serviceName, versionIds);
//		return result;
//	}
//
//	@SuppressWarnings("unchecked")
//	private JSONArray createJsonObjectForBucketsAndUpdateTableJson(JSONObject comparedBuckets, Double minY, Double maxY, int count, String versionName) {
//		tableArray = new JSONArray();			// TODO  added just to make it work should not refresh and below version name condition should be enabled. 
//		JSONObject result = new JSONObject();
//		JSONArray errorArray = (JSONArray) comparedBuckets.get("errorStatus");
//		JSONObject firstError = (JSONObject) errorArray.get(0);
//		JSONArray resultBucketArray = new JSONArray();
//
//		if(StringUtils.equalsIgnoreCase(firstError.get("errorString").toString(), "successful")){
//			List<JSONObject> bucketsArray = (List<JSONObject>) comparedBuckets.get("buckets");
//
//			for(JSONObject bucket : bucketsArray){
//				if(Boolean.parseBoolean(bucket.get("versionDifferent").toString())){
//					JSONObject resultBucket = new JSONObject();
//					String minXDashMaxX = bucket.get("loadRange").toString();
//					Integer minX = Integer.parseInt(minXDashMaxX.split("-")[0]);
//					Integer maxX = Integer.parseInt(minXDashMaxX.split("-")[1]);
//					List<JSONObject> statArray = (List<JSONObject>) bucket.get("stats");
//					for(JSONObject stat : statArray){
//						JSONObject tableObject = new JSONObject();
//						tableObject.put("bucketID", bucket.get("bucketID"));
//						//if(StringUtils.equalsIgnoreCase(stat.get("version").toString(), versionName )){
//						tableObject.put("version",stat.get("version"));
//						tableObject.put("average",stat.get("average"));
//						tableObject.put("std_dev",stat.get("std_dev"));
//						tableObject.put("median",stat.get("median"));
//						tableObject.put("variance",stat.get("variance"));
//						tableArray.add(tableObject);
//						//}
//					}
//					resultBucket.put("a1",maxX );
//					resultBucket.put("b1", minY);
//					resultBucket.put("a2", maxX);
//					resultBucket.put("b2", maxY);
//					resultBucketArray.add(resultBucket);
//				}
//			}
//		}
//		else{
//			JSONObject error = new JSONObject();
//			error.put("error", firstError.get("errorString").toString());
//			tableArray.add(error);
//		}
//		return resultBucketArray;
//	}
//
//	public static void main(String args[]){
//		Integer appId = 23;
//		SVCUIService service = new SVCUIService();
//		boolean serviceVersion = false ; 
//		//				JSONObject result = service.getVersionDetails(appId, serviceVersion);
//		String comparatorOutput = "{\"bucketComparison\":{\"processID\":\"\",\"appID\":\"\",\"serviceID\":\"\",\"versionID\":[{\"version1\":\"\",\"version2\":\"\"}],\"errorStatus\":[{\"error\":\"0\",\"errorString\":\"successful\"}],\"buckets\":[{\"bucketID\":\"bucket1\",\"loadRange\":\"0-500\",\"stats\":[{\"version\":\"version1\",\"average\":34511,\"std_dev\":58017.3,\"median\":8336,\"variance\":3366007140},{\"version\":\"version2\",\"average\":5992.6665,\"std_dev\":7253.811,\"median\":2324,\"variance\":5.2617776E7}],\"versionDifferent\":\"TRUE\",\"comparisonResult\":[{\"colorCode\":2,\"betterVersion\":\"version2\"}]},{\"bucketID\":\"bucket2\",\"loadRange\":\"500-1000\",\"stats\":[{\"version\":\"version1\",\"average\":41356,\"std_dev\":14366.995,\"median\":41356,\"variance\":206410562},{\"version\":\"version2\",\"average\":41356,\"std_dev\":14366.995,\"median\":41356,\"variance\":206410562}],\"versionDifferent\":\"FALSE\",\"comparisonResult\":[{\"colorCode\":0,\"betterVersion\":\"none\"}]},{\"bucketID\":\"bucket3\",\"loadRange\":\"1000-1500\",\"stats\":[{\"version\":\"version1\",\"average\":328984.56,\"std_dev\":74418.1,\"median\":302928.5,\"variance\":5.5380541E9},{\"version\":\"version2\",\"average\":99735.5,\"std_dev\":17142.39,\"median\":99735.5,\"variance\":2.93861536E8}],\"versionDifferent\":\"TRUE\",\"comparisonResult\":[{\"colorCode\":2,\"betterVersion\":\"version2\"}]},{\"bucketID\":\"bucket4\",\"loadRange\":\"1500-2000\",\"stats\":[{\"version\":\"version1\",\"average\":143111.88,\"std_dev\":12461.86,\"median\":141792,\"variance\":1.55297952E8},{\"version\":\"version2\",\"average\":100081.49,\"std_dev\":49257.527,\"median\":130892.5,\"variance\":2.426304E9}],\"versionDifferent\":\"TRUE\",\"comparisonResult\":[{\"colorCode\":2,\"betterVersion\":\"version2\"}]}]}}";
//		String serviceName = null;
//		Integer versionId1= 1;
//		Integer versionId2 = 2;
//		List<Integer> versionIds = new ArrayList<>();
//		versionIds.add(versionId1);
//		versionIds.add(versionId2);
//		List<Integer> serviceVersionIds = new ArrayList<Integer>();
//		serviceVersionIds.add(1);
//		serviceVersionIds.add(2);
//		Boolean serviceComparator =false;
//		JSONObject result = service.getVersionData(appId, 
//				serviceName, versionIds, serviceVersionIds, serviceComparator );
//		System.out.println("result : " + result);
//	}
//
}

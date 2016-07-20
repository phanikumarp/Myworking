package com.n42.analytics.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import n42.domain.model.Action;
import n42.domain.model.Canary;
import n42.domain.model.CasServiceMetricDetails;
import n42.domain.model.CasServiceMetrics;
import n42.domain.model.Cluster;
import n42.domain.model.Health;
import n42.domain.model.OwnerWatcherData;
import n42.domain.model.Status;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.n42.analytics.exceptions.ClusterMissingException;
import com.n42.analytics.exceptions.NullInputFoundException;
import com.n42.analytics.exceptions.StatusException;
import com.n42.analytics.hibernate.dao.CanaryAnalysisDao;
import com.n42.analytics.hibernate.dao.CanaryAnalysisDaoImpl;
import com.n42.analytics.hibernate.dao.CanaryDao;
import com.n42.analytics.hibernate.dao.CanaryDaoImpl;
import com.n42.analytics.hibernate.dao.CasServiceMetricDetailsDao;
import com.n42.analytics.hibernate.dao.CasServiceMetricDetailsDaoImpl;
import com.n42.analytics.hibernate.dao.CasServiceMetricsDao;
import com.n42.analytics.hibernate.dao.CasServiceMetricsDaoImpl;
import com.n42.analytics.hibernate.dao.ClusterDao;
import com.n42.analytics.hibernate.dao.ClusterDaoImpl;
import com.n42.analytics.hibernate.dao.OwnerWatcherDataDao;
import com.n42.analytics.hibernate.dao.OwnerWatcherDataDaoImpl;

public class RegisterCanary {

	@SuppressWarnings("unchecked")
	public Integer registerCanary(Map<String,Object> sDEJSON){
		try {
			Canary canary = new Canary();
			CanaryDao cRDao = new CanaryDaoImpl();
			if(sDEJSON != null){
				if(sDEJSON.get("application") != null){
					String application = sDEJSON.get("application").toString();
					canary.setApplication(application);
				}
				if(sDEJSON.get("launchedDate") != null){
					String launchedDate = sDEJSON.get("launchedDate").toString();
					canary.setLaunchedDate(launchedDate);
				}else{
					canary.setLaunchedDate(new Date().toString());
				}
				//TODO  need to change status object in the else part
				Status status = new Status();
				if( sDEJSON.get("status") != null){
					Map<String,Object> StatusObject = new LinkedHashMap<String,Object>();
					StatusObject =  (Map<String,Object>) sDEJSON.get("status");
					if(StatusObject.get("status") != null){
						status.setStatus(StatusObject.get("status").toString());
					}
					if(StatusObject.get("complete") != null){
						status.setComplete((boolean)StatusObject.get("complete"));
					}
				}else{
					status.setStatus("RUNNING");
					status.setComplete(false);
				}
				canary.setStatus(status);
				Health health = new Health();
				if(sDEJSON.get("health") != null){
					JSONObject healthObj = new JSONObject();
					healthObj = (JSONObject) sDEJSON.get("health");
					if(healthObj.get("health")!=null){
						health.setHealth(sDEJSON.get("health").toString());
					}
					else{
						health.setHealth("HEALTHY");
					}
					if(healthObj.get("meassage")!=null){
						health.setMessage(sDEJSON.get("message").toString());
					}
					else{
						health.setMessage("Canary Is HEALTHY");
					}
					canary.setHealth(health);

				}
				else{
					health.setHealth("HEALTHY");
					health.setMessage("Canary Is HEALTHY");
					canary.setHealth(health);
				}
				Map<String,Object> canaryConfigObject = new LinkedHashMap<String,Object>();
				if(sDEJSON.get("canaryConfig") != null){
					canaryConfigObject.putAll((Map<String,Object>) sDEJSON.get("canaryConfig"));
					//canaryConfigObject =  (JSONObject) sDEJSON.get("canaryConfig");
				}
				//canary.setId(canaryConfigObject.get("id").toString());
				if(canaryConfigObject.get("name") != null){
					String name = canaryConfigObject.get("name").toString();
					canary.setCanaryName(name);
				}
				if(canaryConfigObject.get( "actionsForUnhealthyCanary" ) != null 
						&& !((JSONArray)canaryConfigObject.get( "actionsForUnhealthyCanary" )).isEmpty()){
					List<Action> actions = new ArrayList<Action>();
					JSONArray actionArray =(JSONArray)canaryConfigObject.get( "actionsForUnhealthyCanary" );
					for(int i=0 ; i < actionArray.size() ; i ++ ){
						JSONObject actionObj = (JSONObject) actionArray.get(i);
						Action action = new Action();
						if(actionObj.get("action") != null ){
							action.setAction(actionObj.get("action").toString()); 
						}
						if(actionObj.get("delayBeforeActionInMins") !=  null) {
							Object delayBeforeActionInMins = actionObj.get("delayBeforeActionInMins");
							if(delayBeforeActionInMins instanceof String) {
								action.setDelayBeforeActionInMins(NumberUtils.toInt((String)delayBeforeActionInMins));
							}
							else if(delayBeforeActionInMins instanceof Number) {
								action.setDelayBeforeActionInMins(((Number) delayBeforeActionInMins).intValue());
							}
						}
						actions.add(action); 
					}
					canary.setActionsForUnhealthyCanary(actions);
				}	
				int lifetimeMinutes = 0;
				if(canaryConfigObject.get("lifetimeMinutes") != null){
					Object lifetimeMinutesObj = canaryConfigObject.get("lifetimeMinutes");
					if(lifetimeMinutesObj instanceof String) {
						lifetimeMinutes = NumberUtils.toInt((String)lifetimeMinutesObj);
					}
					else if(lifetimeMinutesObj instanceof Number) {
						lifetimeMinutes = ((Number) lifetimeMinutesObj).intValue();
					}
				}
				if(canaryConfigObject.get("lifetimeHours") != null){
					Object lifetimeHours = canaryConfigObject.get("lifetimeHours");
					if(lifetimeHours instanceof String) {
						Double d = Double.parseDouble(lifetimeHours.toString());
						d = d * 60;
						lifetimeMinutes = d.intValue();
					}
					else if(lifetimeHours instanceof Number) {
						lifetimeMinutes = ((Number) lifetimeHours).intValue()*60;
					}
				}
				canary.setLifetimeMinutes(lifetimeMinutes);
				if(canaryConfigObject.get("combinedCanaryResultStrategy") != null){
					String combinedCanaryResultStrategy = canaryConfigObject.get("combinedCanaryResultStrategy").toString();
					canary.setCombinedCanaryResultStrategy(combinedCanaryResultStrategy);
				}

				Map<String,Object> canaryAnalysisObject = new LinkedHashMap<String,Object>();
				if(canaryConfigObject.get("canaryAnalysisConfig") != null){
					canaryAnalysisObject.putAll((Map<String,Object>) canaryConfigObject.get("canaryAnalysisConfig"));
					//canaryAnalysisObject =  (JSONObject) canaryConfigObject.get("canaryAnalysisConfig");
				}
				if(canaryAnalysisObject.get("name")!=null){
					String name = canaryAnalysisObject.get("name").toString();
					canary.setAnalysisName(name);
				}
				int beginCanaryAnalysisAfterMins = 0 ; 
				if(canaryAnalysisObject.get("beginCanaryAnalysisAfterMins")!=null){
					Object beginCanaryAnalysisAfterMinsObj = canaryAnalysisObject.get("beginCanaryAnalysisAfterMins");
					if(beginCanaryAnalysisAfterMinsObj instanceof String) {
						beginCanaryAnalysisAfterMins = NumberUtils.toInt((String)beginCanaryAnalysisAfterMinsObj);
					}
					else if(beginCanaryAnalysisAfterMinsObj instanceof Number) {
						beginCanaryAnalysisAfterMins = ((Number) beginCanaryAnalysisAfterMinsObj).intValue();
					}
					canary.setBeginCanaryAnalysisAfterMins(beginCanaryAnalysisAfterMins);
				}
				List<Integer> notificationHours = new ArrayList<Integer>();
				if( canaryAnalysisObject.get("notificationHours") != null){
					Scanner scanner = new Scanner(canaryAnalysisObject.get("notificationHours").toString());
					while(scanner.hasNext()){
						if (scanner.hasNextInt()) {
							notificationHours.add(scanner.nextInt());
						}
						scanner.next();
					}
					canary.setNotificationHours( convertJSONArrayToIntegerList(notificationHours) );
				}
				int canaryAnalysisIntervalMins = 0;
				if(canaryAnalysisObject.get("canaryAnalysisIntervalMins") != null){
					Object canaryAnalysisIntervalMinsObj = canaryAnalysisObject.get("canaryAnalysisIntervalMins");
					if(canaryAnalysisIntervalMinsObj instanceof String) {
						canaryAnalysisIntervalMins = NumberUtils.toInt((String)canaryAnalysisIntervalMinsObj);
					}
					else if(canaryAnalysisIntervalMinsObj instanceof Number) {
						canaryAnalysisIntervalMins = ((Number) canaryAnalysisIntervalMinsObj).intValue();
					}
					canary.setCanaryAnalysisIntervalMins(canaryAnalysisIntervalMins);
				}
				int lookbackMins = 0;
				if(canaryAnalysisObject.get("lookbackMins") != null){
					Object lookbackMinsObj = canaryAnalysisObject.get("lookbackMins");
					if(lookbackMinsObj instanceof String) {
						lookbackMins = NumberUtils.toInt((String)lookbackMinsObj);
					}
					else if(lookbackMinsObj instanceof Number) {
						lookbackMins = ((Number) lookbackMinsObj).intValue();
					}
					canary.setLookbackMins(lookbackMins);
				}
				if(canaryAnalysisObject.get("params") != null){
					canary.setParams(canaryAnalysisObject.get("params").toString());
				}

				Map<String,Object> canarySuccessObject = new LinkedHashMap<String,Object>();
				if(canaryConfigObject.get("canarySuccessCriteria") != null){
					canarySuccessObject.putAll((Map<String,Object>) canaryConfigObject.get("canarySuccessCriteria"));
					//canarySuccessObject =  (JSONObject) canaryConfigObject.get("canarySuccessCriteria");
				}
				if(canarySuccessObject.get("canaryResultScore") != null){
					String canaryResultScore = (String) (canarySuccessObject.get("canaryResultScore"));
					canary.setCanaryResultScore(canaryResultScore);
				}
				Map<String,Object> canaryHealthCheckHandlerObject = new LinkedHashMap<String,Object>();
				if(canaryConfigObject.get("canaryHealthCheckHandler") != null){
					canaryHealthCheckHandlerObject.putAll((Map<String,Object>) canaryConfigObject.get("canaryHealthCheckHandler"));
					//			canaryHealthCheckHandlerObject =  (JSONObject) canaryConfigObject.get("canaryHealthCheckHandler");
				}
				if(canaryHealthCheckHandlerObject.get("@class") != null){
					canary.setcHCHClass(canaryHealthCheckHandlerObject.get("@class").toString());
				}
				if(canaryHealthCheckHandlerObject.get("minimumCanaryResultScore") != null){
					String minimumCanaryResultScore =   (String) (canaryHealthCheckHandlerObject.get("minimumCanaryResultScore"));
					canary.setMinimumCanaryResultScore(minimumCanaryResultScore);
				}

				Cluster cluster = new Cluster();
				ClusterDao clusterDao = new ClusterDaoImpl();
				List<Object> canaryDeploymentsObject = new ArrayList<>();
				if(sDEJSON.get("canaryDeployments") != null){
					canaryDeploymentsObject =  (List<Object>) sDEJSON.get("canaryDeployments");
					Map<String,Object> canaryClusterObject = new LinkedHashMap<String,Object>();
					if(((Map<String,Object>)canaryDeploymentsObject.get(0)).get("canaryCluster")!=null){
						canaryClusterObject =   (Map<String, Object>) ((Map<String,Object>) canaryDeploymentsObject.get(0)).get("canaryCluster");
					}
					if(canaryClusterObject.get("name") != null){
						cluster.setName(canaryClusterObject.get("name").toString());
					}
					if(canaryClusterObject.get("type") != null){
						cluster.setClusterType(canaryClusterObject.get("type").toString());
					}
					if(canaryClusterObject.get("accountName") != null){
						cluster.setAccountName(canaryClusterObject.get("accountName").toString());
					}
					if(canaryClusterObject.get("region") != null){
						cluster.setRegion(canaryClusterObject.get("region").toString());
					}
					if(canaryClusterObject.get("buildId") != null){
						cluster.setBuildId(canaryClusterObject.get("buildId").toString());
					}
					if(canaryClusterObject.get("imageId") != null){
						cluster.setImageId(canaryClusterObject.get("imageId").toString());
					}
					cluster.setIsCanaryCluster(true);
					if( cluster != null ){
						clusterDao.saveCluster(cluster);
					}
					if(cluster.getN42Id() != null){
						canary.setCanaryClusterId(cluster.getN42Id().intValue());
					}

					cluster = new Cluster();
					Map<String,Object> baselineClusterObject = new LinkedHashMap<String,Object>();
					if(((Map<String,Object>) canaryDeploymentsObject.get(0)).get("baselineCluster")!=null){
						baselineClusterObject =  (Map<String,Object>) ((Map<String,Object>) canaryDeploymentsObject.get(0)).get("baselineCluster");
					}
					if(baselineClusterObject.get("name") != null){
						cluster.setName(baselineClusterObject.get("name").toString());
					}
					if(baselineClusterObject.get("type") != null){
						cluster.setClusterType(baselineClusterObject.get("type").toString());
					}
					if(baselineClusterObject.get("accountName") != null){
						cluster.setAccountName(baselineClusterObject.get("accountName").toString());
					}
					if(baselineClusterObject.get("region") != null){
						cluster.setRegion(baselineClusterObject.get("region").toString());
					}
					if(baselineClusterObject.get("buildId") != null){
						cluster.setBuildId(baselineClusterObject.get("buildId").toString());
					}
					if(baselineClusterObject.get("imageId") != null){
						cluster.setImageId(baselineClusterObject.get("imageId").toString());
					}
					cluster.setIsCanaryCluster(false);
					if(cluster != null){
						clusterDao.saveCluster(cluster);
					}
					if(cluster.getN42Id() != null){
						canary.setBaselineClusterId(cluster.getN42Id().intValue());
					}
				}

				if( sDEJSON.get("owner") != null){
					canary.setOwner(sDEJSON.get("owner").toString());
				}

				OwnerWatcherData oWD = new OwnerWatcherData();
				OwnerWatcherDataDao oWDDao = new OwnerWatcherDataDaoImpl();

				List<Map<String,Object>> watchersObject = new ArrayList<Map<String,Object>>();
				if(sDEJSON.get("watchers") != null){
					watchersObject =  (List<Map<String,Object>>) sDEJSON.get("watchers");
				}
				List<Integer> watcherIds = new ArrayList<Integer>();
				for(Map<String,Object> watcherObject : watchersObject){
					oWD = new OwnerWatcherData();
					if(watcherObject.get("name") != null){
						oWD.setName(watcherObject.get("name").toString());
					}
					if(watcherObject.get("email") != null){
						oWD.setEmail(watcherObject.get("email").toString());
					}
					oWDDao.saveOwnerWatcherData(oWD);
					if(oWD.getN42Id() != null){
						watcherIds.add(oWD.getN42Id().intValue());
					}
				}
				canary.setWatcherIds(watcherIds);
				cRDao.saveCanary(canary);

				//Starting a thread to Trigger CAS at different intervals. 
				CASThreadTrigger triggerThread=new CASThreadTrigger(beginCanaryAnalysisAfterMins, sDEJSON, lifetimeMinutes, notificationHours, canaryAnalysisIntervalMins, canary.getN42Id(), lookbackMins);  
				Thread trigger =new Thread(triggerThread);  
				trigger.start();  

				System.out.println( "Result : " + canary.getN42Id() );
				JSONObject response = new JSONObject();
				if(canary.getN42Id() != null){
						/*String command = "nohup /root/apache-jmeter-3.0/bin/jmeter.sh -n -t /root/apache-jmeter-3.0/serverGroup.jmx > servergroup.log 2>&1 &";
						String output = executeCommand(command);
						System.out.println("output to execute jmeter:::"+output);*/
					response.put("status", "Successfully registered Canary with Id:"+canary.getN42Id());
				}else{
					response.put("status", "Canary not registered");
				}
				return canary.getN42Id();
			}
			else{
				throw new NullInputFoundException("Null Json Found As Register Canary Input");
			}
		} catch (NullInputFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
	private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                           new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}

	@SuppressWarnings("unchecked")
	private JSONObject parseJsonForRAlgos(Map<String, Object> sDEJSON, Canary canary) throws IOException {

		String baselineCanaryIP = null;
		String currentlineCanaryIP = null;


		JSONObject inputJsonForCanaryAnalysis = new JSONObject();

		try {
			JSONArray clusterInfo = (JSONArray) sDEJSON.get("canaryDeployments");
			if(clusterInfo.get(0) == null){
				throw new ClusterMissingException("Canary Deployment Cluster is Null");
			}
			JSONObject versionClusterData = (JSONObject) clusterInfo.get(0);
			JSONObject baseClusterObj = (JSONObject) versionClusterData.get("baselineCluster");
			JSONObject currentClusterObj = (JSONObject) versionClusterData.get("canaryCluster");
			JSONObject canaryConfigObj = (JSONObject) sDEJSON.get("canaryConfig");

			Process shell = Runtime.getRuntime().exec(new String[] {"python","/opt/AWSmappingFile/mapping_privateip_sg.py",baseClusterObj.get("serverGroup").toString()});
			BufferedReader reader = new BufferedReader(new InputStreamReader(shell.getInputStream())); 
			String baseIp = null;
			while ((baseIp = reader.readLine()) != null) {
				System.out.println("baselineCanaryIP:::::::::"+baseIp);
				baselineCanaryIP = baseIp;
			}

			Process shell1 = Runtime.getRuntime().exec(new String[] {"python","/opt/AWSmappingFile/mapping_privateip_sg.py",currentClusterObj.get("serverGroup").toString()});
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(shell1.getInputStream())); 
			String currentIp = null;
			while ((currentIp = reader1.readLine()) != null) {
				System.out.println("currentlineCanaryIP:::::::::"+currentIp);
				currentlineCanaryIP = currentIp;
			}
			
			canary.setBaselineClusterIp(baselineCanaryIP);
			canary.setCanaryClusterIp(currentlineCanaryIP);
			CanaryDao  canaryDao = new CanaryDaoImpl() ;
			canaryDao.updateCanary(canary);
			
			JSONObject applicationData = new JSONObject();
			JSONObject applicationVersionData1 = new JSONObject();
			JSONObject applicationVersionData2 = new JSONObject();

			JSONObject metricsData = new JSONObject();
			JSONObject tagsData = new JSONObject();
			JSONObject aggregatorData = new JSONObject();
			JSONArray metricsDatalist = new JSONArray();
			JSONArray tagsDatalist = new JSONArray();

			JSONObject metricsData1 = new JSONObject();
			JSONObject tagsData1 = new JSONObject();
			JSONObject aggregatorData1 = new JSONObject();
			JSONArray metricsDatalist1 = new JSONArray();
			JSONArray tagsDatalist1 = new JSONArray();

			//String serviceName = sDEJSON.get("application").toString();
			String serviceName = "apache";
			CasServiceMetricsDao casSMetricsDao = new CasServiceMetricsDaoImpl();
			CasServiceMetricDetailsDao casSMDetailsDao = new CasServiceMetricDetailsDaoImpl();
			CasServiceMetrics  casMertics = casSMetricsDao.getCasServiceMetricsByServiceName(serviceName);
			List<CasServiceMetricDetails>  casMerticDetails = casSMDetailsDao.getAllCasServiceMetricDetails();

			for(CasServiceMetricDetails casMetric :casMerticDetails){

				tagsDatalist = new JSONArray();
				metricsData = new JSONObject();
				tagsData = new JSONObject();
				aggregatorData = new JSONObject();

				metricsData.put("metricType", casMetric.getMetricType());
				metricsData.put("metricName", casMetric.getMetricName());
				tagsData.put("tagName", "host");
				tagsData.put("tagValue", baselineCanaryIP);
				//tagsData.put("tagValue", "currentlineCanaryIP");
				aggregatorData.put("Function", casMetric.getKairosAggregator());
				aggregatorData.put("time", "1");
				aggregatorData.put("unit", "seconds");
				tagsDatalist.add(tagsData);
				metricsData.put("tagsData", tagsDatalist);
				metricsData.put("aggregator", aggregatorData);

				metricsDatalist.add(metricsData);

			}

			applicationVersionData1.put("versionType", "base");
			applicationVersionData1.put("versionName", baseClusterObj.get("name"));
			//applicationVersionData1.put("versionName", "baselineCanary");                  //fake data
			applicationVersionData1.put("versionID", baseClusterObj.get("buildId"));
			//applicationVersionData1.put("versionID", "123456");                            //fake data
			applicationVersionData1.put("metrics", metricsDatalist);

			for(CasServiceMetricDetails casMetric1 :casMerticDetails){

				tagsDatalist1 = new JSONArray();
				metricsData1 = new JSONObject();
				tagsData1 = new JSONObject();
				aggregatorData1 = new JSONObject();

				metricsData1.put("metricType", casMetric1.getMetricType());
				metricsData1.put("metricName", casMetric1.getMetricName());
				tagsData1.put("tagName", "host");
				tagsData1.put("tagValue", currentlineCanaryIP);
				//tagsData1.put("tagValue", "baselineCanaryIP");
				aggregatorData1.put("Function", casMetric1.getKairosAggregator());
				aggregatorData1.put("time", "1");
				aggregatorData1.put("unit", "seconds");
				tagsDatalist1.add(tagsData1);
				metricsData1.put("tagsData", tagsDatalist1);
				metricsData1.put("aggregator", aggregatorData1);

				metricsDatalist1.add(metricsData1);
			}

			applicationVersionData2.put("versionType", "current");
			applicationVersionData2.put("versionName", currentClusterObj.get("name"));
			//applicationVersionData2.put("versionName", "currentlineCanary");                  //fake data
			applicationVersionData2.put("versionID", currentClusterObj.get("buildId"));
			//applicationVersionData2.put("versionID", "7891011");                              //fake data
			applicationVersionData2.put("metrics", metricsDatalist1);
			applicationData.put("applicationName", sDEJSON.get("application"));
			applicationData.put("canaryID", canaryConfigObj.get("id"));
			//		applicationData.put("canaryID", "canaryID159");
			applicationData.put("startTime", ""); 
			applicationData.put("endTime", "");
			applicationData.put("version1", applicationVersionData1);
			applicationData.put("version2", applicationVersionData2);

			inputJsonForCanaryAnalysis.put("application", applicationData);
			System.out.println("==================================="+inputJsonForCanaryAnalysis.toString());

			//		String input = "{\"application\": {\"applicationName\": \"edge\",\"canaryID\": \"abcdefgh\",\"startTime\": 1463391340000,\"endTime\": 1463394940000,\"version2\": {\"versionType\": \"current\",\"versionName\": \"edge-canary\",\"versionID\": \"edge-master-100\",\"metrics\": [{\"metricType\": \"load\",\"metricName\": \"apache.responsetime\",\"tagsData\": [{\"tagName\": \"host\",\"tagValue\": \"74567e90d673\"}],\"aggregator\": {\"Function\": \"count\",\"time\": \"1\",\"unit\": \"seconds\"}}, {\"metricType\": \"other\",\"metricName\": \"apache.requests\",\"tagsData\": [{\"tagName\": \"host\",\"tagValue\": \"74567e90d673\"}],\"aggregator\": {\"Function\": \"count\",\"time\": \"1\",\"unit\": \"seconds\"}}]},\"version1\": {\"versionType\": \"base\",\"versionName\": \"edge-baseline\",\"versionID\": \"edge-master-95\",\"metrics\": [{\"metricType\": \"load\",\"metricName\": \"apache.responsetime\",\"tagsData\": [{\"tagName\": \"host\",\"tagValue\": \"c4508b0c24df\"}],\"aggregator\": {\"Function\": \"count\",\"time\": \"1\",\"unit\": \"seconds\"}}, {\"metricType\": \"other\",\"metricName\": \"apache.requests\",\"tagsData\": [{\"tagName\": \"host\",\"tagValue\": \"c4508b0c24df\"}],\"aggregator\": {\"Function\": \"count\",\"time\": \"1\",\"unit\": \"seconds\"}}]}}}";
			//		JSONParser parser = new JSONParser();
			//		JSONObject inputToR = null;
			//		try {
			//			inputToR = (JSONObject) parser.parse(input);
			//		} catch (ParseException e) {
			//			// TODO Auto-generated catch block
			//			e.printStackTrace();
			//		}
			//		return inputToR;
		} catch (ClusterMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputJsonForCanaryAnalysis;

	}

	@SuppressWarnings("unchecked")
	public String getCanaryAsJSONObject(int id){

		CanaryDao canaryDao = new CanaryDaoImpl();
		Canary canary =  canaryDao.getCanaryById(id);
		JSONObject sDEJSON = new JSONObject();

		if(canary != null && canary.getN42Id() ==  id ){

			sDEJSON.put("id", canary.getN42Id().toString() );
			if(canary.getApplication()!=null){
				sDEJSON.put("application", canary.getApplication().toString());
			}else{
				sDEJSON.put("application", "application_123" );
			}
			Date d = new Date();
			if(canary.getLaunchedDate()!=null){
				sDEJSON.put("launchedDate", canary.getLaunchedDate().toString());
			}
			if(canary.getStatus() != null){
				JSONObject statusObj = new JSONObject();
				statusObj.put("status", canary.getStatus().getStatus());
				statusObj.put("complete", canary.getStatus().getComplete());
				sDEJSON.put("status", statusObj);
			}
			JSONObject healthObj = new JSONObject();
			if(canary.getHealth()!=null){
				healthObj.put("health", canary.getHealth().getHealth());
				healthObj.put("message", canary.getHealth().getMessage());
			}else{     //TODO  need to make dynamic health object
				healthObj.put("health", "HEALTHY");
				healthObj.put("message", "it is HEALTHY");
			}
			sDEJSON.put("health", healthObj);
			CanaryAnalysisDao canaryAnalysisDao = new CanaryAnalysisDaoImpl();
			Float canaryFinalScore = (Float)canaryAnalysisDao.getCanaryResultById(canary.getN42Id());
			//TODO  need to make dynamic canaryResult object
			JSONObject canaryResult = new JSONObject();
			/*JSONObject result = new JSONObject();
			result.put("result", "SUCCESS");*/
			canaryResult.put("overallResult", "SUCCESS");
			canaryResult.put("overallScore", canaryFinalScore);
			canaryResult.put("manual", false);
			canaryResult.put("message", "got success");
			canaryResult.put("lastUpdated",  canary.getN42LastUpdatedDate().toString());
			List<String> errors = new ArrayList<String>();
			errors.add("no errors");
			canaryResult.put("errors", errors);

			JSONObject canaryAnalysisResult = new JSONObject();
			canaryAnalysisResult.put("DEFAULT_SCORE", canaryFinalScore);
			canaryAnalysisResult.put("id", canary.getN42Id().toString());
			canaryAnalysisResult.put("canaryDeploymentId", "2");
			canaryAnalysisResult.put("score", canaryFinalScore);
			if(canaryFinalScore > 90){
				canaryAnalysisResult.put("result", "SUCCESS");
			}else{
				canaryAnalysisResult.put("result", "FAILURE");
			}
			JSONObject timeDuration = new JSONObject();
			canaryAnalysisResult.put("timeDuration", timeDuration);
			canaryAnalysisResult.put("lastUpdated", canary.getN42LastUpdatedDate().toString());
			canaryAnalysisResult.put("canaryReportURL", "https://test.com");
			Map<String,Object> additionalAttributes = new HashMap<String, Object>();
			canaryAnalysisResult.put("additionalAttributes", additionalAttributes);

			JSONArray canaryAnalysisResults = new JSONArray();
			canaryAnalysisResults.add(canaryAnalysisResult);
			canaryResult.put("lastCanaryAnalysisResults", canaryAnalysisResults);

			sDEJSON.put("canaryResult", canaryResult);

			JSONObject canaryConfigObject = new JSONObject();
			if(canary.getCanaryName()!=null){
				canaryConfigObject.put("name", canary.getCanaryName());
			}
			if(canary.getLifetimeMinutes()!=null){
				canaryConfigObject.put("lifetimeMinutes", canary.getLifetimeMinutes());
			}
			if(canary.getCombinedCanaryResultStrategy()!=null){
				canaryConfigObject.put("combinedCanaryResultStrategy", canary.getCombinedCanaryResultStrategy());
			}

			JSONObject canaryAnalysisObject = new JSONObject();

			if(canary.getAnalysisName()!=null){
				canaryAnalysisObject.put("name", canary.getAnalysisName());
			}
			if( canary.getNotificationHours() !=null){
				//					canaryAnalysisObject.put("notificationHours", (JSONArray)canary.getNotificationHours());
				//					canaryAnalysisObject.put("notificationHours", convertIntegerListToJsonArray(canary.getNotificationHours()));
			}
			if(canary.getCanaryAnalysisIntervalMins()!=null){
				canaryAnalysisObject.put("canaryAnalysisIntervalMins", canary.getCanaryAnalysisIntervalMins());
			}
			if(canary.getParams()!=null){
				canaryAnalysisObject.put("params", canary.getParams());
			}
			canaryConfigObject.put("canaryAnalysisConfig", canaryAnalysisObject);

			JSONObject canarySuccessObject = new JSONObject();
			if(canary.getCanaryResultScore()!=null){
				canarySuccessObject.put("canaryResultScore", canary.getCanaryResultScore());
			}
			canaryConfigObject.put("canarySuccessCriteria", canarySuccessObject);

			JSONObject canaryHealthCheckHandlerObject = new JSONObject();
			if(canary.getcHCHClass()!=null){
				canaryHealthCheckHandlerObject.put("@class", canary.getcHCHClass());
			}
			if(canary.getMinimumCanaryResultScore()!=null){
				canaryHealthCheckHandlerObject.put("minimumCanaryResultScore", canary.getMinimumCanaryResultScore());
			}
			canaryConfigObject.put("canaryHealthCheckHandler", canaryHealthCheckHandlerObject);
			sDEJSON.put("canaryConfig", canaryConfigObject );

			ClusterDao clusterDao = new ClusterDaoImpl();
			JSONArray canaryDeploymentsArray = new JSONArray();
			JSONObject canaryClusterObject = new JSONObject();

			if(canary.getCanaryClusterId() != null){
				Cluster cluster = clusterDao.getClusterById(canary.getCanaryClusterId());
				JSONObject clusterObject = fillClusterDetails(cluster);
				canaryClusterObject.put("canaryCluster", clusterObject);
				if(canary.getBaselineClusterId() != null){
					Cluster baseLineCluster = clusterDao.getClusterById(canary.getBaselineClusterId());
					JSONObject baseLineClusterObject = fillClusterDetails(baseLineCluster);
					canaryClusterObject.put("baselineCluster", baseLineClusterObject);
				}
				JSONObject healthObj1 = new JSONObject();
				if(canary.getHealth() != null){
					healthObj1.put("health", canary.getHealth().getHealth());
					healthObj1.put("message", canary.getHealth().getMessage());
				}else{
					healthObj1.put("health", "HEALTHY");
					healthObj1.put("message", "it is HEALTHY");
				}
				canaryClusterObject.put("health", healthObj1);

				JSONObject canaryResult1 = new JSONObject();
				long duration = canary.getN42CreatedDate().getTime() - canary.getN42LastUpdatedDate().getTime();
				timeDuration.put("durationString", String.valueOf(duration));
				canaryResult1.put("timeDuration", timeDuration);
				canaryResult1.put("lastUpdated", canary.getN42LastUpdatedDate().toString());
				canaryResult1.put("score", canaryFinalScore);
				canaryResult1.put("result", "SUCCESS");
				canaryClusterObject.put("canaryResult", canaryResult1);

				canaryDeploymentsArray.add(canaryClusterObject);

			}else{
				JSONObject region = new JSONObject();
				region.put("region", "us-east-1");
				canaryClusterObject.put("canaryCluster", region);

				JSONObject baseregion = new JSONObject();
				baseregion.put("region", "us-east-1");
				canaryClusterObject.put("baselineCluster", baseregion);

				JSONObject canaryResult1 = new JSONObject();
				timeDuration.put("durationString", "2");
				canaryResult1.put("timeDuration", timeDuration);
				canaryResult1.put("lastUpdated", canary.getN42LastUpdatedDate().toString());
				canaryResult1.put("score", canaryFinalScore);
				canaryResult1.put("result", "SUCCESS");

				JSONObject healthObj1 = new JSONObject();
				healthObj1.put("health", "HEALTHY");
				healthObj1.put("message", "it is HEALTHY");
				canaryClusterObject.put("health", healthObj1);
				canaryClusterObject.put("canaryResult", canaryResult1);
				canaryDeploymentsArray.add(canaryClusterObject);
			}
			sDEJSON.put("canaryDeployments", canaryDeploymentsArray );

			OwnerWatcherDataDao oWDDao = new OwnerWatcherDataDaoImpl();
			List<OwnerWatcherData> oWDList = oWDDao.getAllOwnerWatcherData();

			JSONArray watcherArray = new JSONArray();
			for(OwnerWatcherData oWD : oWDList){
				if(canary.getWatcherIds().contains(oWD.getN42Id().intValue())){
					JSONObject watchersObject =  new JSONObject();
					watchersObject = fillOwnerWatcherDetails(watchersObject, oWD);
					watcherArray.add(watchersObject);
				}
			}
			sDEJSON.put("watchers", watcherArray );
			sDEJSON.put("endDate", d.toString() );
			sDEJSON.put("owner", canary.getOwner() );

			System.out.println( "Result : " + sDEJSON.toJSONString() );
		}else{
			sDEJSON.put("status", "Canary does not exist with id:"+id);
		}
		return sDEJSON.toJSONString();
	}

	@SuppressWarnings("unchecked")
	private JSONObject fillOwnerWatcherDetails(JSONObject object, OwnerWatcherData oWD) {
		if(oWD.getName()!=null){
			object.put("name", oWD.getName());
		}
		if( oWD.getEmail()!=null){
			object.put("email", oWD.getEmail());
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	private JSONObject fillClusterDetails(Cluster cluster) {

		JSONObject clusterObject = new JSONObject();
		if(cluster.getName()!=null){
			clusterObject.put("name", cluster.getName());
		}
		if(cluster.getClusterType()!=null){
			clusterObject.put("type", cluster.getClusterType());
		}
		if( cluster.getAccountName()!=null){
			clusterObject.put("accountName", cluster.getAccountName());
		}
		if(cluster.getRegion()!=null){
			clusterObject.put("region", cluster.getRegion());
		}
		if( cluster.getBuildId()!=null){
			clusterObject.put("buildId", cluster.getBuildId());
		}
		if(cluster.getImageId()!=null){
			clusterObject.put("imageId", cluster.getImageId());
		}
		return clusterObject;
	}

	private List<Integer> convertJSONArrayToIntegerList(List<Integer> list) {
		List<Integer> castedList = new ArrayList<Integer>();

		for(int i =0 ; i < list.size();i++){
			String s = String.valueOf(list.get(i));
			System.out.println(list.toString());
			//int temp = (int) Integer.parseInt(list.get(i));
			castedList.add(Integer.parseInt(s));
		}
		return castedList;
	}

	class CASThreadTrigger implements Runnable{  

		private int beginCanaryAnalysisAfterMins;
		private Map<String,Object> sDEJSON;
		private List<Integer> notificationHours;
		private int canaryAnalysisIntervalMins;
		private int canaryId;
		private int lifetimeMinutes;
		private int lookbackMins;

		public CASThreadTrigger( int beginCanaryAnalysisAfterMins, Map<String,Object> sDEJSON, int lifetimeMinutes, List<Integer> notificationHours, int canaryAnalysisIntervalMins, Integer canaryId, Integer lookbackMins) {
			this.beginCanaryAnalysisAfterMins = beginCanaryAnalysisAfterMins;
			this.sDEJSON = sDEJSON;
			this.canaryAnalysisIntervalMins = canaryAnalysisIntervalMins;
			this.notificationHours = notificationHours ;
			this.canaryId = canaryId ;
			this.lifetimeMinutes = lifetimeMinutes;
			this.lookbackMins = lookbackMins;
		}

		public void run(){  
			System.out.println("thread is running...");  
			try {

				CanaryAnalysisDao canaryAnalysisDao = new CanaryAnalysisDaoImpl();
				CanaryDao  canaryDao = new CanaryDaoImpl() ;
				Canary canary =  canaryDao.getCanaryById(canaryId);


				float result = canaryAnalysisDao.getCanaryResultById(canaryId);


				if( canary != null && canary.getStatus()!= null){

					Status statusObj = canary.getStatus();
					if( StringUtils.equalsIgnoreCase( "RUNNING", statusObj.getStatus()) && BooleanUtils.isFalse( statusObj.getComplete())){
						System.out.println("Begining Canary");
						TimeUnit.MINUTES.sleep(beginCanaryAnalysisAfterMins);
						System.out.println("wait over");
						System.out.println("Start Canary");
						CASTrigger casTrigger = new CASTrigger();
						JSONObject temp = new JSONObject();
						JSONObject canaryConfigObj = (JSONObject) sDEJSON.get("canaryConfig");
						canaryConfigObj.put("id",canary.getN42Id().toString());
						sDEJSON.put("canaryConfig", canaryConfigObj);
						System.out.println(sDEJSON.toString());
						temp = parseJsonForRAlgos(sDEJSON, canary);
						System.out.println("begining R algo calculations");
						casTrigger.triggerCAS(temp, lifetimeMinutes, canaryAnalysisIntervalMins, "minutes", canary, lookbackMins);
						Status status = new Status();
						status.setStatus("COMPLETED");
						status.setComplete(true);
						canary.setStatus(status);
						canaryDao.updateCanary(canary);
					}
					else{
						throw new StatusException(" Status : " + statusObj.getStatus());
					}
				}
				else if(canary.getStatus() == null){
					throw new StatusException( " Status : null ");
				}
			} catch (InterruptedException | StatusException | IOException e) {

				e.printStackTrace();
			}
		}
	}

	public static void main(String Args[]){
		try {
			RegisterCanary registerCanary = new RegisterCanary();
			String sDEString = "{\n  \"canaryConfig\" : {\n    \"actionsForUnhealthyCanary\" : [ {\n      \"action\" : \"DISABLE\"\n    }, {\n      \"action\" : \"TERMINATE\",\n      \"delayBeforeActionInMins\" : \"10\"\n    } ],\n    \"canaryAnalysisConfig\" : {\n      \"beginCanaryAnalysisAfterMins\" : \"2\",\n      \"canaryAnalysisIntervalMins\" : \"5\",\n      \"lookbackMins\" : 30,\n      \"name\" : \"appgitcanaryconfig1\",\n      \"notificationHours\" : [ 1 ],\n      \"useLookback\" : true\n    },\n    \"canaryHealthCheckHandler\" : {\n      \"minimumCanaryResultScore\" : \"40\",\n      \"@class\" : \"com.netflix.spinnaker.mine.CanaryResultHealthCheckHandler\"\n    },\n    \"canarySuccessCriteria\" : {\n      \"canaryResultScore\" : \"90\"\n    },\n    \"combinedCanaryResultStrategy\" : \"LOWEST\",\n    \"lifetimeHours\" : \".5\",\n    \"name\" : \"canary - Canary\",\n    \"application\" : \"appgit\"\n  },\n \"owner\" : \"d.basivireddy@gmai.com\",\n  \"watchers\" : [ ],\n  \"canaryDeployments\" : [ {\n    \"canaryStage\" : \"dd5b5775-b690-49b9-af16-6a35c7c9c166\",\n    \"baselineCluster\" : {\n      \"name\" : \"appgit--baseline\",\n      \"serverGroup\" : \"appgit--baseline-v000\",\n      \"type\" : \"aws\",\n      \"accountName\" : \"phanip\",\n\"region\" : \"us-west-2\",\n      \"imageId\" : \"ami-63d51103\",\n      \"buildId\" : null\n    },\n    \"canaryCluster\" : {\n      \"name\" : \"appgit--canary\",\n      \"serverGroup\" : \"appgit--canary-v000\",\n      \"type\" : \"aws\",\n      \"accountName\" : \"phanip\",\n      \"region\" : \"us-west-2\",\n      \"imageId\" : \"ami-6bca0e0b\",\n      \"buildId\" : \"http://172.9.239.142:8080/job/Hello%20Poll/81/\"\n    }\n  } ],\n  \"application\" : \"appgit\"\n}\n";
			JSONParser parser = new JSONParser();
			JSONObject sDEJSON = null;
			sDEJSON = (JSONObject) parser.parse(sDEString);
			//			registerCanary.getCanaryAsJSONObject(34);
			registerCanary.registerCanary(sDEJSON);




			//		int id = 2;
			//		String reason = "Madhu's decision";
			//		CanaryDao canaryDao = new CanaryDaoImpl();
			//		canaryDao.updateCanaryReason( id, reason );
			//		canaryDao.deleteCanary( id );
			//
			//		int id =3;
			//		RegisterCanary registerCanary = new RegisterCanary();
			//		registerCanary.getCanaryAsJSONObject(id);

		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
	}



}

package com.n42.analytics.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import n42.domain.model.Canary;
import n42.domain.model.CanaryAnalysis;
import n42.domain.model.CasServiceMetricDetails;
import n42.domain.model.Cluster;
import n42.domain.model.MetricScore;
import n42.domain.model.OwnerWatcherData;
import n42.domain.model.VersionStats;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.n42.analytics.hibernate.dao.CanaryAnalysisDao;
import com.n42.analytics.hibernate.dao.CanaryAnalysisDaoImpl;
import com.n42.analytics.hibernate.dao.CanaryDao;
import com.n42.analytics.hibernate.dao.CanaryDaoImpl;
import com.n42.analytics.hibernate.dao.CasServiceMetricDetailsDao;
import com.n42.analytics.hibernate.dao.CasServiceMetricDetailsDaoImpl;
import com.n42.analytics.hibernate.dao.ClusterDao;
import com.n42.analytics.hibernate.dao.ClusterDaoImpl;
import com.n42.analytics.hibernate.dao.MetricScoreDao;
import com.n42.analytics.hibernate.dao.MetricScoreDaoImpl;
import com.n42.analytics.hibernate.dao.OwnerWatcherDataDao;
import com.n42.analytics.hibernate.dao.OwnerWatcherDataDaoImpl;
import com.n42.analytics.hibernate.dao.VersionStatsDao;
import com.n42.analytics.hibernate.dao.VersionStatsDaoImpl;
import com.n42.analytics.http.HttpUtils;
import com.n42.analytics.util.JSONUtils;

public class CanaryAnalysisService {

	//TODO full result model saver need to use in future or in SVC. Uncomment the tables as well. 
	public void saveCanaryAnalysisModel(String comparisonData) {
		//		CanaryAnalysisDao canaryAnalysisDao = new CanaryAnalysisDaoImpl();
		//		BucketScoreDao bucketScoreDao = new BucketScoreDaoImpl();
		//		MetricScoreDao metricScoreDao = new MetricScoreDaoImpl();
		//		JSONObject comparisonDataObject = JSONUtils.parseJsonFromInputString(comparisonData);
		//		JSONObject comparison = (JSONObject) comparisonDataObject.get("Comparison");
		//		/*preparing Canary Analysis object */
		//		CanaryAnalysis canaryAnalysis = new CanaryAnalysis(); 
		//		canaryAnalysis.setCanaryId((Integer) comparison.get("processID"));
		//		JSONArray versions = (JSONArray) comparison.get("versionID");
		//		JSONObject versionObject = (JSONObject) versions.get(0);
		//		int baseVersion = Integer.parseInt((String)versionObject.get("version1"));
		//		int compareVersion = Integer.parseInt((String)versionObject.get("version2"));
		//		canaryAnalysis.setBaseVersionId(baseVersion);
		//		canaryAnalysis.setCompareVersionId(compareVersion);
		//
		//		CanaryAnalysis existingCanaryAnalysis = ModelUtils.getCanaryBasedOnBaseVersionAndcompareVersion(baseVersion, compareVersion);
		//		if(existingCanaryAnalysis.getBaseVersionId() != null && existingCanaryAnalysis.getCompareVersionId() != null){
		//			List<Integer> metricScoreIds = existingCanaryAnalysis.getMetricScoreIds();
		//			for(Integer mId : metricScoreIds){
		//				MetricScore mScore = metricScoreDao.getMetricScoreById(mId);
		//				List<Integer> bucketScoreIds = mScore.getBucketScoreIds();
		//				for(Integer bId : bucketScoreIds){
		//					bucketScoreDao.deleteBucketScore(bId);
		//				}
		//				metricScoreDao.deleteMetricScore(mId);
		//			}
		//			canaryAnalysisDao.deleteCanaryAlaysis(existingCanaryAnalysis.getN42Id());
		//		}
		//		JSONArray comparisonResult = (JSONArray) comparison.get("ComparisonResult");
		//		JSONObject result = (JSONObject) comparisonResult.get(0);
		//		canaryAnalysis.setFinalScore(Float.valueOf((String)result.get("overallScore")));
		//
		//		List<Integer> metricScoreIds = new ArrayList<Integer>();
		//		JSONArray metrics = (JSONArray) comparison.get("metrics");
		//		for (int i = 0; i < metrics.size(); i++) {
		//			JSONObject metric = (JSONObject) metrics.get(i);
		//			/*preparing Metric Score object */
		//			MetricScore metricScore =new MetricScore();
		//			metricScore.setMetricId((String)metric.get("metricId"));
		//			JSONArray metricComparisonResult = (JSONArray) metric.get("metricComparisonResult");
		//			JSONObject metricResult = (JSONObject) metricComparisonResult.get(0);
		//			metricScore.setScore(Float.valueOf((String) metricResult.get("metricScore")));
		//
		//			List<Integer> bucketScoreIds = new ArrayList<Integer>();
		//			JSONArray buckets = (JSONArray) metric.get("buckets");
		//			for (int j = 0; j < buckets.size(); j++) {
		//				JSONObject bucket = (JSONObject) buckets.get(j);
		//				/*preparing Bucket Score object */
		//				BucketScore bucketScore = new BucketScore();
		//				bucketScore.setRange((String)bucket.get("loadRange"));
		//				JSONArray bucketComparisonResult = (JSONArray) bucket.get("bucketComparisonResult");
		//				JSONObject bucketResult = (JSONObject) bucketComparisonResult.get(0);
		//				bucketScore.setScore(Float.valueOf((String) bucketResult.get("bucketScore")));
		//				// TODO need to override existing objects
		//				/*saving bucket score object*/
		//				bucketScoreDao.saveBucketScore(bucketScore);
		//
		//				bucketScoreIds.add(bucketScore.getN42Id());
		//			}
		//			metricScore.setBucketScoreIds(bucketScoreIds);
		//			// TODO need to override existing objects
		//			/*saving Metric score object*/
		//			metricScoreDao.saveMetricScore(metricScore);
		//			metricScoreIds.add(metricScore.getN42Id());
		//		}
		//		canaryAnalysis.setMetricScoreIds(metricScoreIds);
		//		// TODO need to override existing objects
		//		/*saving Canary Analysis object*/
		//		canaryAnalysisDao.saveCanaryAnalysis(canaryAnalysis);
	}

	public String saveCanaryAnalysisResult(String comparisonData) {
		CanaryAnalysisDao canaryAnalysisDao = new CanaryAnalysisDaoImpl();
		CanaryAnalysis canaryAnalysis = new CanaryAnalysis();
		String status = null;
		try{
			if(comparisonData != null){
				JSONParser jsonParser = new JSONParser();
				JSONObject comparisonDataObject;
				comparisonDataObject = (JSONObject)jsonParser.parse(comparisonData);
				if(comparisonDataObject.get("canary_output")!= null ) { 
					JSONObject canaryOutputObj = (JSONObject) comparisonDataObject.get("canary_output");
					if(canaryOutputObj.get("canaryId").toString() != null && canaryOutputObj.get("canaryScore") != null){
						canaryAnalysis.setCanaryId(Integer.parseInt(canaryOutputObj.get("canaryId").toString()));
						canaryAnalysis.setFinalScore(Float.parseFloat(canaryOutputObj.get("canaryScore").toString()));
						if( canaryOutputObj.get("results")!=null ){

							JSONArray metricResultArray = (JSONArray) canaryOutputObj.get("results") ;
							List<Integer> metricScoreIds = new ArrayList<Integer>();

							MetricScore metricScore = new MetricScore();

							VersionStatsDao vSDao = new VersionStatsDaoImpl();
							MetricScoreDao mSDao = new MetricScoreDaoImpl();

							for( int i=0 ; i < metricResultArray.size() ; i ++ ){
								JSONObject metricResultObj = (JSONObject) metricResultArray.get(i);

								if( metricResultObj.get("metricName")!=null ){

									metricScore.setMetricName(metricResultObj.get("metricName").toString());
								}
								if( metricResultObj.get("score") != null && !StringUtils.equalsIgnoreCase( metricResultObj.get("score").toString(),"NA")){
									metricScore.setScore(Float.parseFloat( metricResultObj.get("score").toString()));
								}

								if(metricResultObj.get("error") != null){
									metricScore.setError(metricResultObj.get("error").toString());
								}
								if(metricResultObj.get("metricType") != null){
									metricScore.setMetricType(metricResultObj.get("metricType").toString());
								}
								if(metricResultObj.get("stats") != null){
									VersionStats vS = new VersionStats();
									JSONObject statsObj = (JSONObject) metricResultObj.get("stats");
									if(statsObj.get("version1") != null){
										JSONObject version1Obj = (JSONObject) statsObj.get("version1");
										vS = fillPojoObjFromJsonObjForVersionStats(version1Obj);
										vSDao.saveVersionStats(vS);
										metricScore.setVersion1Stats(vS);
									}
									if(statsObj.get("version2") != null){
										JSONObject version2Obj = (JSONObject) statsObj.get("version2");
										vS = fillPojoObjFromJsonObjForVersionStats(version2Obj);;
										vSDao.saveVersionStats(vS);
										metricScore.setVersion2Stats(vS);
									}
								}
								mSDao.saveMetricScore(metricScore);
								metricScoreIds.add(metricScore.getN42Id());
							}
							canaryAnalysis.setMetricScoreIds(metricScoreIds);
						}
						int id = canaryAnalysisDao.saveCanaryAnalysis(canaryAnalysis).getN42Id();
						if(id > 0 ){
							status = "Saved successfully with ID:"+id;
						}else{
							status = "Not saved";
						}
					}
				}
			}
		}catch(ParseException e){
			status = "json ParseException";
			e.printStackTrace();
		}
		return status;
	}

	private VersionStats fillPojoObjFromJsonObjForVersionStats(JSONObject version1Obj) {
		VersionStats vS = new VersionStats();
		if( version1Obj.get("Min.") != null ){
			Object version1 = version1Obj.get("Min.");
			vS.setMin(convertToFloat(version1));
		}
		if( version1Obj.get("1st Qu.") != null ){
			Object version1 = version1Obj.get("1st Qu.");
			vS.setFirstQu(convertToFloat(version1));
		}
		if( version1Obj.get("Median") != null ){
			Object version1 = version1Obj.get("Median");
			vS.setMedian(convertToFloat(version1));
		}
		if( version1Obj.get("Mean") != null ){
			Object version1 = version1Obj.get("Mean");
			vS.setMean(convertToFloat(version1));
		}
		if( version1Obj.get("3rd Qu.") != null ){
			Object version1 = version1Obj.get("3rd Qu.");
			vS.setThirdQu(convertToFloat(version1));
		}
		if( version1Obj.get("Max.") != null ){
			Object version1 = version1Obj.get("Max.");
			vS.setMax(convertToFloat(version1));
		}
		return vS;

	}

	public float convertToFloat(Object version){
		float result = 0f;
		if(version instanceof String) {
			result = NumberUtils.toFloat((String)version);
		}
		else if(version instanceof Number) {
			result = ((Number) version).floatValue();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public JSONObject deleteCanary(int id, String reason) {
		CanaryDao canaryDao = new CanaryDaoImpl();
		canaryDao.updateCanaryReason( id, reason );
		canaryDao.deleteCanary( id );
		JSONObject response = new JSONObject();
		response.put("status", "Canary deleted");
		return response;
	}

	@SuppressWarnings("unchecked")
	public String getAllCanaries(){

		CanaryDao canaryDao = new CanaryDaoImpl();
		List<Canary> canaries = canaryDao.getAllCanary();
		JSONObject finalResult = new JSONObject();
		JSONArray canaryList = new JSONArray();

		for(Canary canary : canaries){
			JSONObject sDEJSON = new JSONObject();
			if(canary.getApplication()!=null){
				sDEJSON.put("application", canary.getApplication().toString());
			}
			if(canary.getLaunchedDate()!=null){
				sDEJSON.put("launchedDate", canary.getLaunchedDate().toString());
			}
			if(canary.getStatus()!=null){
				sDEJSON.put("status", canary.getStatus().toString());
			}
			if(canary.getHealth()!=null){
				sDEJSON.put("health", canary.getHealth().toString());
			}

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
			List<Cluster> clusters = clusterDao.getAllClusters();

			for(Cluster cluster : clusters){
				if(cluster.getN42Id().intValue() == canary.getCanaryClusterId().intValue()){
					JSONObject canaryClusterObject = new JSONObject();
					JSONObject clusterObject = fillClusterDetails(cluster);
					canaryClusterObject.put("canaryCluster", clusterObject);
					canaryDeploymentsArray.add(canaryClusterObject);
				}
				else if(cluster.getN42Id().intValue() == canary.getBaselineClusterId().intValue()){
					JSONObject baselineClusterObject = new JSONObject();
					JSONObject clusterObject = fillClusterDetails(cluster);
					baselineClusterObject.put("baselineCluster", clusterObject);
					canaryDeploymentsArray.add(baselineClusterObject);
				}

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

			canaryList.add(sDEJSON);

		}
		//finalResult.put(key, value)
		System.out.println( "Result : " + canaryList.toJSONString() );
		return canaryList.toJSONString();
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
	public String getCanaryStatus(int id){

		CanaryDao canaryDao = new CanaryDaoImpl();
		Canary canary =  canaryDao.getCanaryById(id);
		JSONObject sDEJSON = new JSONObject();

		if(canary != null && canary.getN42Id() ==  id ){
			JSONObject status = new JSONObject();
			status.put("status", canary.getStatus().getStatus());
			status.put("complete", canary.getStatus().getComplete());
			sDEJSON.put("status", status);
		}else{
			sDEJSON.put("status", "Canary does not exist with id:"+id);
		}
		return sDEJSON.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public String getCanaryHealth(int id){

		CanaryDao canaryDao = new CanaryDaoImpl();
		Canary canary =  canaryDao.getCanaryById(id);
		JSONObject sDEJSON = new JSONObject();

		if(canary != null && canary.getN42Id() ==  id ){
			sDEJSON.put("status", canary.getHealth());
		}else{
			sDEJSON.put("status", "Canary does not exist with id:"+id);
		}
		return sDEJSON.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public String getCanaryConfigs(String applicationName){
		CanaryDao canaryDao = new CanaryDaoImpl();
		List<Canary> canaries =  canaryDao.getCanaryByApplication(applicationName);
		JSONArray array = new JSONArray();
		if(canaries == null || canaries.isEmpty() ){
			JSONObject sDEJSON = new JSONObject();
			sDEJSON.put("status", "Canaries does not exist with given application name");
			array.add(sDEJSON);
		}else{
			for(Canary canary : canaries){
				JSONObject sDEJSON = new JSONObject();
				sDEJSON.put("canaryName", canary.getCanaryName());
				sDEJSON.put("lifetimeMinutes", canary.getLifetimeMinutes());
				sDEJSON.put("combinedCanaryResultStrategy", canary.getCombinedCanaryResultStrategy());
				sDEJSON.put("canaryAnalysisName", canary.getAnalysisName());
				sDEJSON.put("notificationHours", canary.getNotificationHours());
				sDEJSON.put("canaryAnalysisIntervalMins", canary.getCanaryAnalysisIntervalMins());
				sDEJSON.put("params", canary.getParams());
				sDEJSON.put("canaryResultScore", canary.getCanaryResultScore());
				sDEJSON.put("minimumCanaryResultScore", canary.getMinimumCanaryResultScore());
				array.add(sDEJSON);
			}
		}

		return array.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public String disableCanary(Integer id, String reason) {
		CanaryDao canaryDao = new CanaryDaoImpl();
		Canary canary = canaryDao.getCanaryById(id);
		JSONObject response = new JSONObject();
		if(canary != null){
			canary.setReason(reason);
			canary.getStatus().setStatus("DISABLED");
			canary.getStatus().setComplete(false);
			canaryDao.disableCanary(canary);
			response.put("status", "Canary disabled");
		}else{
			response.put("status", "Canary does not exist with id:"+id);
		}
		return response.toString();
	}

	@SuppressWarnings("unchecked")
	public String getMetricGraphData(Integer id, String metricName) {
		CanaryDao canaryDao = new CanaryDaoImpl();
		Canary canary = canaryDao.getCanaryById(id);
		CasServiceMetricDetailsDao casSMDetailsDao = new CasServiceMetricDetailsDaoImpl();
		CasServiceMetricDetails casServiceMetricDetails = casSMDetailsDao.getCasServiceMetricDetails(metricName);
		Map<String,Object> clusterObject = new LinkedHashMap<String, Object>();

		Map<String,Object> root = new LinkedHashMap<String, Object>();
		List<Object> metricsArray = new ArrayList<Object>();
		root.put("metrics", metricsArray);

		Map<String,Object> metricVersion1 = getMetricByVersion(canary.getBaselineClusterIp(), casServiceMetricDetails);
		metricsArray.add(metricVersion1);

		Map<String,Object> metricVersion2 = getMetricByVersion(canary.getCanaryClusterIp(), casServiceMetricDetails);
		metricsArray.add(metricVersion2);

		root.put("cache_time", 0);
		long startTimeInMillis = canary.getN42CreatedDate().getTime();
		long endTimeInMillis = startTimeInMillis + 72000000 ;

		root.put("start_absolute", startTimeInMillis-600000);
		root.put("end_absolute", endTimeInMillis);

		String kairosRequest = JSONUtils.getFormattedJson(root);
		System.out.println(kairosRequest);
		String response = HttpUtils.postRequest(kairosRequest);
		
		Map<String,Object> graphRoot = new LinkedHashMap<String, Object>();
		Map<String,Object> version1 = new LinkedHashMap<String, Object>();
		Map<String,Object> version2 = new LinkedHashMap<String, Object>();
		graphRoot.put("metric", metricName);
		graphRoot.put("version1", version1);
		version1.put("host", canary.getBaselineClusterIp());
		List<Object> ver1 = new ArrayList<Object>();
		version1.put("data", ver1);

		graphRoot.put("version2", version2);
		version1.put("host", canary.getCanaryClusterIp());
		JSONArray ver2 = new JSONArray();
		version2.put("data", ver2);

		JSONParser parser = new JSONParser();
		try {
			Map<String,Object> responseRoot = (Map<String,Object>) parser.parse(response);
			List<Object> queries = (List<Object>) responseRoot.get("queries");
//			System.out.println(queries.size());
			for(Object query : queries) {
				Map<String,Object> queryObj = (Map<String,Object>) query;
				List<Object> results = (List<Object>) queryObj.get("results");
				if(CollectionUtils.isEmpty(results)) {
					continue;
				}
				Map<String,Object> resObj = (Map<String,Object>) results.get(0);
				if(resObj == null) {
					continue;
				}
				
				Map<String,Object> tagsObj = (Map<String,Object>)resObj.get("tags");
				if(tagsObj == null) {
					continue;
				}
				List<Object> hosts = (List<Object>) tagsObj.get("host");
				List<Object> datapoints = (List<Object>) resObj.get("values");


				int count = 0;
				if(CollectionUtils.isNotEmpty(hosts)) {
					String ip = hosts.get(0).toString();
					if(CollectionUtils.isNotEmpty(datapoints)) {
						if(StringUtils.equalsIgnoreCase(canary.getBaselineClusterIp(), ip)) {
							for(Object dtptObject : datapoints) {
								List<Object> dtpt = (List<Object>) dtptObject;
								Map<String,Object> data = new LinkedHashMap<String, Object>();
								Date date = new Date((Long)dtpt.get(0));
								String strDateTime = date.toGMTString().trim();
								String strDate = strDateTime.split(" ")[0] +" "+strDateTime.split(" ")[1] +" "+strDateTime.split(" ")[2];
								String strTime = strDateTime.split(" ")[3] +" "+strDateTime.split(" ")[4];
								data.put("time", strTime);
								data.put("date", strDate);
								data.put("value", dtpt.get(1));
								ver1.add(data);
								
								if(count ++ > 10) break;
							}
						}
						else {
							for(Object dtptObject : datapoints) {
								List<Object> dtpt = (List<Object>) dtptObject;
								Map<String,Object> data = new LinkedHashMap<String, Object>();
								Date date = new Date((Long)dtpt.get(0));
								String strDateTime = date.toGMTString().trim();
								String strDate = strDateTime.split(" ")[0] +" "+strDateTime.split(" ")[1] +" "+strDateTime.split(" ")[2];
								String strTime = strDateTime.split(" ")[3] +" "+strDateTime.split(" ")[4];
								data.put("time", strTime);
								data.put("date", strDate);
								data.put("value", dtpt.get(1));
								ver2.add(data);
								
								if(count ++ > 10) break;
							}
						}
					}

				}

			}
//			System.out.println("********************" +graphRoot.toJSONString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		System.out.println(response);
		return JSONUtils.getFormattedJson(graphRoot);
	}

	private Map<String,Object> getMetricByVersion(String ip, CasServiceMetricDetails casServiceMetricDetails) {
		Map<String,Object> metricObj = new LinkedHashMap<String, Object>();
		metricObj.put("name", casServiceMetricDetails.getMetricName());

		Map<String,Object> tagsObj = new LinkedHashMap<String, Object>();
		metricObj.put("tags", tagsObj);
		List<Object> host = new ArrayList<Object>();
		host.add(ip);
		tagsObj.put("host", host);


		List<Object> aggregators = new ArrayList<Object>();

		Map<String,Object> aggrObj = new LinkedHashMap<String, Object>();
		aggrObj.put("name", casServiceMetricDetails.getKairosAggregator());
		aggrObj.put("align_sampling", true);

		Map<String,Object> sampling = new LinkedHashMap<String, Object>();
		sampling.put("value", "1");
		sampling.put("unit", "seconds");
		aggrObj.put("sampling", sampling);

		aggregators.add(aggrObj);
		metricObj.put("aggregators", aggregators);

		return metricObj;
	}
	public static void main(String[] args) {
		CanaryAnalysisService aa = new CanaryAnalysisService();
		System.out.println(aa.getMetricGraphData(41, "http.responsetime"));
		//		JSONParser parser = new JSONParser();
		//		CanaryAnalysisService canaryAnalysisService = new CanaryAnalysisService();
		//		try {   
		//			String sDEString = "{\n\t\"canary_output\": {\n\t\t\"canaryId\": \"6\",\n\t\t\"applicationName\": \"appgit\",\n\t\t\"startTime\": \"1465814063266\",\n\t\t\"endTime\": \"1465815263266\",\n\t\t\"canaryScore\": 100,\n\t\t\"results\": [{\n\t\t\t\"metricName\": \"system.cpu.util\",\n\t\t\t\"score\": 100\n\t\t}, {\n\t\t\t\"metricName\": \"system.disk.blocks.read\",\n\t\t\t\"score\": \"NA\",\n\t\t\t\"error\": \"Sets has insufficient datapoints or zero standard deviation.\"\n\t\t}, {\n\t\t\t\"metricName\": \"system.mem.util\",\n\t\t\t\"score\": \"NA\",\n\t\t\t\"error\": \"Sets has insufficient datapoints or zero standard deviation.\"\n\t\t}, {\n\t\t\t\"metricName\": \"system.net.transmit_rate\",\n\t\t\t\"score\": 100\n\t\t}, {\n\t\t\t\"metricName\": \"http.requests\",\n\t\t\t\"score\": \"NA\",\n\t\t\t\"error\": \"Sets has insufficient datapoints or zero standard deviation.\"\n\t\t}]\n\t}\n}";
		//			JSONObject sDEJSON = null;
		//			sDEJSON = (JSONObject) parser.parse(sDEString);
		//			canaryAnalysisService.saveCanaryAnalysisResult(sDEJSON.toJSONString());
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		} 
	}


}

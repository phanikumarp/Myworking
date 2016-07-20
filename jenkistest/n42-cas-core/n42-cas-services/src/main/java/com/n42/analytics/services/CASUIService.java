package com.n42.analytics.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.n42.analytics.exceptions.CanaryNotFoundException;
import com.n42.analytics.hibernate.dao.CanaryAnalysisDao;
import com.n42.analytics.hibernate.dao.CanaryAnalysisDaoImpl;
import com.n42.analytics.hibernate.dao.CanaryDao;
import com.n42.analytics.hibernate.dao.CanaryDaoImpl;
import com.n42.analytics.util.ModelUtils;

import n42.domain.model.Canary;
import n42.domain.model.CanaryAnalysis;
import n42.domain.model.MetricScore;
import n42.domain.model.VersionStats;

public class CASUIService {

	@SuppressWarnings("unchecked")
	public JSONObject getCanaryAanalysisOutput(int canaryId) {

		JSONObject canaryOutputObj = new JSONObject();
		JSONObject canaryAnalysisObj = new JSONObject();

		try {

			Float canaryFinalScore = null;

			CanaryDao cDao = new CanaryDaoImpl();
			CanaryAnalysisDao cADao = new CanaryAnalysisDaoImpl();

			Canary canary = cDao.getCanaryById(canaryId);
			CanaryAnalysis canaryScore = new CanaryAnalysis();
			MetricScore metricScore = new MetricScore();
			//		BucketScore bucketScore = new BucketScore();

			JSONArray groupScoreList = new JSONArray();
			//		JSONArray bucketSc = new JSONArray();

			if( canary == null ){
				throw new CanaryNotFoundException("No Canary Present For Corresponding Canary Id");
			}

			if(canary.getStatus() != null){
				if( canary.getStatus().getStatus() != null ){
					canaryAnalysisObj.put("status", canary.getStatus().getStatus().toString());
				}
			}	

			int durationInMins = 0 ; 
			if(canary.getLifetimeMinutes() != null){
				canaryAnalysisObj.put("duration", canary.getLifetimeMinutes());
				durationInMins =  canary.getLifetimeMinutes() ; 
			}	

			if( canary.getHealth() != null ){
				if( canary.getHealth().getHealth() != null ){
					canaryAnalysisObj.put("health", canary.getHealth().getHealth());
				}
				if( canary.getHealth().getMessage() != null ){
					canaryAnalysisObj.put("healthMessage", canary.getHealth().getMessage());
				}
			}
			if( canary.getCanaryName() != null){
				canaryAnalysisObj.put("name", canary.getCanaryName() );
			}
			if(canary.getLaunchedDate() != null){
				canaryAnalysisObj.put("launchedDate", canary.getLaunchedDate() );
			}
			else{
				canaryAnalysisObj.put("launchedDate", canary.getN42CreatedDate().toString() );
			}

			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
			if( durationInMins > 0 && canary.getLaunchedDate() == null){
				long launchedDate = canary.getN42CreatedDate().getTime() ;
				long endDateLong = launchedDate + durationInMins * 60 * 1000 ;
				String endDate = sdf.format(new Date(endDateLong));
				canaryAnalysisObj.put("endDate", endDate );
			}
			else if(durationInMins > 0 && canary.getLaunchedDate() != null) {
				long launchedDate = 0;
				try {
					launchedDate = sdf.parse(canary.getLaunchedDate()).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				long endDateLong = launchedDate + durationInMins * 60 * 1000 ;
				String endDate = sdf.format(new Date(endDateLong));
				canaryAnalysisObj.put("endDate", endDate );
			}
			else if(canary.getLaunchedDate() == null){
				canaryAnalysisObj.put("endDate", canary.getN42CreatedDate().toString() );
			}
			else if(canary.getLaunchedDate() != null){
				String endDate = sdf.format(new Date(canary.getLaunchedDate()));
				canaryAnalysisObj.put("endDate", endDate );
			}

			canaryScore = cADao.getCanaryAnalysisById(canaryId);
			if(canaryScore == null){
				throw new CanaryNotFoundException("No Analysis Result Present For Corresponding Canary Id");
			}

			if(canaryScore.getFinalScore() != null){
				canaryFinalScore = canaryScore.getFinalScore() ;
				canaryAnalysisObj.put("canaryFinalScore",canaryFinalScore);

				if( canary.getMinimumCanaryResultScore() != null ){
					float minScore = Float.parseFloat(canary.getMinimumCanaryResultScore()) ; 
					if( Float.compare( minScore, canaryFinalScore ) > 0 ){
						canaryAnalysisObj.put("canaryFinalColor", "red");
					}
					else {
						if( canary.getCanaryResultScore() != null ){
							float successScore =  Float.parseFloat(canary.getCanaryResultScore()) ; 
							if(Float.compare( minScore, canaryFinalScore ) <= 0 && Float.compare( successScore, canaryFinalScore ) > 0) {
								canaryAnalysisObj.put("canaryFinalColor", "yellow");
							}
							else if(Float.compare( successScore, canaryFinalScore ) <= 0){
								canaryAnalysisObj.put("canaryFinalColor", "green");
							}
						}
					}
				}

			}

			List<Integer> metricScoreIDs = canaryScore.getMetricScoreIds();
			Map<Integer, MetricScore> metricScoreIdMap = ModelUtils.getMetricScoreIDMap();
			for(Integer metricScoreID: metricScoreIDs){

				JSONArray metricScoreArray = new JSONArray() ;

				JSONObject  groupObj = new JSONObject();
				JSONObject  metricObj = new JSONObject();
				
				metricScore = metricScoreIdMap.get(metricScoreID.intValue());
				if(metricScore.getMetricType() != null){
					String metricType = metricScore.getMetricType();
					metricObj.put("metricType", metricType);
					if(groupScoreList.isEmpty()){
						groupObj.put("name", metricType);
					}
					else {
						for(int i =0 ; i < groupScoreList.size() ; i++){
							JSONObject groupScoreObj = (JSONObject) groupScoreList.get(i);
							if( groupScoreObj.get("name") !=  null && StringUtils.equalsIgnoreCase(groupScoreObj.get("name").toString(), metricType) ){
								metricScoreArray = (JSONArray) groupScoreObj.get("metricList");
								groupObj = groupScoreObj ;
								groupScoreList.remove(i);
							}
						}
						if(groupObj.isEmpty()){
							groupObj.put("name", metricType);
						}
					}
				}
				else{
					continue;
				}

				if(metricScore.getMetricName() != null){
					String metricName = metricScore.getMetricName();
					metricObj.put("metricName", metricName);
				}

				if( metricScore.getScore()!= null){
					Float metricResult = metricScore.getScore();
					metricObj.put("metricScore", metricResult);
					Float groupScore = 0.0f ;
					if( !groupObj.isEmpty() && groupObj.get("groupScore") == null && groupObj.get("groupScore") == null){
						groupScore = metricResult ;
						groupObj.put("groupScore", groupScore);
						groupObj.put("groupsSuccessMetricCount", 1);
					}
					else if(!groupObj.isEmpty() && groupObj.get("groupScore") != null && groupObj.get("groupsSuccessMetricCount") != null){
						groupScore = (Float) groupObj.get("groupScore");
						int groupsSuccessMetricCount = (int)groupObj.get("groupsSuccessMetricCount");
						groupScore = groupScore * groupsSuccessMetricCount ;
						groupsSuccessMetricCount++;
						groupScore += metricResult;
						groupScore = groupScore/groupsSuccessMetricCount ;
						groupObj.put( "groupScore",  groupScore);
						groupObj.put("groupsSuccessMetricCount", groupsSuccessMetricCount);
					}
					if( canary.getMinimumCanaryResultScore() != null ){
						float minScore = Float.parseFloat(canary.getMinimumCanaryResultScore()) ; 
						if( Float.compare( minScore, metricResult ) > 0 ){
							metricObj.put( "metricColor", "red");
						}
						else {
							if( canary.getCanaryResultScore() != null ){
								float successScore =  Float.parseFloat(canary.getCanaryResultScore()) ; 
								if(Float.compare( minScore, metricResult ) <= 0 && Float.compare( successScore, metricResult ) > 0) {
									metricObj.put( "metricColor", "yellow");
								}
								else if(Float.compare( successScore, metricResult ) <= 0){
									metricObj.put( "metricColor", "green");
								}
							}
						}
						if( Float.compare( minScore, groupScore ) > 0 ){
							groupObj.put( "groupColor", "red");
						}
						else {
							if( canary.getCanaryResultScore() != null ){
								float successScore =  Float.parseFloat(canary.getCanaryResultScore()) ; 
								if(Float.compare( minScore, groupScore ) <= 0 && Float.compare( successScore, groupScore ) > 0) {
									groupObj.put( "groupColor", "yellow");
								}
								else if(Float.compare( successScore, groupScore ) <= 0){
									groupObj.put( "groupColor", "green");
								}
							}
						}
					}

				}

				if(metricScore.getError() != null){
					String metricError = metricScore.getError();
					metricObj.put("metricError", metricError);
				}

				if(metricScore.getVersion1Stats() != null && metricScore.getVersion2Stats() != null){

					VersionStats vS = metricScore.getVersion1Stats() ;
					JSONObject statsObj = new JSONObject();
					JSONObject version1Obj = fillVersionObj(vS);
					statsObj.put("version1", version1Obj);

					vS = metricScore.getVersion2Stats();
					JSONObject version2Obj = fillVersionObj(vS);
					statsObj.put("version2", version2Obj);
					metricObj.put("stats", statsObj);
				}

				//			List<Integer> BucketScoreIDs = metricScore.getBucketScoreIds();
				//			bucketSc = new JSONArray();
				//			for(Integer BucketScoreID: BucketScoreIDs){
				//				Map<Integer, BucketScore> bucketScoreIDMap = ModelUtils.getBucketScoreIDMap();
				//					Map<String, Object>  bucketObj = new LinkedHashMap<String, Object>();
				//						bucketScore= bucketScoreIDMap.get(BucketScoreID);
				//						String bucketRange = bucketScore.getRange();
				//						Float bucketRangeScore = bucketScore.getScore();
				//						bucketObj.put("bucketRange" , bucketRange);
				//						bucketObj.put("bucketRangeScore" , bucketRangeScore);
				//						bucketSc.add(bucketObj);
				//			}
				//			metricObj.put("buckets", bucketSc);
				metricScoreArray.add(metricObj);
				groupObj.put("metricList", metricScoreArray);
				groupScoreList.add(groupObj);
			}
			canaryAnalysisObj.put("canaryId",canaryId);
			canaryAnalysisObj.put("results", groupScoreList);


		} catch (CanaryNotFoundException e) {
			e.printStackTrace();
		}

		canaryOutputObj.put("canary_output", canaryAnalysisObj);

		System.out.println(canaryOutputObj);

		return canaryOutputObj;
	}

	@SuppressWarnings("unchecked")
	private JSONObject fillVersionObj( VersionStats vS) {

		JSONObject versionObj = new JSONObject();
		if(vS.getMin()!=null){
			versionObj .put( "Min", vS.getMin());
		}
		if(vS.getFirstQu()!=null){
			versionObj.put( "1stQuantile", vS.getFirstQu());
		}
		if( vS.getMedian()!=null){
			versionObj.put( "Median", vS.getMedian());
		}
		if(vS.getMean()!=null){
			versionObj.put( "Mean", vS.getMean());
		}
		if(vS.getThirdQu()!=null){
			versionObj.put( "3rdQuantile", vS.getThirdQu());
		}
		if( vS.getMax()!=null){
			versionObj.put( "Max", vS.getMax());
		}
		return versionObj;
	}

	public static void main(String args[]) throws ParseException{

		CASUIService cASUIService = new CASUIService();

		int canaryId = 13;
		JSONObject result = cASUIService.getCanaryAanalysisOutput(canaryId);
		System.out.println("Result : " + result );
	}
}

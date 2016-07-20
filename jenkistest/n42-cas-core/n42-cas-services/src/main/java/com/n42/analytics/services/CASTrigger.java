package com.n42.analytics.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import n42.domain.model.Canary;
import n42.domain.model.Health;
import n42.domain.model.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.n42.analytics.exceptions.CASException;
import com.n42.analytics.exceptions.InvalidTimeUnitException;
import com.n42.analytics.hibernate.dao.CanaryAnalysisDao;
import com.n42.analytics.hibernate.dao.CanaryAnalysisDaoImpl;
import com.n42.analytics.hibernate.dao.CanaryDao;
import com.n42.analytics.hibernate.dao.CanaryDaoImpl;

public class CASTrigger {

	//Triggers CAS 
	@SuppressWarnings("unchecked")
	public JSONObject triggerCAS(JSONObject sDEJSON, int lifetimeMinutes, int timeSlice, String sliceUnit, Canary canary, int lookbackMins){
		JSONObject result = new JSONObject();
		try {
			TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
			CanaryAnalysisService canaryAnalysisService = new CanaryAnalysisService();
			CASUIService casOutputExtractor = new CASUIService();

			//TODO checks for time slice  should be less then timeDuration if slice unit and duration unit is equal.      Need to see custom exception. 
			//check for slice unit to be in a smaller time format then time duration
			//			if((StringUtils.equalsIgnoreCase(durationUnit, "hour")||StringUtils.equalsIgnoreCase(durationUnit, "hours"))){
			//				if(!(StringUtils.equalsIgnoreCase(sliceUnit, "minute")||StringUtils.equalsIgnoreCase(sliceUnit, "minutes"))
			//						|| !(StringUtils.equalsIgnoreCase(sliceUnit, "second")||StringUtils.equalsIgnoreCase(sliceUnit, "seconds"))){
			//					InvalidTimeUnitException iUException =  new InvalidTimeUnitException(" Invalid Time Slice Unit ") ;
			//					iUException.printStackTrace();
			//				}
			//			}
			//			else if((StringUtils.equalsIgnoreCase(durationUnit, "minute")||StringUtils.equalsIgnoreCase(durationUnit, "minutes"))){
			//				if(!(StringUtils.equalsIgnoreCase(sliceUnit, "second")||StringUtils.equalsIgnoreCase(sliceUnit, "seconds"))){
			//					InvalidTimeUnitException iUException =  new InvalidTimeUnitException(" Invalid Time Slice Unit ") ;
			//					iUException.printStackTrace();
			//				}
			//			}
			//			else if((StringUtils.equalsIgnoreCase(durationUnit, "seconds")||StringUtils.equalsIgnoreCase(durationUnit, "seconds"))){
			//				if(!(StringUtils.equalsIgnoreCase(sliceUnit, "second")||StringUtils.equalsIgnoreCase(sliceUnit, "seconds"))){
			//					InvalidTimeUnitException iUException =  new InvalidTimeUnitException(" Invalid Time Slice Unit ") ;
			//					iUException.printStackTrace();
			//					throw new InvalidTimeUnitException(" Invalid Time Unit ") ;
			//				}
			//			}
			//			//check if time units are correct
			//			else if( !StringUtils.equalsIgnoreCase(durationUnit, "seconds")|| !StringUtils.equalsIgnoreCase(durationUnit, "seconds")
			//					|| !StringUtils.equalsIgnoreCase(sliceUnit, "second")|| !StringUtils.equalsIgnoreCase(sliceUnit, "seconds")
			//					|| !StringUtils.equalsIgnoreCase(durationUnit, "minute")|| !StringUtils.equalsIgnoreCase(durationUnit, "minutes")
			//					|| !StringUtils.equalsIgnoreCase(sliceUnit, "minute")|| !StringUtils.equalsIgnoreCase(sliceUnit, "minutes")
			//					|| !StringUtils.equalsIgnoreCase(durationUnit, "hour")|| !StringUtils.equalsIgnoreCase(durationUnit, "hours")
			//					|| !StringUtils.equalsIgnoreCase(sliceUnit, "hour")|| !StringUtils.equalsIgnoreCase(sliceUnit, "hours")
			//					|| !StringUtils.equalsIgnoreCase(durationUnit, "day")|| !StringUtils.equalsIgnoreCase(durationUnit, "days")
			//					|| !StringUtils.equalsIgnoreCase(sliceUnit, "day")|| !StringUtils.equalsIgnoreCase(sliceUnit, "days")){
			//				InvalidTimeUnitException iUException =  new InvalidTimeUnitException(" Invalid Time Unit : units allowed are seconds, minutes, hours, day") ;
			//				iUException.printStackTrace();
			//			}
			//
			//calculating the number of times we need to call CAS
			int maxLoopCount = maxLoopCountCalculator(lifetimeMinutes, sliceUnit, timeSlice, sliceUnit);


			LinkedList<Integer> versionIds = getVersionIds(sDEJSON);			//To get the version IDs from the Spinnaker JSON
			int versionId1 = 1 ;
			int versionId2 = 2 ;
			if(CollectionUtils.isNotEmpty(versionIds)){
				versionId1 = versionIds.get(0).intValue();
				versionId2 = versionIds.get(1).intValue();
			}

			//		maxLoopCount=1;
			//Loop for number of times according to time duration and slice unit
			for (int i = 0; i < maxLoopCount; i++) {
				CanaryAnalysisDao canaryAnalysisDao = new CanaryAnalysisDaoImpl();
				float canaryResult = canaryAnalysisDao.getCanaryResultById(canary.getN42Id());
				CanaryDao  canaryDao = new CanaryDaoImpl() ;
				canary =  canaryDao.getCanaryById(canary.getN42Id());
				Status statusObj = canary.getStatus();
				float minimumScore = Float.parseFloat( canary.getMinimumCanaryResultScore() );
				if( 0.0 < canaryResult && canaryResult < minimumScore ){
					
					Health health = new Health();
					health.setHealth("UNHEALTHY");
					health.setMessage("Result Score Is Lower Then Minimum Score. ");
					canary.setHealth(health);
					canaryDao.updateCanary(canary); 
					
					AutoActionThreadTrigger actionThreadTrigger = new AutoActionThreadTrigger(canary);
					Thread trigger =new Thread(actionThreadTrigger);  
					trigger.start();  
					
				}
				if(  BooleanUtils.isFalse( statusObj.getComplete()) ){
					result = addTimeStampAndcallRAS(sDEJSON, lookbackMins, sliceUnit, versionId1, versionId2, canaryAnalysisService, casOutputExtractor, canary.getLaunchedDate());
					TimeUnit.MINUTES.sleep(timeSlice);

				}
			}

			System.out.println("level Clear 8D........................................................");
			System.out.println("Output JSon : " + result);
		} catch (InterruptedException e) {	
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	class AutoActionThreadTrigger implements Runnable{  

		private Canary canary ;

		public AutoActionThreadTrigger(Canary canary) {
			this.canary = canary;
		}

		public void run(){  
			System.out.println("Auto Action Thread Started : ");  
			CASAutomatedServices automatedServices = new CASAutomatedServices() ;

			//Thread thread = new Thread()
			//TODO start another thread with different actions 
			//and make the below call in that thread. 
			automatedServices.startAutomatedActions( canary ); 
		}
	}

	//To get the version IDs from the Spinnaker JSON
	public LinkedList<Integer> getVersionIds( JSONObject sDEJSON ) {

		int versionId = 0;
		LinkedList<Integer> versionIds = new LinkedList<Integer>();
		if(sDEJSON != null){
			if( sDEJSON.get("service") != null ){

				JSONObject serviceObj = (JSONObject) sDEJSON.get("service") ; 
				int noOfVersions = 2 ;

				for(int i =1 ; i <= noOfVersions ; i++){

					String versionNum = "version" + i ;
					if(serviceObj.get(versionNum) != null){
						JSONObject versionObj = (JSONObject) serviceObj.get(versionNum) ;
						Object temp = versionObj.get("serviceVersionId");
						versionId = (int) (long) temp;
						versionIds.add(versionId);
					}
				}
			}
		}
		return versionIds;
	}

	//Adds time stamp to the existing JSON and Calls RAS to analyze the data.
	@SuppressWarnings("unchecked")
	private JSONObject addTimeStampAndcallRAS(JSONObject sDEJSON, int lookbackMins, String sliceUnit, int versionId1, int versionId2, CanaryAnalysisService canaryAnalysisService, CASUIService casOutputExtractor, String canaryLaunchedDate) {

		JSONObject result = new JSONObject();
		try {
			String[] startAndEndDays = new String[2];
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

			long start = 0; 
			if(lookbackMins > 0){
				start = timeDecrementorByTimeSlice(new Date(), lookbackMins, sliceUnit);
			}
			else{
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
				start = sdf.parse(canaryLaunchedDate).getTime(); 
			}

			long end = timeDecrementorByTimeSlice(new Date(), (Integer) null, null);

			

			startAndEndDays[0] = dateFormatGmt.format(start);
			startAndEndDays[1] = dateFormatGmt.format(end);
			if(sDEJSON.get("application")!= null){
				System.out.println(sDEJSON.get("application"));
				JSONObject applicationObj = (JSONObject) sDEJSON.get("application");
				/*applicationObj.put("startTime", "1465464300000");
				applicationObj.put("endTime", "1465465133000");*/
				applicationObj.put("startTime", start);
				applicationObj.put("endTime", end);
				sDEJSON.put("application",applicationObj);
			}
			System.out.println("**************"+sDEJSON.toJSONString());


			//TODO call RAS 
			/*String input = "{\"application\":{\"startTime\":1465976178987,\"canaryID\":\"42\",\"applicationName\":\"appgit\",\"endTime\":1465977978987,\"version2\":{\"metrics\":[{\"tagsData\":[{\"tagName\":\"host\",\"tagValue\":\"ip-10-0-0-130\"}],\"aggregator\":{\"unit\":\"seconds\",\"time\":\"1\",\"Function\":\"avg\"},\"metricType\":\"other\",\"metricName\":\"system.cpu.util\"},{\"tagsData\":[{\"tagName\":\"host\",\"tagValue\":\"ip-10-0-0-130\"}],\"aggregator\":{\"unit\":\"seconds\",\"time\":\"1\",\"Function\":\"avg\"},\"metricType\":\"other\",\"metricName\":\"system.disk.blocks.read\"},{\"tagsData\":[{\"tagName\":\"host\",\"tagValue\":\"ip-10-0-0-130\"}],\"aggregator\":{\"unit\":\"seconds\",\"time\":\"1\",\"Function\":\"avg\"},\"metricType\":\"other\",\"metricName\":\"system.mem.util\"},{\"tagsData\":[{\"tagName\":\"host\",\"tagValue\":\"ip-10-0-0-130\"}],\"aggregator\":{\"unit\":\"seconds\",\"time\":\"1\",\"Function\":\"avg\"},\"metricType\":\"other\",\"metricName\":\"system.net.transmit_rate\"},{\"tagsData\":[{\"tagName\":\"host\",\"tagValue\":\"ip-10-0-0-130\"}],\"aggregator\":{\"unit\":\"seconds\",\"time\":\"1\",\"Function\":\"count\"},\"metricType\":\"load\",\"metricName\":\"http.requests\"}],\"versionID\":null,\"versionName\":\"appgit--canary\",\"versionType\":\"current\"},\"version1\":{\"metrics\":[{\"tagsData\":[{\"tagName\":\"host\",\"tagValue\":\"ip-10-0-0-84\"}],\"aggregator\":{\"unit\":\"seconds\",\"time\":\"1\",\"Function\":\"avg\"},\"metricType\":\"other\",\"metricName\":\"system.cpu.util\"},{\"tagsData\":[{\"tagName\":\"host\",\"tagValue\":\"ip-10-0-0-84\"}],\"aggregator\":{\"unit\":\"seconds\",\"time\":\"1\",\"Function\":\"avg\"},\"metricType\":\"other\",\"metricName\":\"system.disk.blocks.read\"},{\"tagsData\":[{\"tagName\":\"host\",\"tagValue\":\"ip-10-0-0-84\"}],\"aggregator\":{\"unit\":\"seconds\",\"time\":\"1\",\"Function\":\"avg\"},\"metricType\":\"other\",\"metricName\":\"system.mem.util\"},{\"tagsData\":[{\"tagName\":\"host\",\"tagValue\":\"ip-10-0-0-84\"}],\"aggregator\":{\"unit\":\"seconds\",\"time\":\"1\",\"Function\":\"avg\"},\"metricType\":\"other\",\"metricName\":\"system.net.transmit_rate\"},{\"tagsData\":[{\"tagName\":\"host\",\"tagValue\":\"ip-10-0-0-84\"}],\"aggregator\":{\"unit\":\"seconds\",\"time\":\"1\",\"Function\":\"count\"},\"metricType\":\"load\",\"metricName\":\"http.requests\"}],\"versionID\":null,\"versionName\":\"appgit--baseline\",\"versionType\":\"base\"}}}";
			JSONParser parser = new JSONParser();
			JSONObject inputToR = null;
			try {
				inputToR = (JSONObject) parser.parse(input);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

			Process shell = Runtime.getRuntime().exec(new String[] { "/usr/bin/Rscript","/opt/AWSmappingFile/testCanaryAnalysis.R",sDEJSON.toString()});
			BufferedReader reader = new BufferedReader(new InputStreamReader(shell.getInputStream())); 
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);

			}

			//			String outputRASString = "{\"Comparison\":{\"processID\":\"abc123\",\"appID\":\"23\",\"serviceID\":\"\",\"ComparisonResult\":[{\"resultColorCode\":\"1\",\"betterVersion\":\"versionID\",\"overallScore\":\"1\"}],\"errorID\":[{\"OK\":\"True\",\"versionModelNotFound\":\"False\",\"noCommonBucket\":\"False\"}],\"versionID\":[{\"version1\":\"1\",\"version2\":\"2\"}],\"metrics\":[{\"metricId\":\"metric1\",\"metricComparisonResult\":[{\"resultColorCode\":\"1\",\"betterVersion\":\"versionID\",\"metricScore\":\"1\"}],\"buckets\":[{\"bucketID\":\"bucket1\",\"loadRange\":\"0-2000\",\"versionDifferent\":\"TRUE\",\"bucketComparisonResult\":[{\"resultColorCode\":\"1\",\"betterVersion\":\"versionID\",\"bucketScore\":\"1\"}]},{\"bucketID\":\"bucket2\",\"loadRange\":\"6000-8000\",\"versionDifferent\":\"FALSE\",\"bucketComparisonResult\":[{\"resultColorCode\":\"1\",\"betterVersion\":\"none\",\"bucketScore\":\"1\"}]}]},{\"metricId\":\"metric12\",\"metricComparisonResult\":[{\"resultColorCode\":\"1\",\"betterVersion\":\"versionID\",\"metricScore\":\"1\"}],\"buckets\":[{\"bucketID\":\"bucket1\",\"loadRange\":\"0-2000\",\"versionDifferent\":\"TRUE\",\"bucketComparisonResult\":[{\"resultColorCode\":\"1\",\"betterVersion\":\"versionID\",\"bucketScore\":\"1\"}]},{\"bucketID\":\"bucket2\",\"loadRange\":\"6000-8000\",\"versionDifferent\":\"FALSE\",\"bucketComparisonResult\":[{\"resultColorCode\":\"1\",\"betterVersion\":\"none\",\"bucketScore\":\"1\"}]}]}]}}";
			//			JSONParser parser = new JSONParser();
			//			JSONObject outputRASJson;
			//			outputRASJson = (JSONObject) parser.parse(outputRASString);
			//			canaryAnalysisService.saveCanaryAnalysisModel(outputRASJson.toJSONString()); 		// Saving the output

			//			result = new JSONObject(casOutputExtractor.getCanaryAanalysisOutput(versionId1,versionId2)); 		//Extracting the models from DB 
			//			sleep(timeSlice, sliceUnit);			// will make the thread wait for a time slice to call RAS again.

		} 
		catch (IOException | NumberFormatException | java.text.ParseException e1) {
			e1.printStackTrace();
		} 
		return result;

	}

	//Sleeps the thread for the time slice as per the slice unit.
	protected void sleep(int timeSlice, String sliceUnit) {
		try {
			if((StringUtils.equalsIgnoreCase(sliceUnit, "minute")||StringUtils.equalsIgnoreCase(sliceUnit, "minutes"))){
				TimeUnit.MINUTES.sleep(timeSlice);
			}
			else if((StringUtils.equalsIgnoreCase(sliceUnit, "second")||StringUtils.equalsIgnoreCase(sliceUnit, "seconds"))){
				TimeUnit.SECONDS.sleep(timeSlice);
			}
			else if((StringUtils.equalsIgnoreCase(sliceUnit, "hour")||StringUtils.equalsIgnoreCase(sliceUnit, "hours"))){
				TimeUnit.HOURS.sleep(timeSlice);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	//how many times we need to call CAS according to whole duration and time slice.
	public int maxLoopCountCalculator(int timeDuration, String durationUnit, int timeSlice, String sliceUnit) {
		int maxLoopCount = 0;

		if(((StringUtils.equalsIgnoreCase(durationUnit, "hour")||StringUtils.equalsIgnoreCase(durationUnit, "hours")) 
				&& (StringUtils.equalsIgnoreCase(sliceUnit, "minute")||StringUtils.equalsIgnoreCase(sliceUnit, "minutes"))) 
				||((StringUtils.equalsIgnoreCase(durationUnit, "minute")||StringUtils.equalsIgnoreCase(durationUnit, "minutes")) 
						&& (StringUtils.equalsIgnoreCase(sliceUnit, "second")||StringUtils.equalsIgnoreCase(sliceUnit, "seconds")))){
			maxLoopCount = ( (timeDuration * 60) / (timeSlice) ) ;
		}
		else if((StringUtils.equalsIgnoreCase(durationUnit, "day")||StringUtils.equalsIgnoreCase(durationUnit, "days")) 
				&& (StringUtils.equalsIgnoreCase(sliceUnit, "hour")||StringUtils.equalsIgnoreCase(sliceUnit, "hours"))){
			maxLoopCount = ( (timeDuration * 24) / (timeSlice) ) ;
		}
		else if((StringUtils.equalsIgnoreCase(durationUnit, "hour")||StringUtils.equalsIgnoreCase(durationUnit, "hours")) 
				&& (StringUtils.equalsIgnoreCase(sliceUnit, "second")||StringUtils.equalsIgnoreCase(sliceUnit, "seconds"))){
			maxLoopCount = ( (timeDuration * 3600) / (timeSlice) ) ;
		}
		else if((StringUtils.equalsIgnoreCase(durationUnit, "hour") && StringUtils.equalsIgnoreCase(sliceUnit, "hours")) 
				|| (StringUtils.equalsIgnoreCase(durationUnit, "minutes") && StringUtils.equalsIgnoreCase(sliceUnit, "minutes"))
				|| (StringUtils.equalsIgnoreCase(durationUnit, "second") && StringUtils.equalsIgnoreCase(sliceUnit, "seconds"))){
			maxLoopCount = ( (timeDuration) / (timeSlice) ) ;
		}

		return maxLoopCount;
	}

	//Increases the time by time slice as per the slice unit and returns the time in long.
	public long timeDecrementorByTimeSlice(Date end, Integer lookbackMins, String sliceUnit) {
		long epoch =  end.getTime();

		if((StringUtils.equalsIgnoreCase(sliceUnit, "hour")||StringUtils.equalsIgnoreCase(sliceUnit, "hours"))){
			epoch = epoch - lookbackMins.intValue() * 3600 * 1000 ; 
		}
		else if((StringUtils.equalsIgnoreCase(sliceUnit, "second")||StringUtils.equalsIgnoreCase(sliceUnit, "seconds"))){
			epoch = epoch - lookbackMins.intValue() * 1000 ; 
		}
		else if((StringUtils.equalsIgnoreCase(sliceUnit, "minute")||StringUtils.equalsIgnoreCase(sliceUnit, "minutes"))){
			epoch = epoch - lookbackMins.intValue() * 60 * 1000 ; 
		}
		else if((lookbackMins == null) || (StringUtils.isBlank(sliceUnit))){
			epoch =  end.getTime();
		}

		return epoch;
	}

}

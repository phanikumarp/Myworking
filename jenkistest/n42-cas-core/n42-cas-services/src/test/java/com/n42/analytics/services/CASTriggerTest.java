//package com.n42.analytics.services;
//
//import java.util.Date;
//import java.util.LinkedList;
//
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import org.junit.Assert;
//import org.junit.Ignore;
//import org.junit.Test;
//
//public class CASTriggerTest {
//
//	@Ignore
//	@Test
//	public void testTriggerCAS(){
//		try{
//			String sDEString = "{\"service\":{\"canaryId\":\"fdgdf\",\"serviceId\":123456,\"serviceName\":\"haproxy\",\"version1\":{\"appID\":23,\"applicationVersionID\":1,\"serviceVersionId\":1,\"Metrics\":[{\"loadMetric\":{\"metricName\":\"haproxy.requests\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":\"count\",\"time\":\"60\",\"unit\":\"seconds\"}}},{\"otherMetric\":[{\"metricName\":\"haproxy.responsetime\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":null,\"time\":\"60\",\"unit\":\"seconds\"}},{\"metricName\":\"mem.usage.percent\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":\"rate\",\"time\":\"60\",\"unit\":\"seconds\"}},{\"metricName\":\"proc.cpu.util\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":\"rate\",\"time\":\"60\",\"unit\":\"seconds\"}}]}]},\"version2\":{\"appID\":23,\"applicationVersionID\":2,\"serviceVersionId\":2,\"Metrics\":[{\"loadMetric\":{\"metricName\":\"haproxy.requests\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":\"count\",\"time\":\"60\",\"unit\":\"seconds\"}}},{\"otherMetric\":[{\"metricName\":\"haproxy.responsetime\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":null,\"time\":\"60\",\"unit\":\"seconds\"}},{\"metricName\":\"mem.usage.percent\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":\"rate\",\"time\":\"60\",\"unit\":\"seconds\"}},{\"metricName\":\"proc.cpu.util\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":\"rate\",\"time\":\"60\",\"unit\":\"seconds\"}}]}]}}}";
//			CASTrigger triggerCAS = new CASTrigger();
//			JSONParser parser = new JSONParser();
//			JSONObject sDEJSON =  (JSONObject) parser.parse(sDEString);
//			int timeDuration = 1;
//			String durationUnit = "seconds";
//			int timeSlice = 5;
//			String sliceUnit = "minutes";
//			long startExpected = 0 ;
//			long endExpected = 0;
//			int maxLoopCount = triggerCAS.maxLoopCountCalculator(timeDuration, durationUnit, timeSlice, sliceUnit);
//			for (int i = 0; i < maxLoopCount; i++) {
//				startExpected = triggerCAS.timeIncrementorByTimeSlice(new Date(), (Integer) null, null);
//				endExpected = triggerCAS.timeIncrementorByTimeSlice(new Date(), timeSlice, sliceUnit);
//			}
//			JSONObject triggerObj =  triggerCAS.triggerCAS(sDEJSON, timeDuration, durationUnit, timeSlice, sliceUnit);
//			long startActual = 0 ;
//			long endActual = 0 ;
//			if(sDEJSON.get("service")!= null){
//				JSONObject serviceObj = (JSONObject) sDEJSON.get("service") ; 
//				startActual = (long) serviceObj.get("startTime");
//				endActual = (long) serviceObj.get("endTime");
//			}
//
//			//Assert.AssertEquals(startActual, endActual);
//
//		} 
//		catch(ParseException e){
//			System.out.println("Not a valid Json String:");
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void testMaxLoopCountCalculator(){
//
//		CASTrigger triggerCAS = new CASTrigger();
//
//		int timeDuration = 1;
//		String durationUnit = "minutes";
//		int timeSlice = 5;
//		String sliceUnit = "seconds";
//
//		int expectedMaxLoopCount = 12 ; 
//		int actualMaxLoopCount = triggerCAS.maxLoopCountCalculator(timeDuration, durationUnit, timeSlice, sliceUnit);
//
//		Assert.assertEquals(expectedMaxLoopCount, actualMaxLoopCount);
//
//		timeDuration = 1;
//		durationUnit = "days";
//		timeSlice = 5;
//		sliceUnit = "hours";
//
//		expectedMaxLoopCount = 4 ; 
//		actualMaxLoopCount = triggerCAS.maxLoopCountCalculator(timeDuration, durationUnit, timeSlice, sliceUnit);
//
//		Assert.assertEquals(expectedMaxLoopCount, actualMaxLoopCount);
//
//	}
//
//	@Test
//	public void testTimeConversionToEpoch(){
//
//		CASTrigger triggerCAS = new CASTrigger();
//
//		//test 1
//
//		Date test = new Date();
//		Integer timeSlice = 5;
//		String sliceUnit = "seconds";
//
//		long expectedTime = test.getTime() + 5 * 1000;
//		long actualTime = triggerCAS.timeIncrementorByTimeSlice(test, timeSlice, sliceUnit);
//
//		Assert.assertEquals(expectedTime, actualTime);
//
//		//test 2
//		test = new Date();
//		timeSlice = 10;
//		sliceUnit = "minutes";
//
//		expectedTime = test.getTime() + 10 * 60 * 1000 ;
//		actualTime = triggerCAS.timeIncrementorByTimeSlice(test, timeSlice, sliceUnit);
//
//		Assert.assertEquals(expectedTime, actualTime);
//
//	}
//
//
//	@Test
//	public void testSleep(){
//
//		CASTrigger triggerCAS = new CASTrigger();
//
//		//test 1
//		int timeSlice = 5; 
//		String sliceUnit = "seconds" ;
//		long testStart = (new Date()).getSeconds();
//
//		triggerCAS.sleep(timeSlice, sliceUnit);
//
//		int testEnd = (new Date()).getSeconds();
//		testEnd = testEnd - 5;
//
//		Assert.assertEquals(testStart, testEnd);
//
//		//test 2
//		timeSlice = 1; 
//		sliceUnit = "minute" ;
//		testStart = (new Date()).getMinutes();
//
//		triggerCAS.sleep(timeSlice, sliceUnit);
//
//		testEnd = (new Date()).getMinutes();
//		testEnd = testEnd - 1;
//
//		Assert.assertEquals(testStart, testEnd);
//
//	}
//
//	@Test
//	public void testGetVersionIds(){
//
//		CASTrigger triggerCAS = new CASTrigger();
//		try {
//
//			String sDEString = "{\"service\":{\"canaryId\":\"fdgdf\",\"serviceId\":123456,\"serviceName\":\"haproxy\",\"version1\":{\"appID\":23,\"applicationVersionID\":1,\"serviceVersionId\":0,\"Metrics\":[{\"loadMetric\":{\"metricName\":\"haproxy.requests\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":\"count\",\"time\":\"60\",\"unit\":\"seconds\"}}},{\"otherMetric\":[{\"metricName\":\"haproxy.responsetime\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":null,\"time\":\"60\",\"unit\":\"seconds\"}},{\"metricName\":\"mem.usage.percent\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":\"rate\",\"time\":\"60\",\"unit\":\"seconds\"}},{\"metricName\":\"proc.cpu.util\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":\"rate\",\"time\":\"60\",\"unit\":\"seconds\"}}]}]},\"version2\":{\"appID\":23,\"applicationVersionID\":2,\"serviceVersionId\":1,\"Metrics\":[{\"loadMetric\":{\"metricName\":\"haproxy.requests\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":\"count\",\"time\":\"60\",\"unit\":\"seconds\"}}},{\"otherMetric\":[{\"metricName\":\"haproxy.responsetime\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":null,\"time\":\"60\",\"unit\":\"seconds\"}},{\"metricName\":\"mem.usage.percent\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":\"rate\",\"time\":\"60\",\"unit\":\"seconds\"}},{\"metricName\":\"proc.cpu.util\",\"tagsData\":[{\"tagName\":\"host\",\"tagValues\":[{\"tagValue1\":\"ff1eb6ea700d\"},{\"tagValue2\":\"ff1eb6ea700d\"},{\"tagValue3\":\"ff1eb6ea700d\"}]}],\"aggregator\":{\"Function\":\"rate\",\"time\":\"60\",\"unit\":\"seconds\"}}]}]}}}";
//			
//			JSONParser parser = new JSONParser();
//			JSONObject sDEJSON;
//			sDEJSON = (JSONObject) parser.parse(sDEString);
//
//			int expectedVersionId1 = 0;
//			int expectedVersionId2 = 1;
//
//			LinkedList<Integer> versionIds = triggerCAS.getVersionIds(sDEJSON);			
//			int actualVersionId1 = versionIds.get(0).intValue();
//			int actualVersionId2 = versionIds.get(1).intValue();
//
//			Assert.assertEquals(expectedVersionId1, actualVersionId1);
//			Assert.assertEquals(expectedVersionId2, actualVersionId2);
//			
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//}

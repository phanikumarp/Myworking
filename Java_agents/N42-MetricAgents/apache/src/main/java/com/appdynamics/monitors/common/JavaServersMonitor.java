package com.appdynamics.monitors.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.EnvPropertyWriter;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;

public abstract class JavaServersMonitor extends AManagedMonitor
{
  protected final Logger logger = Logger.getLogger(JavaServersMonitor.class);
  protected volatile String host;
  protected volatile String port;
  protected volatile String userName;
  protected volatile String passwd;
  protected volatile String serverRoot = "C:/Program Files/Apache Software Foundation/Apache2.2";
  protected volatile String restartAllowed = "TRUE";
  protected volatile String customURLPath = "/server-status?auto";
  protected volatile String metricPrefix = "Custom Metrics|WebServer|Apache|Status|";
  protected volatile Map<String, String> oldValueMap;
  protected volatile Map<String, String> valueMap;
  protected volatile long oldTime = 0L;
  protected volatile long currentTime = 0L;
  protected String proxyHost;
  protected String proxyPort;
  
  public abstract TaskOutput execute(Map<String, String> paramMap, TaskExecutionContext paramTaskExecutionContext)
    throws TaskExecutionException;
  
  protected void parseArgs(Map<String, String> args)
  {
    host = getArg(args, "host", "175.126.103.47");
    userName = getArg(args, "user", "root");
    passwd = getArg(args, "password", "son123!");
    port = getArg(args, "port", "80");
    customURLPath = getArg(args, "custom-url-path", customURLPath);
    serverRoot = getArg(args, "serverRoot", serverRoot);
    restartAllowed = getArg(args, "restartAllowed", restartAllowed);
    metricPrefix = getArg(args, "metric-prefix", metricPrefix);
    
    proxyHost = getArg(args, "proxy-host", proxyHost);
    proxyPort = getArg(args, "proxy-port", proxyPort);
  }
  
  protected String getArg(Map<String, String> args, String arg, String oldVal)
  {
    String result = (String)args.get(arg);
    if (result == null) {
      return oldVal;
    }
    return result;
  }
  
  protected void printStringMetric(String name, String value)
  {
    String metricName = getMetricPrefix() + name;
    EnvPropertyWriter writer = getPropertyWriter();
    writer.printEnvironmentProperty(metricName, value);
    if (logger.isDebugEnabled()) {
      logger.debug("METRIC STRING:  NAME:" + name + " VALUE:" + value);
    }
  }
  
  public static List<String[]> metricslist = null;
  
  protected void printMetric(String name, String value, String aggType, String timeRollup, String clusterRollup)
  {
    if (metricslist== null) {
      metricslist = new ArrayList<String[]>();
    }
    String metricName = getMetricPrefix() + name;
    MetricWriter metricWriter = getMetricWriter(metricName, aggType,timeRollup, clusterRollup);
    metricWriter.printMetric(value);
    if (logger.isDebugEnabled())
    {
      logger.debug("METRIC:  NAME:" + metricName + " VALUE:" + value + " :" + aggType + ":" + timeRollup + ":" + clusterRollup);
    }
    String[] temp = { name, value, aggType, timeRollup, clusterRollup };
    metricslist.add(temp);
   
  }
  
  protected String getMetricPrefix()
  {
    return "";
  }
  
  protected void startExecute(Map<String, String> taskArguments, TaskExecutionContext taskContext)
  {
    valueMap = Collections.synchronizedMap(new HashMap<String, String>());
    parseArgs(taskArguments);
  }
  
  protected TaskOutput finishExecute()
  {
    oldValueMap = valueMap;
    oldTime = currentTime;
    if (logger.isDebugEnabled())
    {
      logger.info("Finished METRIC COLLECTION for Monitor.......");
    }
    return new TaskOutput("Success");
  }
  
  protected String getString(float num)
  {
    int result = Math.round(num);
    return Integer.toString(result);
  }
  
  protected String getString(String key)
  {
    return getString(key, true);
  }
  
  protected String getString(String key, boolean convertUpper)
  {
    if (convertUpper) {
      key = key.toUpperCase();
    }
    String strResult = (String)valueMap.get(key);
    if (strResult == null) {
      return "0";
    }
    float result = Float.valueOf(strResult).floatValue();
    String resultStr = getString(result);
    return resultStr;
  }
  
  protected String getPercent(String numerator, String denumerator)
  {
    float tmpResult = 0.0F;
    try
    {
      tmpResult = getValue(numerator) / getValue(denumerator);
    }
    catch (Exception e)
    {
      return null;
    }
    tmpResult *= 100.0F;
    return getString(tmpResult);
  }
  
  protected String getReversePercent(float numerator, float denumerator)
  {
    if (denumerator == 0.0F) {
      return null;
    }
    float tmpResult = numerator / denumerator;
    tmpResult = 1.0F - tmpResult;
    tmpResult *= 100.0F;
    return getString(tmpResult);
  }
  
  protected String getPercent(float numerator, float denumerator)
  {
    if (denumerator == 0.0F) {
      return getString(0.0F);
    }
    float tmpResult = numerator / denumerator;
    tmpResult *= 100.0F;
    return getString(tmpResult);
  }
  
  protected float getValue(String key)
  {
    String strResult = (String)valueMap.get(key.toUpperCase());
    if (strResult == null) {
      return 0.0F;
    }
    float result = Float.valueOf(strResult).floatValue();
    return result;
  }
  
  protected float getDiffValue(String key)
  {
    String strResult = (String)valueMap.get(key.toUpperCase());
    if (strResult == null) {
      return 0.0F;
    }
    float result = Float.valueOf(strResult).floatValue();
    float oldResult = 0.0F;
    
    String oldResultStr = (String)oldValueMap.get(key.toUpperCase());
    if (oldResultStr != null) {
      oldResult = Float.valueOf(oldResultStr).floatValue();
    }
    return result;
  }
  
  protected float getRateValue(String key) {
	  String strResult = (String)valueMap.get(key.toUpperCase());
	    if (strResult == null) {
	      return 0.0F;
	    }
	    float result = Float.valueOf(strResult).floatValue();
	    float oldResult = 0.0F;
	    
	    String oldResultStr = (String)oldValueMap.get(key.toUpperCase());
	    if (oldResultStr != null) {
	      oldResult = Float.valueOf(oldResultStr).floatValue();
	    }
	    float finalResult;
	    if(oldTime == 0L || (currentTime - oldTime) <= 0) {
	    	finalResult = 0;
	    	
	    }
	//    System.out.println();
	    else {
	    	finalResult = (result - oldResult) / ((currentTime - oldTime) / 1000L);
	    }
	    return finalResult;
	}

}

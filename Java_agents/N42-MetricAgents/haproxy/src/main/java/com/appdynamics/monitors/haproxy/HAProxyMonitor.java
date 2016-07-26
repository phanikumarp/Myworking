package com.appdynamics.monitors.haproxy;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import au.com.bytecode.opencsv.CSVWriter;

import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import com.singularity.ee.util.httpclient.HttpClientWrapper;
import com.singularity.ee.util.httpclient.HttpExecutionRequest;
import com.singularity.ee.util.httpclient.HttpExecutionResponse;
import com.singularity.ee.util.httpclient.HttpOperation;
import com.singularity.ee.util.httpclient.IHttpClientWrapper;
import com.singularity.ee.util.log4j.Log4JLogger;


public class HAProxyMonitor extends AManagedMonitor
{
  private static Logger logger = Logger.getLogger(HAProxyMonitor.class.getName());
  @SuppressWarnings("unused")
  private static final String metricPathPrefix = "Custom Metrics|HAProxy|";
  private WritableWorkbook workbook;
  private Map<String, Integer> dictionary;
  private String responseString;
  private List<String> proxiesListedInConfigFile = new ArrayList<String>();
  
  public HAProxyMonitor()
  {
    dictionary = new HashMap<String, Integer>();
    dictionary.put("# pxname", Integer.valueOf(0));
    dictionary.put("svname", Integer.valueOf(1));
    dictionary.put("qcur", Integer.valueOf(2));
    dictionary.put("scur", Integer.valueOf(4));
    dictionary.put("stot", Integer.valueOf(7));
    dictionary.put("bin", Integer.valueOf(8));
    dictionary.put("bout", Integer.valueOf(9));
    dictionary.put("ereq", Integer.valueOf(12));
    dictionary.put("econ", Integer.valueOf(13));
    dictionary.put("eresp", Integer.valueOf(14));
    dictionary.put("status", Integer.valueOf(17));
    dictionary.put("act", Integer.valueOf(19));
    dictionary.put("bck", Integer.valueOf(20));
  }
  
@SuppressWarnings("unchecked")
public TaskOutput execute(Map<String, String> taskArguments, TaskExecutionContext arg1)throws TaskExecutionException
  {
    try
    {
      connect(taskArguments);    
      writeResponseToWorkbook(responseString);
      Map<Integer, String> proxiesToBeMonitored = getAllProxyAndTypes(((Integer)dictionary.get("# pxname")).intValue());
      Map<Integer, String> proxyTypes = getAllProxyAndTypes(((Integer)dictionary.get("svname")).intValue());
      if (proxiesListedInConfigFile.size() != 0)
      {
        Iterator<Map.Entry<Integer, String>> it = proxiesToBeMonitored.entrySet().iterator();
         while (it.hasNext())
         {
          Entry<Integer, String> entry = (Entry<Integer, String>)it.next();
          if (!proxiesListedInConfigFile.contains(entry.getValue())) {
            it.remove();
          }
        }
      }
      Map.Entry<Integer, String> entry = (Entry<Integer, String>) proxiesToBeMonitored.entrySet().iterator();
       while (((Iterator<Entry<Integer, String>>) entry).hasNext())
        {
        @SuppressWarnings("rawtypes")
		 Map.Entry<Integer, String> proxy = (Map.Entry)((Iterator<Entry<Integer, String>>) entry).next();
         printMetric(getMetricPrefix() + (String)proxy.getValue() + "|" + 
          (String)proxyTypes.get(proxy.getKey()) + "|", "status",getStatus(((Integer)proxy.getKey()).intValue()), 
          "AVERAGE", 
          "AVERAGE", 
          "COLLECTIVE");
        
        printMetric(proxy, proxyTypes, "qcur", "queued_requests");
        printMetric(proxy, proxyTypes, "scur", "current sessions");
        printMetric(proxy, proxyTypes, "stot", "total sessions");
        printMetric(proxy, proxyTypes, "bin", "bytes in");
        printMetric(proxy, proxyTypes, "bout", "bytes out");
        printMetric(proxy, proxyTypes, "ereq", "error requests");
        printMetric(proxy, proxyTypes, "econ", "connection errors");
        printMetric(proxy, proxyTypes, "eresp", "response errors");
        printMetric(proxy, proxyTypes, "act", "active servers");
        printMetric(proxy, proxyTypes, "bck", "backup servers");
      }
      return new TaskOutput("ActiveMQ Metric Upload Complete");
    }
    catch (Exception e)
    {
      logger.error("ActiveMQ Metric upload failed");
    }
    return new TaskOutput("ActiveMQ Metric upload failed");
  }
  
  private void connect(Map<String, String> taskArguments)
  {
    if ((!taskArguments.containsKey("url")) || 
      (!taskArguments.containsKey("username")) || 
      (!taskArguments.containsKey("password")))
    {
      logger.error("Monitor.xml needs to contain all required task arguments");
      throw new RuntimeException("Monitor.xml needs to contain all required task arguments");
    }
    String url = (String)taskArguments.get("url");
    try
    {
      URL aurl = new URL(url);
      String host = aurl.getHost();
      String userName = (String)taskArguments.get("username");
      String password = (String)taskArguments.get("password");
      if ((url != null) && (url != "")) {
        responseString = getResponseString(url, host, userName,password);        
      }
     
      logger.error("Connected to " + url);
    }
    catch (MalformedURLException e)
    {
      logger.error("URL null or empty in monitor.xml");
      throw new RuntimeException("URL null or empty in monitor.xml");
    }
    if ((taskArguments.containsKey("proxynames")) &&  (taskArguments.get("proxynames") != null) && 
      (!((String)taskArguments.get("proxynames")).equals(""))) {
      proxiesListedInConfigFile = Arrays.asList(((String)taskArguments.get("proxynames")).split(","));
    }
  }
  
  public String getResponseString(String url, String host, String username, String password)
  {
	  IHttpClientWrapper httpClient = null;
    try
    {
      httpClient = HttpClientWrapper.getInstance();
      HttpExecutionRequest request = new HttpExecutionRequest(url, "",HttpOperation.GET);
      httpClient.authenticateHost(host, 80, "", username, password, true);
      HttpExecutionResponse response = httpClient.executeHttpOperation(request, new Log4JLogger(logger));
      responseString = response.getResponseBody();
    }
    catch (Exception ex)
    {	
      ex.printStackTrace();
    }
    finally {
      // httpClient.shutdown(); // don't uncomment ,won't work 
    }
    return responseString;
  }
  
  private void writeResponseToWorkbook(String responseString) throws Exception
  {
    try(OutputStream outputStream = new ByteArrayOutputStream();) {
      
      outputStream.write(responseString.getBytes());
      workbook = Workbook.createWorkbook(outputStream);
      
      WritableSheet sheet = workbook.createSheet("First Sheet", 0);
      BufferedReader reader = new BufferedReader(new StringReader(responseString));
      int j = 0;
      String line;
      while ((line = reader.readLine()) != null)
       {
	        Pattern p = Pattern.compile(",");
	        String[] result = p.split(line);
	        for (int i = 0; i < result.length; i++)
	        {
	          Label label = new Label(i, j, result[i]);
	          sheet.addCell(label);
	        }
	        j++;
       }
       workbook.write();
       workbook.close();
    }
    catch (Exception e)
    {
      logger.error("Error while writing response to workbook stream");
      throw new RuntimeException("Error while writing response to workbook stream");
    }
    finally {
        workbook.close();
    }
  }
  
  private Map<Integer, String> getAllProxyAndTypes(int columnIndex)
  {
    Map<Integer, String> proxiesMap = new HashMap<Integer, String>();
    int rows = getSheet().getRows();
    for (int i = 1; i < rows; i++) {
      proxiesMap.put(Integer.valueOf(i), getSheet().getCell(columnIndex, i).getContents());
    }
    return proxiesMap;
  }
  
  private void printMetric(Map.Entry<Integer, String> proxy, Map<Integer, String> proxyTypes, String metricKey, String metricName)
  {
    if (!getCellContents(((Integer)dictionary.get(metricKey)).intValue(), ((Integer)proxy.getKey()).intValue()).equals("")) {
      printMetric(getMetricPrefix() + (String)proxy.getValue() + "|" + 
    		  	(String)proxyTypes.get(proxy.getKey()) + "|", metricName, 
    		  	 getCellContents(((Integer)dictionary.get(metricKey)).intValue(), ((Integer)proxy.getKey()).intValue()), 
    		  	 "AVERAGE", 
    		  	 "AVERAGE", 
    		  	 "COLLECTIVE");
    }
  }
  
  private void printMetric(String metricPath, String metricName, Object metricValue, String aggregation, String timeRollup, String cluster)
  {
    MetricWriter metricWriter = super.getMetricWriter(metricPath +metricName, aggregation, timeRollup, cluster);
    metricWriter.printMetric(String.valueOf(metricValue));
  }
    
  private String getMetricPrefix()
  {
    return "Custom Metrics|HAProxy|";
  }
    
  private String getStatus(int proxyRowIndex)
  {
    String status = "0";
    if ((getCellContents(((Integer)dictionary.get("status")).intValue(), proxyRowIndex).equals("UP")) || 
    
       (getCellContents(((Integer)dictionary.get("status")).intValue(), proxyRowIndex).equals("OPEN"))) {
        status = "1";
    }
    return status;
  }
  
  private Sheet getSheet()
  {
    return workbook.getSheet(0);
  }
  
  private String getCellContents(int column, int row)
  {
    String contents = getSheet().getCell(column, row).getContents();
    return contents;
  }
  
  public static void main(String[] args) throws Exception
  {
    String csvfile = "config/haproxymetrics/haproxymetrics.csv";
    HAProxyMonitor monitor = new HAProxyMonitor();
    String responseString = monitor.getResponseString("http://175.126.103.46/haproxy?stats;csv", "175.126.103.46", "root", "son123!");
    try(CSVWriter writer = new CSVWriter(new FileWriter(csvfile));) {
      
      String[] metrics = responseString.split("\n");
      for (int i = 0; i < metrics.length; i++) {
        writer.writeNext(metrics[i].split(","));
      }
      writer.close();
    }
    catch (Exception e)
    {
      e.getMessage();
    }
  }

}

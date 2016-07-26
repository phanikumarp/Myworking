package com.N42.MetricsManager.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.appdynamics.monitors.haproxy.HAProxyMonitor;
import au.com.bytecode.opencsv.CSVWriter;

public class HAProxyManager  extends MetricsManager  implements Job
{
  private static Logger logger = Logger.getLogger(HAProxyManager.class);
  private String csvfile = "";
  private int lineCounter = 0;
  JSONArray json = null;
  private BufferedReader csvToRead = null;
  
  	private static final String TAG1 = "host=";
	private static final String TAG2 = "hostname=";
	private static final String TAG3 = "appname=";

	private static final String METRIC_NAME = "haproxy";
	private static Long TIMESTAMP;
  
  public void execute(JobExecutionContext arg0) throws JobExecutionException
  {
	  getMetrics();
	  
	  DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	  Date dateobj = new Date();
	  logger.info("Getting Haproxy Metrics Time :" + df.format(dateobj));		
	  try(Socket  clientSocket = new Socket(getDbHost(), getDBPort());
	      PrintWriter out = (clientSocket.isConnected()) ? new PrintWriter(clientSocket.getOutputStream(), true) : null;)
	    {
	      if (out != null)
	      {   
	        logger.info("Connected with the server : " + clientSocket.getInetAddress().getHostName() + " with port : " + clientSocket.getPort() +"\n");
	        insertMetrics(out);   
	      }
	    }catch (UnknownHostException e) {
			logger.error("Server not connected  due to this exception : "+e);
		} 
		catch (IOException e) {
			logger.error("Server not connected  due to this exception : "+e);
		} 
	    logger.info("End of the Haproxy Metrics \n");
  }
  
  private void getMetrics()
  {
    String csvfile = "config/haproxymetrics/haproxymetrics.csv";
    HAProxyMonitor monitor=new HAProxyMonitor();
    String url = "http://" + getHostIp() + "/haproxy?stats;csv";
    
    String responseString = monitor.getResponseString(url, getHostIp(), getUser(), getPassword());
    try (CSVWriter writer = new CSVWriter(new FileWriter(csvfile));) {
      
      String[] metrics = responseString.split("\n");
      for (int i = 0; i < metrics.length; i++) {
         writer.writeNext(metrics[i].split(","));
      }
      writer.close();
      logger.debug(responseString);
    }
    catch (Exception e)
    {
      e.getMessage();
    }
  }
  
  private void insertMetrics(PrintWriter out)
  {
    try
    {
      csvfile = "config/haproxymetrics/haproxymetrics.csv";
      if (csvfile != null)
      {
        try
        {
          csvToRead = new BufferedReader(new FileReader(csvfile));          
          String[] keys = null;          
          String line;
          String tag;
          StringBuilder putCommand;
          while ((line = csvToRead.readLine()) != null)
          {
	            if (lineCounter == 0)
	            {
	              keys = line.replace("#", "_").split(",");
	              logger.debug("length of keys :" + keys.length);
	              
	            }
	            else if(lineCounter==4) // for sending last row only
	            {
	              String[] values = line.replace("#", "_").split(",");
	              for (int j = 2; j < keys.length; j++)
	              {
	            	  tag =   keys[j].replaceAll("\"", "").trim();
	            	 
	            	  String value = values[j].replaceAll("\"", "").trim();
		                if ((value.equals("")) || (value.length() <= 0)) {
		                     value = "0.0";
		                }	                
		                TIMESTAMP = Long.valueOf(Long.valueOf(System.currentTimeMillis()).longValue() / 1000L);  //  for milliseconds to seconds     
						      putCommand = new StringBuilder();
						   if( !tag.equalsIgnoreCase("status")){	
				     			putCommand.append("put"+" ");
								putCommand.append(METRIC_NAME);
								putCommand.append("." + tag + " ");
								putCommand.append(TIMESTAMP + " ");
								putCommand.append(value + " ");
								putCommand.append(TAG3 + getAppName() + " ");	
								putCommand.append(TAG2 + getHostName() + " ");
								putCommand.append(TAG1 + getHostIp() +  "\n");
								out.write(putCommand.toString());
								logger.info(putCommand.toString());
								out.flush(); 
						   }
	                }
	             }
	            lineCounter += 1;
	          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}

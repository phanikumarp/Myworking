package com.N42.MetricsManager.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.appdynamics.monitors.apache.ApacheStatusMonitor;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;

public class ApacheManager extends MetricsManager implements Job {
	static Logger logger = Logger.getLogger(ApacheManager.class.getName());
	private String csvfile = "";
	@SuppressWarnings("unused")
	private int lineCounter = 0;
//	private BufferedReader csvToRead = null;

	private static final String TAG1 = "host=";
	private static final String TAG2 = "hostname=";
	private static final String TAG3 = "appname=";
	
	private static final String METRIC_NAME = "apache";
	private static Long TIMESTAMP;

	 public void execute(JobExecutionContext arg0) throws JobExecutionException
	  {
		  try {
			getMetrics();
		} catch (ConfigurationException ex) {
			ex.printStackTrace();
		}
		  
		  DateFormat df = new SimpleDateFormat("dd/MM/YY HH:mm:ss");
		  Date dateobj = new Date();
		  logger.info("Getting Apache Metrics Time :" + df.format(dateobj));		
			try(Socket  clientSocket = new Socket(getDBHost(), getDBPort());PrintWriter out = (clientSocket.isConnected()) ? new PrintWriter(clientSocket.getOutputStream(), true) : null;)
		    {
		      if (out != null)
		      {
		        
		        logger.info("Connected with the server : " + clientSocket.getInetAddress().getHostName() +  " with port : " + clientSocket.getPort() +"\n");
		        insertMetrics(out);
		        
		      }
		  
		    }catch (UnknownHostException e) {
				logger.error("Server not connected  due to this exception : "+e);
			} 
			catch (IOException e) {
				logger.error("Server not connected  due to this exception : "+e);
			} 
		    logger.info("End of the Apache Metrics \n");
	  }

	  public static Map<String, String> oldValueMap;
	  public static Map<String, String> valueMap;
	  public static long oldTime = 0L;
	  public static long currentTime = 0L;
	 
	private void getMetrics() throws ConfigurationException {
		Map<String, String> taskArguments = new HashMap<String, String>();
		try {
			taskArguments.put("host", getHostIp());
			taskArguments.put("port", getPort().toString());
			taskArguments.put("user", getUsername());
			taskArguments.put("password", getPassword());
			taskArguments.put("csvfile",
					"config/apachemetrics/apachemetrics.csv");
			taskArguments.put("custom-url-path", "/server-status?auto");
			taskArguments.put("serverRoot",
					"C:/Program Files/Apache Software Foundation/Apache2.2");
		} catch (Exception e) {
			throw new ConfigurationException(
					"'Host_details' could not be found in the 'apachesettings.json' configuration file");
		}
		ApacheStatusMonitor monitor = new ApacheStatusMonitor(oldValueMap, oldTime);

		TaskExecutionContext taskContext = null;
		try {
			monitor.execute(taskArguments, taskContext);
			
			oldValueMap = monitor.getOldValueMap();
			oldTime = monitor.getOldTime();
			
		} catch (Exception localException) {
			logger.info("Monitor exception " + localException);
		}
	}

	private void insertMetrics(PrintWriter out){
		try {
			csvfile = "config/apachemetrics/apachemetrics.csv";
			if (csvfile != null) {
				try(BufferedReader csvToRead = new BufferedReader(new FileReader(csvfile));) {

					String[] rows = null;
					String line;
					StringBuilder putCommand;
					while ((line = csvToRead.readLine()) != null) {

						rows = line.replace("#", "_").split(",");
						String apachemetric = rows[0].replace("|", "_")	.replace(" ", "_").replace("(", "").replace(")", "").replace("\"", "");
						
						TIMESTAMP = Long.valueOf(Long.valueOf(System.currentTimeMillis()).longValue() / 1000L);  //  for milliseconds to seconds    
					       putCommand = new StringBuilder();
			     			putCommand.append("put"+" ");
							putCommand.append(METRIC_NAME);
							putCommand.append("." + apachemetric + " ");
							putCommand.append(TIMESTAMP + " ");
							putCommand.append(Double.parseDouble(rows[1].replace("\"", "")) + " ");
							putCommand.append(TAG3 + getAppName() + " ");	
							putCommand.append(TAG2 + getHostName() + " ");
							putCommand.append(TAG1 + getHostIp() +  "\n");
							out.write(putCommand.toString());
						//	System.out.println(putCommand.toString());
							logger.info(putCommand.toString());
							out.flush(); 
						//	out.close();
						lineCounter += 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

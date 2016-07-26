package com.appdynamics.monitors.apache;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import au.com.bytecode.opencsv.CSVWriter;

import com.appdynamics.monitors.common.JavaServersMonitor;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;

public class ApacheStatusMonitor extends JavaServersMonitor {
	static Logger logger = Logger.getLogger(ApacheStatusMonitor.class);
	String url = "";
	int port = 80;
	//	String csvFile = "";

	public ApacheStatusMonitor() {
		oldValueMap = new HashMap<String, String>();
	}

	public ApacheStatusMonitor(Map<String, String> oldValueMap, long oldTime) {
		if(oldValueMap == null) {
			this.oldValueMap = new HashMap<String, String>();
		}
		else {
			this.oldValueMap = oldValueMap;
		}
		
		    this.oldTime = oldTime;
	}

	public TaskOutput execute(Map<String, String> taskArguments,TaskExecutionContext taskContext) throws TaskExecutionException {
		startExecute(taskArguments, taskContext);

		try {
			port = Integer.parseInt(((String) taskArguments.get("port")).toLowerCase());
			url = taskArguments.get("host").toString();
			populate(valueMap);

		} catch (IOException e) {
			throw new TaskExecutionException(e);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if (logger.isDebugEnabled()) {
			logger.debug("Starting METRIC COLLECTION for Apache Monitor.......");
		}
		// Availability
		printMetric("Availability|up", getString(1),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_SUM,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		// printStringMetric("Availability|Server Uptime (sec)",
		// getString("Uptime"));
		printMetric("Availability|Server Uptime (sec)", getString("Uptime"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_INDIVIDUAL);

		// RESOURCE UTILIZATION

		printMetric("Resource Utilization|CPU|Load", getString("CPULoad"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Resource Utilization|Processes|Busy Workers",
				getString("BusyWorkers"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Resource Utilization|Processes|Idle Workers",
				getString("IdleWorkers"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		// Activity
		printMetric("Activity|Total Accesses",
				getString(getDiffValue("Total Accesses")),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_SUM,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Activity|Total Traffic",
				getString(getDiffValue("Total kBytes")),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_SUM,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);
		
		printMetric("Activity|Total Traffic|Rate",
				getString(getRateValue("Total kBytes")),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_INDIVIDUAL);

		printMetric("Activity|Requests/min", getString("ReqPerMin"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Activity|Bytes/min", getString("BytesPerMin"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Activity|Bytes/req",
				getString(getDiffValue("BytesPerReq")),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Activity|Type|Starting Up", getString("StartingUp"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Activity|Type|Reading Request",
				getString("ReadingRequest"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Activity|Type|Sending Reply", getString("SendingReply"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Activity|Type|Keep Alive", getString("KeepAlive"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Activity|Type|DNS Lookup", getString("DNSLookup"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Activity|Type|Closing Connection",
				getString("ClosingConnection"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Activity|Type|Logging", getString("Logging"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Activity|Type|Gracefully Finishing",
				getString("GracefullyFinishing"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);

		printMetric("Activity|Type|Cleaning Up", getString("CleaningUp"),
				MetricWriter.METRIC_AGGREGATION_TYPE_OBSERVATION,
				MetricWriter.METRIC_TIME_ROLLUP_TYPE_CURRENT,
				MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE);
		String csvfile = taskArguments.get("csvfile").toString();
		List<String[]> metrics = JavaServersMonitor.metricslist;
		try (CSVWriter writer = new CSVWriter(new FileWriter(csvfile));){

			for (Iterator<String[]> i = metrics.iterator(); i.hasNext();) {
				String[] row = i.next();
				writer.writeNext(row);
			}
			metrics.clear();
			writer.close();
		} catch (Exception e) {
			e.getMessage();
		}


		//return finishExecute();
		   return new TaskOutput("Success");
	}

	protected void populate(Map<String, String> valueMap)throws InstantiationException, IllegalAccessException,	ClassNotFoundException, IOException {
		HttpClient httpClient = new HttpClient();
		if ((proxyHost != null) && (proxyHost.length() > 0)	&& (proxyPort != null) && (proxyPort.length() > 0)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Usinxy server " + proxyHost + ":" + proxyPort);
			}
			httpClient.getHostConfiguration().setProxy(proxyHost,Integer.valueOf(proxyPort).intValue());
		}
		String connStr = "http://" + url + ":" + port + customURLPath;
System.out.println("url:" + connStr);
		GetMethod get = new GetMethod(connStr);

		InputStream responseStream = null;
		try {
			int responseCode = httpClient.executeMethod(get);
			responseStream = get.getResponseBodyAsStream();
			logger.debug("Response code : " + responseCode + " Response length: " + get.getResponseContentLength());
		} catch (IOException e) {
			logger.error("Failed to execute HTTP request to URL " + connStr	+ ". Got error" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		currentTime = System.currentTimeMillis();

		BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
		String line;
		while ((line = reader.readLine()) != null) {
			Pattern p = Pattern.compile(":", 16);
			String[] result = p.split(line);
			if (result.length == 2) {
				String key = result[0];
				String value = result[1];
				if (key.equals("Scoreboard")) {
					parseScoreboard(value);
				} else {
					if (key.equals("BytesPerSec")) {
						key = "BytesPerMin";
						float floatVal = Float.valueOf(value).floatValue();
						value = getString(floatVal * 60.0F);
					}
					if (key.equals("ReqPerSec")) {
						key = "ReqPerMin";
						float floatVal = Float.valueOf(value).floatValue();
						value = getString(floatVal * 60.0F);
					}
					valueMap.put(key.toUpperCase(), value);
				}
			}
		}
		get.releaseConnection();
	}

	protected void parseScoreboard(String value) throws IOException {
		int CleaningUp;
		int GracefullyFinishing;
		int Logging;
		int ClosingConnection;
		int DNSLookup;
		int KeepAlive;
		int SendingReply;
		int ReadingRequest;
		int StartingUp = ReadingRequest = SendingReply = KeepAlive = DNSLookup = ClosingConnection = Logging = GracefullyFinishing = CleaningUp = 0;

		StringReader reader = new StringReader(value);
		int typeInt;
		while ((typeInt = reader.read()) != -1) {

			char typeChar = (char) typeInt;
			switch (typeChar) {
			case 'I':
				CleaningUp++;
				break;
			case 'C':
				ClosingConnection++;
				break;
			case 'S':
				StartingUp++;
				break;
			case 'R':
				ReadingRequest++;
				break;
			case 'W':
				SendingReply++;
				break;
			case 'K':
				KeepAlive++;
				break;
			case 'D':
				DNSLookup++;
				break;
			case 'L':
				Logging++;
				break;
			case 'G':
				GracefullyFinishing++;
			}
		}
		valueMap.put("STARTINGUP", Integer.toString(StartingUp));
		valueMap.put("READINGREQUEST", Integer.toString(ReadingRequest));
		valueMap.put("SENDINGREPLY", Integer.toString(SendingReply));
		valueMap.put("KEEPALIVE", Integer.toString(KeepAlive));
		valueMap.put("DNSLOOKUP", Integer.toString(DNSLookup));
		valueMap.put("CLOSINGCONNECTION", Integer.toString(ClosingConnection));
		valueMap.put("LOGGING", Integer.toString(Logging));
		valueMap.put("GRACEFULLYFINISHING",Integer.toString(GracefullyFinishing));
		valueMap.put("CLEANINGUP", Integer.toString(CleaningUp));

		reader.close();
	}

	public static void main(String[] args) throws Exception {
		String log4jConfPath = "server/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);

		ApacheStatusMonitor monitor = new ApacheStatusMonitor();
		Map<String, String> taskArguments = new HashMap<String, String>();
		taskArguments.put("175.126.103.47", "80");
		TaskExecutionContext taskContext = null;
		monitor.execute(taskArguments, taskContext);
	}

	public Map<String, String> getOldValueMap() {
		
		return this.valueMap;
	}

	public long getOldTime() {
		
		return this.currentTime;
	}
}

package com.N42.MetricsManager.Main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.simplejmx.client.JmxClient;

public class TomcatMetricManager implements Job {

	private static final Logger logger = Logger.getLogger(TomcatMetricManager.class);

	private static final String METRIC_CONFIG_FILE = "config/metrics.json";
	private static final String TAG1 = "host=";
	private static final String TAG2 = "hostip=";
	private static final String TAG3 = "appname=";

	private static Long TIMESTAMP;
	private static final String METRIC_NAME = "tomcat";

	private String hostName;
	private String hostIp;
	private String appName;
	private int portNumber;
	public String dbHostName;
	public int dbPortNumber;
	private long timeInterval;
	private int jmxPortNumber;
	private String filePath;
	public JmxClient jmxClient = null;
	public JSONArray metricNameJsonArray = null;
	private JSONArray json = null;

	private static boolean IS_FIRST_TIME = true;
	private static Map<String, Long> prevMap = new HashMap<String, Long>();
	private static Map<String, Long> currentMap = new HashMap<String, Long>();

	public TomcatMetricManager() {
		try {
			getEnvironmentConfigs();

		} catch (Exception e) {
			// Since we are unable to get the environment configurations, we
			// cannot recover meaningfully.
			System.exit(1);
		}

		// Read the metric configurations into a JSON array
		if (metricNameJsonArray == null) {
			// JSON not loaded yet; read metric config into JSON
			/*
			 * Read the metrics configuration and load them into JSON once.
			 */
			readMetricConfigsIntoJSON();

			if (metricNameJsonArray == null) {
				/*
				 * Since we are unable to read the metric configuration file, we
				 * cannot recover meaningfully.
				 */
				System.exit(1);
			}
		}
	}

	public void getEnvironmentConfigs() throws ConfigurationException {
		// auto closable connection in java
		try (InputStream inputstream = new FileInputStream(METRIC_CONFIG_FILE);
				InputStreamReader reader = new InputStreamReader(inputstream)) {
			JSONObject data = (JSONObject) JSONValue.parseWithException(reader);
			json = (JSONArray) ((JSONObject) data).get("tomcat");

			for (int i = 0; i < json.size(); i++) {
				JSONObject obj = (JSONObject) json.get(i);

				appName = (String) obj.get("app_name");
				hostName = (String) obj.get("host_name");
				dbHostName = (String) obj.get("kairodb_host");
				dbPortNumber = Integer.parseInt((String) obj.get("kairodb_port"));
				hostIp = (String) obj.get("host_ip");
				portNumber = Integer.parseInt((String) obj.get("port"));
				timeInterval = Long.parseLong((String) obj.get("time_interval"));
				jmxPortNumber = Integer.parseInt((String) obj.get("jmxport"));
				filePath = (String) obj.get("file_path");
			}
		} catch (Exception e) {
			logger.info("Unable to find the configuration file parameters due to this Exception ::" + e);
			throw new ConfigurationException(
					"'KairoDB_details' could not be found in the 'metrics.json' configuration file");
		}
	}

	public void readMetricConfigsIntoJSON() {

		try (InputStream inputstream = new FileInputStream(METRIC_CONFIG_FILE);
				InputStreamReader reader = new InputStreamReader(inputstream)) {
			JSONObject data = (JSONObject) JSONValue.parseWithException(reader);

			if (data == null) {
				logger.error("Unable to read metric configuration files. Invalid JSON?");
			}
			metricNameJsonArray = (JSONArray) ((JSONObject) data).get("metrics");
			if (metricNameJsonArray == null) {
				logger.error("Unable to read metric configuration files. Unexpected JSON format: metrics not found");
			}
		} catch (IOException ioe) {
			logger.error("Unable to read metric configuration files :" + ioe);
		} catch (ParseException pe) {
			logger.error("Unable to read metric configuration files. Invalid JSON?");
		}
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		logger.debug("Getting Metrics Time:" + df.format(dateobj));
		initialize();
        logger.debug("initilization completed");
		try {
			/*
			 * Getting all Tomcat metics with instances
			 */
			if (IS_FIRST_TIME == true) {
				getAllMetricsWithInstance();
			}
			try (Socket clientSocket = new Socket(dbHostName, dbPortNumber);
					PrintWriter out = (clientSocket.isConnected())? new PrintWriter(clientSocket.getOutputStream(), true) : null;) {
				if (out != null) {
					logger.info("Connected with the server : " + clientSocket.getInetAddress().getHostName() + " with port : " + clientSocket.getPort() + "\n");
					collectMetrics(out);
				}
			} catch (UnknownHostException e) {
				logger.error("Server not connected  due to this exception : " + e);
			} catch (IOException e) {
				logger.error("Server not connected  due to this exception : " + e);
			}        
			logger.info("End of the metrics \n");
			IS_FIRST_TIME = false;
		} catch (IOException e) {
			logger.error("Server not connected  due to this exception : " + e);
		}
		finally{
		    closejmx();
		}
	}
     // For Closing jmxClient 
	private void closejmx() {
		
		jmxClient.close();
		
	}

	private void initialize() {
	    
		establishJMXClient();

	}

	public void establishJMXClient() {
		try {
			jmxClient = jmxClient != null ? jmxClient : new JmxClient(hostIp, jmxPortNumber);
			logger.info("Config file parameters  Host :" + hostIp + " | Hostname : " + hostName + " | ServerDb host : "
					+ dbHostName + " | ServerDb port : " + dbPortNumber + " | Jmx port  :" + jmxPortNumber
					+ " and Time interval  :" + timeInterval);
			logger.debug("Config file parameters  Host :" + hostIp + " | Hostname : " + hostName + " | ServerDb host : "
					+ dbHostName + " | ServerDb port : " + dbPortNumber + " | Jmx port  :" + jmxPortNumber
					+ " and Time interval  :" + timeInterval);
			if (jmxClient == null) {
				logger.error("The JMX client object is null; it was not be initialized.");
			}
		} catch (Exception e) {
			logger.error("The JMX client object cannot be created:" + e);
			jmxClient = null;

		}
	}

	private void collectMetrics(PrintWriter out) throws IOException {
		long avgProcessingTime = 0;
		long processingTimeDiff = 0;
		long reqCountDiff = 0;
		String attribute = null;
		Object value = null;
		List<String> procList = new ArrayList<String>();
		List<String> reqList = new ArrayList<String>();
		Map<String, Long> avgMap = new HashMap<String, Long>();
		StringBuilder putCommand;
		logger.info("Started gathering metrics");

		try {

			for (int attributeIndex = 0; attributeIndex < metricNameJsonArray.size(); attributeIndex++) {
				if (attributeIndex == metricNameJsonArray.size() - 1) {

					/*
					 * Final attribute (not in the configuration file) and
					 * calculating avgprocessingTime
					 */

					if (IS_FIRST_TIME == false) {
						for (int i = 0; i < procList.size(); i++) {
							attribute = "avarage_" + procList.get(i);
							processingTimeDiff = currentMap.get(procList.get(i)) - prevMap.get(procList.get(i));
							reqCountDiff = currentMap.get(reqList.get(i)) - prevMap.get(reqList.get(i));
							if (reqCountDiff == 0) {
								avgProcessingTime = 0;
							} else {
								try {
									avgProcessingTime = (processingTimeDiff * 1000) / reqCountDiff;
								} catch (Exception e) {
									logger.error("Error calculating average time :: " + e);
								}
							}
							avgMap.put(attribute, new Long(avgProcessingTime));
						}

						if (avgMap != null) {
							for (Map.Entry<String, Long> entry : avgMap.entrySet()) {
								TIMESTAMP = Long.valueOf(Long.valueOf(System.currentTimeMillis()).longValue() / 1000L);
								putCommand = new StringBuilder();

								putCommand.append("put" + " ");
								putCommand.append(METRIC_NAME);
								putCommand.append("." + entry.getKey() + " ");
								putCommand.append(TIMESTAMP + " ");
								putCommand.append(entry.getValue().toString() + " ");
								putCommand.append(TAG3 + appName + " ");
								putCommand.append(TAG1 + hostName + " ");
								putCommand.append(TAG2 + hostIp + "\n");
						    	out.write(putCommand.toString());
								// System.out.print(putCommand.toString());
								logger.debug(putCommand.toString());
								logger.info("Getting metric data of this parameter :: " + entry.getKey() + " = "+ entry.getValue());
							}

						}
					}
				} else {
					JSONObject obj = (JSONObject) metricNameJsonArray.get(attributeIndex);
					value = getValue(obj.get("jmxObject").toString().replaceAll("xxxx", String.valueOf(portNumber)),
							obj.get("attribute").toString(), obj.get("attribute").toString());
					attribute = obj.get("attribute").toString();
					if (value == null) {
						logger.error("Unable to get the metric data for this parameter :" + attribute + "  and IndexPosition :" + attributeIndex);
						continue;
					}

					Map<String, String> map = new HashMap<String, String>();
					String totalcat = obj.get("jmxObject").toString().replaceAll("xxxx", String.valueOf(portNumber));
					String[] tot = totalcat.split("\\:");

					for (String catElement : tot[1].split("\\,")) {
						map.put(catElement.split("=")[0], catElement.split("=")[1].replace("\"", ""));
					}
					if (attribute.equals("processingTime") && map.containsKey("name")) {
						attribute = obj.get("attribute").toString() + "_" + map.get("name");
						procList.add(attribute);
						currentMap.put(attribute, Long.parseLong(value.toString()));
					}
					if (attribute.equals("requestCount") && map.containsKey("name")) {
						attribute = obj.get("attribute").toString() + "_" + map.get("name");
						reqList.add(attribute);
						currentMap.put(attribute, Long.parseLong(value.toString()));
					}
					if (value == null || value == "") {
						logger.error("Unable to get the metric data for this parameter :" + attribute);
					}
					/*
					 * converting milliseconds to seconds
					 */
					TIMESTAMP = Long.valueOf(Long.valueOf(System.currentTimeMillis()).longValue() / 1000L);
					putCommand = new StringBuilder();

					putCommand.append("put" + " ");
					putCommand.append(METRIC_NAME);
					putCommand.append("." + attribute + " ");
					putCommand.append(TIMESTAMP + " ");
					putCommand.append(value.toString() + " ");
					putCommand.append(TAG3 + appName + " ");
					putCommand.append(TAG1 + hostName + " ");
					putCommand.append(TAG2 + hostIp + "\n");
					out.write(putCommand.toString());
					logger.debug(putCommand.toString());
					// System.out.print(putCommand.toString());
					logger.info("Getting metric data of this parameter :: " + attribute + " = " + value);
				}
			} // end of for loop

			prevMap.putAll(currentMap);
			currentMap.clear();
		} catch (Exception e) {
			logger.error("getMetrics::Unable to get the data due to this exception :" + e);

		} finally {

			out.close();
			metricNameJsonArray = null;
			value = null;
			attribute = null;
			processingTimeDiff = 0;
			reqCountDiff = 0;

		}
	}

	private void getAllMetricsWithInstance() throws IOException {
		File file = null;
		if (filePath == null) {
			file = new File("config/Metrics_instances.txt");
		} else {
			file = new File(filePath);

		}
		BufferedWriter fileout = new BufferedWriter(new FileWriter(file));
		String[] domains;
		String servAttrName = null;
		String sobjName = null;
		try {
			if (jmxClient == null) {
				logger.error("The JMX client object is null; it was not be initialized.");
			}
			fileout.write("<----------Tomcat all instances --------->" + "\n\n");
			domains = jmxClient.getBeanDomains();
			for (int k = 0; k < domains.length; k++) {
				ObjectName[] onames = getObjectsByDomain(domains[k]);
				for (int i = 0; i < onames.length; i++) {
					sobjName = onames[i].getCanonicalName();
					// writing all instances to the file
					fileout.write(sobjName + "\n");
					try {
						ObjectName obN = getObjectName(sobjName);
						MBeanAttributeInfo[] att = jmxClient.getAttributesInfo(obN);
						for (int j = 0; j < att.length; j++) {
							servAttrName = att[j].getName();
							// writing all metrics to the file
							fileout.write("Attribute......:: " + servAttrName + "\n");
						}
					} catch (Exception ex) {
						logger.error("Failed to retrieve : " + sobjName + " exception " + ex.toString());
					}
				}
			}

		} catch (JMException ex) {
			logger.error("Exception due to :" + ex.toString());
		} finally {
			fileout.close();
		}
	}

	private Object getValue(String objName, String attributeName, String attributeKey) throws IOException {
		Object value = null;
		try {
			ObjectName objectName = new ObjectName(objName);
			Object attribute = jmxClient.getAttribute(objectName, attributeName);
			if (attribute == null) {
				logger.error("getValue::unable to getting jmxclient object attribute information");
			}
			if (attribute instanceof CompositeDataSupport) {
				CompositeDataSupport compositeAttr = (CompositeDataSupport) attribute;
				value = compositeAttr.get("used");
			} else {
				value = attribute;

			}
		} catch (Exception e) {
			logger.error("Unable to retrieve metric value for : " + objName + " \n" + e);
		}
		return value;
	}

	private ObjectName[] getObjectsByDomain(String domain) throws IOException {
		try {
			Set<ObjectName> obj = jmxClient.getBeanNames(domain);
			ObjectName[] onames = new ObjectName[obj.size()];
			obj.toArray(onames);
			return onames;
		} catch (Exception ex) {
			logger.error("getObjectsByDomain:unable to getting jmxclient Domain ObjectNames due to exception::"
					+ ex.toString());
		}
		return null;
	}

	
	@SuppressWarnings("unused")
	private ObjectName getObjectName(String objectName)
			throws InstanceNotFoundException, MalformedObjectNameException, IOException {
		ObjectName objName = new ObjectName(objectName);
		String username = null;
		String password = null;
		String serviceUrl = "service:jmx:rmi:///jndi/rmi://" + hostIp + ":" + jmxPortNumber + "/jmxrmi";

		HashMap<String, Object> environment = new HashMap<String, Object>();
		environment.put("jmx.remote.x.client.connection.check.period", 0L);

		JMXServiceURL url = new JMXServiceURL(serviceUrl);
		JMXConnector connector;
		MBeanServerConnection connection;
		if ((username != null) && (password != null)) {
			environment.put(JMXConnector.CREDENTIALS, new String[] { username, password });
			connector = JMXConnectorFactory.connect(url, environment);
		} else {
			connector = JMXConnectorFactory.connect(url, environment);
		}
		try{
		connection = connector.getMBeanServerConnection();
		if ((objName.isPropertyPattern()) || (objName.isDomainPattern())) {
			Set<ObjectInstance> mBeans = connection.queryMBeans(objName, null);
			if (mBeans.size() == 0) {
				throw new InstanceNotFoundException();
			}
			if (mBeans.size() > 1) {
				logger.error("Object name not unique: objectName pattern matches " + mBeans.size() + " MBeans.");

			} else {
				objName = ((ObjectInstance) mBeans.iterator().next()).getObjectName();
				logger.debug("Retrieving jmx object:" + objName);
			}
		}
		}catch (Exception ex) {
			logger.error("MBeanServerConnection due to this exception::" + ex.toString());
		}
		finally{
			connector.close();
			connection=null;
		}
		return objName;
	}

	public long getTimeInterval() {
		return timeInterval;
	}

}
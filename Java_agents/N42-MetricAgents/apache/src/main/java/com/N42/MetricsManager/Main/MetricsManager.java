package com.N42.MetricsManager.Main;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class MetricsManager {
	private static final Logger logger = Logger.getLogger(MetricsManager.class);
	
	private static final String CONFIG_FILE = "config/apachesettings.json";
	private String appName;
	private  String hostName;
	private String hostIp;
	private String dbhostName = "";
	private int portNo;
	public int dbportNo = 0;
	private long interval_time=0;	
	private String username;
	private String password;
	
	JSONArray json = null;
	
	public String getHostName() {
		return hostName;
	}

	public  void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getInterval_time() {
		return interval_time;
	}

	public void setInterval_time(long interval_time) {
		this.interval_time = interval_time;
	}

	public MetricsManager() {
		try {
			setDBValues();
		} catch (Exception ex) {

		}
		//this.dbhostName = getDBHost();
		//this.dbportNo = getDBPort();
		//this.client = new HttpClient(dbhostName, dbportNo);
	}

	
	private void setDBValues() throws ConfigurationException {

		try(InputStream inputstream = new FileInputStream(CONFIG_FILE); 
				InputStreamReader reader = new InputStreamReader(inputstream)) {
			JSONObject data = (JSONObject) JSONValue.parseWithException(reader);
			 json = (JSONArray) ((JSONObject) data).get("apache");
			 
			for (int i = 0; i < json.size(); i++) {
				JSONObject obj = (JSONObject) json.get(i);
				setAppName((String) obj.get("app_name"));
			    setHostName((String) obj.get("host_name"));
			    setHostIp((String) obj.get("host_ip"));
				setDBHost((String) obj.get("kairodb_host"));
				setDBPort(Integer.parseInt((String) obj.get("kairodb_port")));
				setInterval_time(Long.parseLong((String) obj.get("time_interval")));				
				setPort(Integer.parseInt((String) obj.get("port")));
				setUsername((String) obj.get("user"));
				setPassword((String) obj.get("passwd"));
				
			}
		}catch (Exception e) {
			logger.error("unable to find the configuration file parameters due to this Exception::"+e);
			throw new ConfigurationException(
					"'KairoDB_details' could not be found in the 'apachesettings.json' configuration file");
		}
	}

	
	
	
	public Integer getPort() {
		return portNo;
	}
	
	public void setPort(int port) {
		portNo = port;
	}

	public String getDBHost() {
		return dbhostName;
	}
	
	public void setDBHost(String dbhost) {
		dbhostName = dbhost;
	}

	public Integer getDBPort() {
		return dbportNo;
	}
	
	public void setDBPort(int dbport) {
		dbportNo = dbport;
	}

	public boolean isDouble(String value) {
		System.out.println("length:" + value.length());
		try {
			long d = Long.parseLong(value);
			logger.debug("long value:" + d);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}

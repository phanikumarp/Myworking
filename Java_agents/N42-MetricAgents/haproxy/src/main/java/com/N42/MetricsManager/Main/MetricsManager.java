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
	private static final String CONFIG_FILE = "config/haproxysettings.json";
	public String appName;
	public String hostIp;
	public int portNo;
	public String hostName;
	
	public String dbHost = "";
	public int dbportNo = 0;
	public String user;
	public String password;	
	public long time_interval=0;
	
	JSONArray json = null;
	
	public long getTime_interval() {
		return time_interval;
	}

	public void setTime_interval(long time_interval) {
		this.time_interval = time_interval;
	}
	
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MetricsManager() {	
		
		try {
			setDBValues();
			
		} catch (Exception ex) {

		}	
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
	
	public Integer getPort() {
		return portNo;
	}
	
	public void setPort(int port) {
		portNo = port;
	}

	public Integer getDBPort() {
		return dbportNo;
	}
	
	public void setDBPort(int dbport) {
		dbportNo = dbport;
	}

	public boolean isDouble(String value) {	
		//logger.debug("length:" + value.length());
		try {
			long d = Long.parseLong(value);
			logger.debug("long value:" + d);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private void setDBValues() throws ConfigurationException {
			try(InputStream inputstream = new FileInputStream(CONFIG_FILE); 
					InputStreamReader reader = new InputStreamReader(inputstream)) {
				JSONObject data = (JSONObject) JSONValue.parseWithException(reader);
			json = (JSONArray) ((JSONObject) data).get("haproxy");
			for (int i = 0; i < json.size(); i++) {
				JSONObject obj = (JSONObject) json.get(i);
				setAppName((String) obj.get("app_name"));
				setDbHost((String) obj.get("kairodb_host"));
				setDBPort(Integer.parseInt((String) obj.get("kairodb_port")));
				setUser((String) obj.get("user"));
				setPassword((String) obj.get("passwd"));
				setHostIp((String) obj.get("host_ip"));
				setPort(Integer.parseInt((String) obj.get("port")));
				setHostName((String) obj.get("host_name"));
				setTime_interval(Long.parseLong((String) obj.get("time_interval")));
			}
		} catch (Exception e) {
			throw new ConfigurationException(
					"'ServerDB_details' could not be found in the 'apachesettings.json' configuration file");
		}
	}
}

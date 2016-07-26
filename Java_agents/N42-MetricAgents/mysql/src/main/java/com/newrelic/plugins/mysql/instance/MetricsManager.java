package com.newrelic.plugins.mysql.instance;


public class MetricsManager {
	public String hostName;
	public String hostIp;
	public int portNo;
	
	public String dbhostName = "175.126.103.50";
	public int dbportNo = 4343;

	public MetricsManager(String hostIp, int port) {
		this.hostIp = hostIp;
		this.portNo = port;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public String getHostIp() {
		return hostIp;
	}

	public Integer getPort() {
		return portNo;
	}

	public String getDBHost() {
		return dbhostName;
	}

	public Integer getDBPort() {
		return dbportNo;
	}

	public boolean isDouble(String value) {
		System.out.println("length:" + value.length());
		try {

//			long d = Long.parseLong(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}

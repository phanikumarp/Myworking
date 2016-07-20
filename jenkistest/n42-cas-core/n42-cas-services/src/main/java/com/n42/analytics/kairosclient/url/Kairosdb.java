package com.n42.analytics.kairosclient.url;

import org.apache.commons.lang3.math.NumberUtils;

import com.n42.analytics.util.N42Properties;

public interface Kairosdb {
	
	public String HOST = N42Properties.getInstance().getProperty("kairos.host");
	//public String HOST = "175.126.103.50";
	public Integer PORT = NumberUtils.toInt(N42Properties.getInstance().getProperty("kairos.port"));
	public String URL = "http://" + HOST + ":" + PORT;
}
package com.n42.analytics.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class N42Properties {
	
	private static Properties properties; // Singleton instance
	
	private N42Properties() {}  // Dont instantiate a singleton class
	
	public synchronized static Properties getInstance(){
		if(properties == null){
			properties = new Properties();
			loadProps();
		}
		
		return properties;
	}

	private static void loadProps() {
		try(InputStream in = N42Properties.class.getClassLoader().getResourceAsStream("n42.properties");) {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

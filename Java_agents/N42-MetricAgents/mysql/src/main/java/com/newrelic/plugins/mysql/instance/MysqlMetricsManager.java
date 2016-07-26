package com.newrelic.plugins.mysql.instance;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.newrelic.metrics.publish.Runner;
import com.newrelic.metrics.publish.configuration.ConfigurationException;

/**
 * This is the main calling class for a New Relic Agent. This class sets up the
 * necessary agents from the provided configuration and runs these indefinitely.
 * 
 * @author Ronald Bradford me@ronaldbradford.com
 * 
 */
public class MysqlMetricsManager {
	private static final Logger logger = Logger.getLogger(MysqlMetricsManager.class);
	public static void main(String[] args) {
		PropertyConfigurator.configure("config/log4j.properties");
		try {
			Runner runner = new Runner();
			MySQLAgentFactory myagent = new MySQLAgentFactory();
			runner.add(myagent);
			runner.setupAndRun();

		} catch (ConfigurationException e) {
			logger.error((new StringBuilder("ERROR: ")).append(e.getMessage()).toString());
			System.exit(1);
		}
	}
}
